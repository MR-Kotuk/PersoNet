package com.mrkotuk.PersoNet.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mrkotuk.PersoNet.model.Message;
import com.mrkotuk.PersoNet.model.Person;
import com.mrkotuk.PersoNet.service.MessageService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/sender")
@AllArgsConstructor
public class MessageController {
    private final MessageService service;;

    @GetMapping("/")
    public ResponseEntity<List<Person>> getPersonsWithEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(service.getPersonsWithEmail(authentication.getName()), HttpStatus.FOUND);
    }

    @GetMapping("/sharedlines")
    public ResponseEntity<List<String>> getSharedLines(@RequestBody List<Integer> id) {
        return new ResponseEntity<>(service.getSharedLines(id), HttpStatus.FOUND);
    }

    @PostMapping("/send")
    public ResponseEntity<Void> sendMessage(@RequestBody Message message) {
        service.sendMessage(message);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}