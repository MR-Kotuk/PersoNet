package com.mrkotuk.PersoNet.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mrkotuk.PersoNet.model.Person;
import com.mrkotuk.PersoNet.service.PersonService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/person")
@AllArgsConstructor
public class PersonController {
    private final PersonService service;

    @GetMapping("/")
    public ResponseEntity<List<Person>> getPersons() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return service.getPerson(authentication.getName());
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addPerson(@RequestBody Person person) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        service.addPerson(person, authentication.getName());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/templates")
    public ResponseEntity<List<Person>> getPersonTemplates() {
        return service.getPersonTemplates();
    }

    @GetMapping("/templates/{personType}")
    public ResponseEntity<Person> getPersonTemplate(@PathVariable String personType) {
        return service.getPersonTemplate(personType);
    }
}
