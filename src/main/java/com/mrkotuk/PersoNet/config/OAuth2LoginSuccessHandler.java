package com.mrkotuk.PersoNet.config;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.mrkotuk.PersoNet.model.User;
import com.mrkotuk.PersoNet.repo.UserRepo;
import com.mrkotuk.PersoNet.service.JWTService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final UserRepo repo;
    private final JWTService jwtService;
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(7);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = oauthToken.getPrincipal();

        String jwtToken = processOAuth2User(oAuth2User, oauthToken);

        response.setContentType("application/json");
        response.getWriter().write(jwtToken);
    }

    public String processOAuth2User(OAuth2User oAuth2User, OAuth2AuthenticationToken oauthToken) {
        String username = oAuth2User.getAttribute("name");

        String email = oAuth2User.getAttribute("email") != null
                ? oAuth2User.getAttribute("email")
                : fetchEmailFromGitHub(oauthToken);

        String id = oAuth2User.getAttribute("sub") != null
                ? oAuth2User.getAttribute("sub")
                : Integer.toString(oAuth2User.getAttribute("id"));

        User userByEmail = repo.findByEmail(email).orElse(null);

        if (userByEmail == null) {
            userByEmail = new User();
            userByEmail.setUsername(username);
            userByEmail.setEmail(email);
            userByEmail.setVerified(true);
            userByEmail.setPassword(encoder.encode(id));
        } else if (!userByEmail.isVerified())
            userByEmail.setVerified(true);

        repo.save(userByEmail);
        return jwtService.generateToken(email);
    }

    private String fetchEmailFromGitHub(OAuth2AuthenticationToken oauthToken) {
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                oauthToken.getAuthorizedClientRegistrationId(),
                oauthToken.getName());

        if (client == null || client.getAccessToken() == null)
            throw new RuntimeException("Access token is missing or invalid!");

        String accessToken = client.getAccessToken().getTokenValue();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String emailApiUrl = "https://api.github.com/user/emails";

        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                emailApiUrl, HttpMethod.GET, entity,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {
                });

        return response.getBody().stream()
                .filter(emailData -> (boolean) emailData.get("primary"))
                .map(emailData -> (String) emailData.get("email"))
                .findFirst()
                .orElse(null);
    }
}