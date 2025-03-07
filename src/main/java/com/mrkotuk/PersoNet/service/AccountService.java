package com.mrkotuk.PersoNet.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mrkotuk.PersoNet.domain.model.Password;
import com.mrkotuk.PersoNet.domain.model.User;
import com.mrkotuk.PersoNet.repo.UserRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AccountService {
    private final UserRepo repo;
    private final AuthenticationManager authManager;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(7);

    public User getAccountInfo(String email) {
        User user = repo.findByEmail(email).get();
        user.setPassword("**********");
        return user;
    }

    public String setUsername(String email, String newUsername) {
        User user = repo.findByEmail(email).get();
        user.setUsername(newUsername);
        repo.save(user);

        return "Username changed successful!";
    }

    public boolean setPassword(String email, Password password) {
        User user = repo.findByEmail(email).get();

        Authentication authentication = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(email, password.getPassword()));

        if (authentication.isAuthenticated()) {
            user.setPassword(encoder.encode(password.getNewPassword()));
            repo.save(user);

            return true;
        } else
            return false;
    }
}