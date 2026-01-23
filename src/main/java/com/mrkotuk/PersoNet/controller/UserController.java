package com.mrkotuk.PersoNet.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mrkotuk.PersoNet.domain.dto.ForgotPasswordDTO;
import com.mrkotuk.PersoNet.domain.model.User;
import com.mrkotuk.PersoNet.service.UserService;

import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        return new ResponseEntity<>(userService.register(user), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        return new ResponseEntity<>(userService.verify(user), HttpStatus.OK);
    }

    @GetMapping("/verify-email/{token}")
    public ResponseEntity<String> verifyEmail(@PathVariable String token) {
        return new ResponseEntity<>(userService.isVerified(token), HttpStatus.OK);
    }

    @PostMapping("/verify-email/send/{email}")
    public ResponseEntity<String> sendVerifyEmail(@PathVariable String email) {
        return new ResponseEntity<>(userService.sendVerificationEmail(email), HttpStatus.OK);
    }

    @PostMapping("/forgot-password/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestBody ForgotPasswordDTO forgotPassword) {
        return new ResponseEntity<>(userService.forgotPassword(forgotPassword), HttpStatus.OK);
    }
}
