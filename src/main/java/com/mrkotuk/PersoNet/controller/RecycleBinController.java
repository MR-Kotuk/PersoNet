package com.mrkotuk.PersoNet.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mrkotuk.PersoNet.model.Person;
import com.mrkotuk.PersoNet.service.RecycleBinService;

import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/recyclebin")
@AllArgsConstructor
public class RecycleBinController {
    private final RecycleBinService service;

    @GetMapping("/search")
    public ResponseEntity<List<Person>> searchPerson(String keyword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Person> persons = service.searchPersons(authentication.getName(), keyword);

        return persons != null
                ? new ResponseEntity<>(persons, HttpStatus.FOUND)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/")
    public ResponseEntity<List<Person>> getPersons() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Person> persons = service.getPersons(authentication.getName());

        return persons != null
                ? new ResponseEntity<>(persons, HttpStatus.FOUND)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/")
    public ResponseEntity<List<Person>> returnFromRecycleBin(@RequestBody List<Integer> id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(service.returnFromRecycleBin(id, authentication.getName()), HttpStatus.OK);
    }

    @DeleteMapping("/")
    public ResponseEntity<List<Person>> removeFromRecycleBin(@RequestBody List<Integer> id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(service.removeFromRecycleBin(id, authentication.getName()), HttpStatus.OK);
    }

    @DeleteMapping("/clean")
    public ResponseEntity<Void> cleanRecycleBin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        service.cleanRecycleBin(authentication.getName());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}