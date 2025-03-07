package com.mrkotuk.PersoNet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mrkotuk.PersoNet.domain.model.ForgotPassword;
import com.mrkotuk.PersoNet.domain.model.User;
import com.mrkotuk.PersoNet.service.UserService;

import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        return service.register(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        return service.verify(user);
    }

    @GetMapping("/verify-email/{token}")
    public ResponseEntity<String> verifyEmail(@PathVariable String token) {
        return service.isVerified(token);
    }

    @PostMapping("/forgot-password/verify-email/send/{email}")
    public ResponseEntity<String> sendVerifyEmail(@PathVariable String email) {
        return service.sendVerificationEmail(email);
    }

    @PostMapping("/forgot-password/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestBody ForgotPassword forgotPassword) {
        return service.forgotPassword(forgotPassword);
    }
}