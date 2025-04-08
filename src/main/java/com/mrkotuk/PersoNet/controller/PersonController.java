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

import com.mrkotuk.PersoNet.domain.model.Person;
import com.mrkotuk.PersoNet.service.PersonService;

import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/person")
@AllArgsConstructor
public class PersonController {
    private final PersonService service;

    @GetMapping("/analytic")
    public ResponseEntity<String> getPersonAnalytic() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(service.getPersonAnalytic(authentication.getName()), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<Person>> getPersons() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(service.getPersons(authentication.getName()), HttpStatus.FOUND);
    }

    @GetMapping("/{personId}")
    public ResponseEntity<Person> getPerson(@PathVariable int personId) {
        return new ResponseEntity<>(service.getPerson(personId), HttpStatus.FOUND);
    }

    @PostMapping("/")
    public ResponseEntity<Person> addPerson(@RequestBody Person person) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(service.addPerson(person, authentication.getName()), HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Person> updatePerson(@RequestBody Person person) {
        return new ResponseEntity<>(service.updatePerson(person), HttpStatus.OK);
    }

    @DeleteMapping("/")
    public ResponseEntity<String> deletePerson(@RequestBody List<Integer> id) {
        return new ResponseEntity<>(service.deletePersonsById(id), HttpStatus.OK);
    }
}
