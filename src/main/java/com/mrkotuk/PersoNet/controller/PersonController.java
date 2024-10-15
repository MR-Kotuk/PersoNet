package com.mrkotuk.PersoNet.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<List<Person>> getPerson() {
        return service.getPerson();
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addPerson(@RequestBody Person person) {
        service.addPerson(person);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
