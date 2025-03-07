package com.mrkotuk.PersoNet.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mrkotuk.PersoNet.domain.model.Person;
import com.mrkotuk.PersoNet.service.PersonTemplatesService;

import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/person/templates")
@AllArgsConstructor
public class PersonTemplatesController {
    private final PersonTemplatesService service;

    @GetMapping("/")
    public ResponseEntity<List<Person>> getPersonTemplates() {
        return new ResponseEntity<>(service.getPersonTemplates(), HttpStatus.FOUND);
    }

    @GetMapping("/{personType}")
    public ResponseEntity<Person> getPersonTemplate(@PathVariable String personType) {
        Person person = service.getPersonTemplate(personType);

        return person != null
                ? new ResponseEntity<>(person, HttpStatus.FOUND)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}