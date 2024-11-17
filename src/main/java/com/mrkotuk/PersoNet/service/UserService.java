package com.mrkotuk.PersoNet.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mrkotuk.PersoNet.model.User;
import com.mrkotuk.PersoNet.repo.UserRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepo repo;
    private final AuthenticationManager authManager;
    private final EmailService emailService;
    private final JWTService jwtService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(7);

    public String register(User user) {
        if (!repo.findByEmail(user.getEmail()).isPresent()) {
            emailService.sendVerificatonEmail(user.getEmail(), "1234");
            user.setVerified(false);
            user.setPassword(encoder.encode(user.getPassword()));
            repo.save(user);

            // return jwtService.generateToken(user.getUsername());
            return "Please verify email";
        } else
            return "Email has already been used!";
    }

    public String verify(User user) {
        Authentication authentication = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        return authentication.isAuthenticated()
                ? jwtService.generateToken(user.getUsername())
                : "Invalid credentials";
    }

    public boolean isVerified(String token) {
        return token.equals("1234");
    }
}