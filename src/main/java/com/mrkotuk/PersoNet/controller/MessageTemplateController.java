package com.mrkotuk.PersoNet.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mrkotuk.PersoNet.domain.model.MessageTemplate;
import com.mrkotuk.PersoNet.service.MessageTemplatesService;

import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/message-templates")
@AllArgsConstructor
public class MessageTemplateController {
    private final MessageTemplatesService messageTemplatesService;

    @GetMapping("/")
    public ResponseEntity<List<MessageTemplate>> getMessageTemplates() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(messageTemplatesService.getMessageTemplates(authentication.getName()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageTemplate> getMessageTemplateById(@PathVariable int id) {
        return new ResponseEntity<>(messageTemplatesService.getMessageTemplateById(id), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<MessageTemplate> addMessageTemplate(@RequestBody MessageTemplate message) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(messageTemplatesService.addMessageTemplate(authentication.getName(), message), HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<MessageTemplate> updateMessageTemplate(@RequestBody MessageTemplate message) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(messageTemplatesService.updateMessageTemplate(authentication.getName(), message), HttpStatus.OK);
    }

    @DeleteMapping("/")
    public ResponseEntity<String> deleteMessageTemplates(@RequestBody List<Integer> id) {
        messageTemplatesService.deleteMessageTemplate(id);
        return new ResponseEntity<>("Message template deleted successfully", HttpStatus.OK);
    }
}
