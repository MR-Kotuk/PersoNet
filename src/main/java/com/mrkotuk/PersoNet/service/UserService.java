package com.mrkotuk.PersoNet.service;

import com.mrkotuk.PersoNet.domain.enums.Role;
import com.mrkotuk.PersoNet.exception.BadRequestException;
import com.mrkotuk.PersoNet.exception.NotFoundException;
import com.mrkotuk.PersoNet.exception.UnauthorizedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mrkotuk.PersoNet.domain.dto.ForgotPasswordDTO;
import com.mrkotuk.PersoNet.domain.model.User;
import com.mrkotuk.PersoNet.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authManager;
    private final MessageSenderService messageSenderService;
    private final JWTService jwtService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(7);

    public String register(User user) {
        if (userRepository.findByEmail(user.getEmail()).isEmpty()) {
            user.setVerified(false);
            user.setRole(Role.MEMBER);
            user.setPassword(encoder.encode(user.getPassword()));
            userRepository.save(user);

            return messageSenderService.sendVerificationEmail(user.getEmail());
        } else
            throw new BadRequestException("Email " + user.getEmail() + " has already been used!");
    }

    public String verify(User user) {
        User existingUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found with email: " + user.getEmail()));
        if (!existingUser.isVerified())
            throw new BadRequestException("Email is not verified");

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(user.getEmail());
        } else
            throw new UnauthorizedException("Invalid credentials");
    }

    public String isVerified(String token) {
        String email = messageSenderService.isVerified(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Not found user with email: " + email));

        user.setVerified(true);
        userRepository.save(user);

        return jwtService.generateToken(email);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));
    }

    public String forgotPassword(ForgotPasswordDTO forgotPassword) {
        User user = userRepository.findByEmail(forgotPassword.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found with email: " + forgotPassword.getEmail()));
        user.setPassword(encoder.encode(forgotPassword.getNewPassword()));
        user.setVerified(true);
        userRepository.save(user);

        return jwtService.generateToken(forgotPassword.getEmail());
    }

    public String sendVerificationEmail(String email) {
        return messageSenderService.sendVerificationEmail(email);
    }
}
