package com.mrkotuk.PersoNet.service;

import com.mrkotuk.PersoNet.exception.BadRequestException;
import com.mrkotuk.PersoNet.exception.NotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mrkotuk.PersoNet.domain.dto.PasswordDTO;
import com.mrkotuk.PersoNet.domain.model.User;
import com.mrkotuk.PersoNet.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AccountService {
    private final UserRepository userRepository;
    private final AuthenticationManager authManager;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(7);

    public User getAccountInfo(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));

        user.setPassword("**********");
        return user;
    }

    public void setUsername(String email, String newUsername) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));

        user.setUsername(newUsername);
        userRepository.save(user);
    }

    public void setPassword(String email, PasswordDTO password) {
        Authentication authentication = authManager.authenticate(
                        new UsernamePasswordAuthenticationToken(email, password.getPassword()));

        if (authentication.isAuthenticated()) {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new NotFoundException("User not found with email: " + email));

            user.setPassword(encoder.encode(password.getNewPassword()));
            userRepository.save(user);
        } else
            throw new BadRequestException("Wrong password when changing password with email: " + email);
    }
}
