package com.mrkotuk.PersoNet.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mrkotuk.PersoNet.domain.dto.MessageDTO;
import com.mrkotuk.PersoNet.domain.model.Person;
import com.mrkotuk.PersoNet.service.MessageService;

import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/sender")
@AllArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @GetMapping("/")
    public ResponseEntity<List<Person>> getPersonsWithEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(messageService.getPersonsWithEmail(authentication.getName()), HttpStatus.OK);
    }

    @PostMapping("/shared-lines")
    public ResponseEntity<List<String>> getSharedLines(@RequestBody List<Integer> id) {
        return new ResponseEntity<>(messageService.getSharedLines(id), HttpStatus.OK);
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody MessageDTO message) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        messageService.sendMessage(authentication.getName(), message);
        return new ResponseEntity<>("Message sent successfully", HttpStatus.OK);
    }
}
