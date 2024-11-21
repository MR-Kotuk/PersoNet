package com.mrkotuk.PersoNet.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
            user.setVerified(false);
            user.setPassword(encoder.encode(user.getPassword()));

            repo.save(user);

            return emailService.sendVerificationEmail(user);
        } else
            return "Email has already been used!";
    }

    public String verify(User user) {
        if (!repo.findByEmail(user.getEmail()).get().isVerified())
            return emailService.sendVerificationEmail(repo.findByEmail(user.getEmail()).get());

        Authentication authentication = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

        if (authentication.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return jwtService.generateToken(user.getEmail());
        } else
            return "Invalid credentials";
    }

    public String isVerified(String token) {
        return emailService.isVerified(token);
    }

    public User getUserByEmail(String email) {
        return repo.findByEmail(email).get();
    }
}