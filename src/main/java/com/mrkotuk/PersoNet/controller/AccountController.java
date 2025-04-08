package com.mrkotuk.PersoNet.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mrkotuk.PersoNet.domain.model.Password;
import com.mrkotuk.PersoNet.domain.model.User;
import com.mrkotuk.PersoNet.service.AccountService;

import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {
    private final AccountService service;

    @GetMapping("/")
    public ResponseEntity<User> getAccountInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(service.getAccountInfo(authentication.getName()), HttpStatus.FOUND);
    }

    @PutMapping("/set-username")
    public ResponseEntity<String> setUsername(@RequestBody String newUsername) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(service.setUsername(authentication.getName(), newUsername), HttpStatus.OK);
    }

    @PutMapping("/set-password")
    public ResponseEntity<String> setPassword(@RequestBody Password password) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(service.setPassword(authentication.getName(), password), HttpStatus.OK);
    }
}
