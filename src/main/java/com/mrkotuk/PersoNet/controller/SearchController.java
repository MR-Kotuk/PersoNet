package com.mrkotuk.PersoNet.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mrkotuk.PersoNet.components.PersonStatus;
import com.mrkotuk.PersoNet.components.PersonType;
import com.mrkotuk.PersoNet.model.Person;
import com.mrkotuk.PersoNet.model.SearchFilter;
import com.mrkotuk.PersoNet.service.SearchService;

import lombok.AllArgsConstructor;

@Controller
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/person/search")
public class SearchController {
    private final SearchService service;

    @GetMapping("/")
    public ResponseEntity<List<Person>> searchPerson(@RequestBody SearchFilter filter) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Person> persons = service.searchPersons(authentication.getName(), filter);

        return !persons.isEmpty()
                ? new ResponseEntity<>(persons, HttpStatus.FOUND)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/tags")
    public ResponseEntity<List<String>> getTags() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<String> tags = service.getTags(authentication.getName());

        return !tags.isEmpty()
                ? new ResponseEntity<>(tags, HttpStatus.FOUND)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/types")
    public ResponseEntity<List<PersonType>> getTypes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<PersonType> types = service.getTypes(authentication.getName());

        return !types.isEmpty()
                ? new ResponseEntity<>(types, HttpStatus.FOUND)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/statuses")
    public ResponseEntity<List<PersonStatus>> getStatuses() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<PersonStatus> statuses = service.getStatuses(authentication.getName());

        return !statuses.isEmpty()
                ? new ResponseEntity<>(statuses, HttpStatus.FOUND)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}