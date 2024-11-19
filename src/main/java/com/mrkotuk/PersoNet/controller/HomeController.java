package com.mrkotuk.PersoNet.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mrkotuk.PersoNet.service.UserService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class HomeController {
    private final UserService userService;

    @GetMapping("/")
    public ResponseEntity<String> greet() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(
                "Welcome " + userService.getUserByEmail(authentication.getName()).getUsername() + " to Perso|||et!",
                HttpStatus.OK);
    }
}