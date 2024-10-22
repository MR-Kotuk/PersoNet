package com.mrkotuk.PersoNet.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

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
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(7);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = oauthToken.getPrincipal();

        String jwtToken = processOAuth2User(oAuth2User);

        response.setContentType("application/json");
        response.getWriter().write(jwtToken != null
                ? jwtToken
                : "Username has already been used!: " + oAuth2User.getAttribute("sub"));
    }

    public String processOAuth2User(OAuth2User oAuth2User) {
        String username = oAuth2User.getAttribute("name");
        String googleId = oAuth2User.getAttribute("sub");

        User user = repo.findByUsername(username).orElse(null);

        if (user == null) {
            user = new User();
            user.setUsername(username);
            user.setPassword(encoder.encode(googleId));
            repo.save(user);
        } else if (user != null && !encoder.matches(googleId, user.getPassword()))
            return null;

        return jwtService.generateToken(user.getUsername());
    }
}