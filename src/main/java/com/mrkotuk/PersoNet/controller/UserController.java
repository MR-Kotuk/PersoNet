package com.mrkotuk.PersoNet.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mrkotuk.PersoNet.model.User;
import com.mrkotuk.PersoNet.service.UserService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        return new ResponseEntity<>(service.register(user), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        return new ResponseEntity<>(service.verify(user), HttpStatus.OK);
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        return service.isVerified(token)
                ? new ResponseEntity<>("Email verified successfully!", HttpStatus.OK)
                : new ResponseEntity<>("Invalid or expired token.", HttpStatus.NOT_FOUND);
    }
}