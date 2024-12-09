package com.mrkotuk.PersoNet.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mrkotuk.PersoNet.model.ForgotPassword;
import com.mrkotuk.PersoNet.model.User;
import com.mrkotuk.PersoNet.repo.UserRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepo repo;
    private final AuthenticationManager authManager;
    private final MessageSenderService messageSenderService;
    private final JWTService jwtService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(7);

    public String register(User user) {
        if (!repo.findByEmail(user.getEmail()).isPresent()) {
            user.setVerified(false);
            user.setPassword(encoder.encode(user.getPassword()));
            repo.save(user);

            return messageSenderService.sendVerificationEmail(user.getEmail());
        } else
            return "Email has already been used!";
    }

    public String verify(User user) {
        if (!repo.findByEmail(user.getEmail()).get().isVerified())
            return messageSenderService.sendVerificationEmail(user.getEmail());

        Authentication authentication = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

        if (authentication.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return jwtService.generateToken(user.getEmail());
        } else
            return "Invalid credentials";
    }

    public String isVerified(String token) {
        String email = messageSenderService.isVerified(token);

        if (email.isEmpty())
            return "Invalid or expired token";

        User user = repo.findByEmail(email).get();
        user.setVerified(true);
        repo.save(user);

        return jwtService.generateToken(email);
    }

    public User getUserByEmail(String email) {
        return repo.findByEmail(email).get();
    }

    public String forgotPassword(ForgotPassword forgotPassword) {
        if (messageSenderService.isVerified(forgotPassword.getVerifyEmailToken()).isEmpty())
            return "Invalid or expired token";

        User user = repo.findByEmail(forgotPassword.getEmail()).get();
        user.setPassword(encoder.encode(forgotPassword.getNewPassword()));
        user.setVerified(true);
        repo.save(user);

        return jwtService.generateToken(forgotPassword.getEmail());
    }

    public String sendVerificationEmail(String email) {
        return messageSenderService.sendVerificationEmail(email);
    }
}