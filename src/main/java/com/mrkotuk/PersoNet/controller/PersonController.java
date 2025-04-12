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
    private final PersonService personService;

    @GetMapping("/analytic")
    public ResponseEntity<String> getPersonAnalytic() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(personService.getPersonAnalytic(authentication.getName()), HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<Person>> getPersons() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(personService.getPersons(authentication.getName()), HttpStatus.OK);
    }

    @GetMapping("/{personId}")
    public ResponseEntity<Person> getPerson(@PathVariable int personId) {
        return new ResponseEntity<>(personService.getPerson(personId), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Person> addPerson(@RequestBody Person person) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(personService.addPerson(person, authentication.getName()), HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Person> updatePerson(@RequestBody Person person) {
        return new ResponseEntity<>(personService.updatePerson(person), HttpStatus.OK);
    }

    @DeleteMapping("/")
    public ResponseEntity<String> deletePerson(@RequestBody List<Integer> id) {
        personService.deletePersonsById(id);
        return new ResponseEntity<>("Persons deleted successfully", HttpStatus.OK);
    }
}
