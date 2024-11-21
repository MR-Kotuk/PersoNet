package com.mrkotuk.PersoNet.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @GetMapping("/analytic")
    public ResponseEntity<String> getPersonAnalytic() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(service.getPersonAnalytic(authentication.getName()), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Person>> searchPerson(@RequestBody String keyword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(service.searchPersons(authentication.getName(), keyword), HttpStatus.FOUND);
    }

    @GetMapping("/")
    public ResponseEntity<List<Person>> getPersons() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Person> persons = service.getPersons(authentication.getName());

        return persons != null
                ? new ResponseEntity<>(persons, HttpStatus.FOUND)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{personId}")
    public ResponseEntity<Person> getPerson(@PathVariable int personId) {
        Person persons = service.getPerson(personId);

        return persons != null
                ? new ResponseEntity<>(persons, HttpStatus.FOUND)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/")
    public ResponseEntity<Void> addPerson(@RequestBody Person person) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        service.addPerson(person, authentication.getName());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<List<Person>> updatePerson(@RequestBody Person person) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        service.updatePerson(person);

        return new ResponseEntity<>(service.getPersons(authentication.getName()), HttpStatus.OK);
    }

    @DeleteMapping("/")
    public ResponseEntity<List<Person>> deletePerson(@RequestBody List<Integer> id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        service.deletePersonsById(id);

        return new ResponseEntity<>(service.getPersons(authentication.getName()), HttpStatus.OK);
    }
}