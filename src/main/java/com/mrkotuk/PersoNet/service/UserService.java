package com.mrkotuk.PersoNet.service;

import com.mrkotuk.PersoNet.domain.enums.Role;
import com.mrkotuk.PersoNet.exception.BadRequestException;
import com.mrkotuk.PersoNet.exception.UnauthorizedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mrkotuk.PersoNet.domain.model.ForgotPassword;
import com.mrkotuk.PersoNet.domain.model.User;
import com.mrkotuk.PersoNet.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final AuthenticationManager authManager;
    private final MessageSenderService messageSenderService;
    private final JWTService jwtService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(7);

    public String register(User user) {
        if (!repository.findByEmail(user.getEmail()).isPresent()) {
            user.setVerified(false);
            user.setRole(Role.MEMBER);
            user.setPassword(encoder.encode(user.getPassword()));
            repository.save(user);

            return messageSenderService.sendVerificationEmail(user.getEmail());
        } else
            throw new BadRequestException("Email has already been used!");
    }

    public String verify(User user) {
        if (!repository.findByEmail(user.getEmail()).get().isVerified())
            return messageSenderService.sendVerificationEmail(user.getEmail());

        Authentication authentication = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

        if (authentication.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return jwtService.generateToken(user.getEmail());
        } else
            throw new UnauthorizedException("Invalid credentials");
    }

    public String isVerified(String token) {
        String email = messageSenderService.isVerified(token);

        if (email.isEmpty())
            throw new BadRequestException("Invalid or expired token");

        User user = repository.findByEmail(email).get();
        user.setVerified(true);
        repository.save(user);

        return jwtService.generateToken(email);
    }

    public User getUserByEmail(String email) {
        return repository.findByEmail(email).get();
    }

    public String forgotPassword(ForgotPassword forgotPassword) {
        if (messageSenderService.isVerified(forgotPassword.getVerifyEmailToken()).isEmpty())
            throw new BadRequestException("Invalid or expired token");

        User user = repository.findByEmail(forgotPassword.getEmail()).get();
        user.setPassword(encoder.encode(forgotPassword.getNewPassword()));
        user.setVerified(true);
        repository.save(user);

        return jwtService.generateToken(forgotPassword.getEmail());
    }

    public String sendVerificationEmail(String email) {
        return messageSenderService.sendVerificationEmail(email);
    }
}
