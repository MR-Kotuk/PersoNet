package com.mrkotuk.PersoNet.service;

import com.mrkotuk.PersoNet.domain.enums.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mrkotuk.PersoNet.domain.model.ForgotPassword;
import com.mrkotuk.PersoNet.domain.model.User;
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

    public ResponseEntity<String> register(User user) {
        if (!repo.findByEmail(user.getEmail()).isPresent()) {
            user.setVerified(false);
            user.setRole(Role.MEMBER);
            user.setPassword(encoder.encode(user.getPassword()));
            repo.save(user);

            return messageSenderService.sendVerificationEmail(user.getEmail());
        } else
            return new ResponseEntity<>("Email has already been used!", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> verify(User user) {
        if (!repo.findByEmail(user.getEmail()).get().isVerified())
            return messageSenderService.sendVerificationEmail(user.getEmail());

        Authentication authentication = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

        if (authentication.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return new ResponseEntity<>(jwtService.generateToken(user.getEmail()), HttpStatus.OK);
        } else
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<String> isVerified(String token) {
        String email = messageSenderService.isVerified(token);

        if (email.isEmpty())
            return new ResponseEntity<>("Invalid or expired token", HttpStatus.BAD_REQUEST);

        User user = repo.findByEmail(email).get();
        user.setVerified(true);
        repo.save(user);

        return new ResponseEntity<>(jwtService.generateToken(email), HttpStatus.OK);
    }

    public User getUserByEmail(String email) {
        return repo.findByEmail(email).get();
    }

    public ResponseEntity<String> forgotPassword(ForgotPassword forgotPassword) {
        if (messageSenderService.isVerified(forgotPassword.getVerifyEmailToken()).isEmpty())
            return new ResponseEntity<>("Invalid or expired token", HttpStatus.BAD_REQUEST);

        User user = repo.findByEmail(forgotPassword.getEmail()).get();
        user.setPassword(encoder.encode(forgotPassword.getNewPassword()));
        user.setVerified(true);
        repo.save(user);

        return new ResponseEntity<>(jwtService.generateToken(forgotPassword.getEmail()), HttpStatus.OK);
    }

    public ResponseEntity<String> sendVerificationEmail(String email) {
        return messageSenderService.sendVerificationEmail(email);
    }
}