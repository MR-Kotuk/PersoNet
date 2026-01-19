package com.mrkotuk.PersoNet.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mrkotuk.PersoNet.domain.enums.PersonStatus;
import com.mrkotuk.PersoNet.domain.enums.PersonType;
import com.mrkotuk.PersoNet.domain.model.Person;
import com.mrkotuk.PersoNet.domain.dto.SearchFilterDTO;
import com.mrkotuk.PersoNet.service.SearchService;

import lombok.AllArgsConstructor;

@Controller
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/person/search")
public class SearchController {
    private final SearchService searchService;

    @PostMapping("/")
    public ResponseEntity<List<Person>> searchPerson(@RequestBody SearchFilterDTO filter) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(searchService.searchPersons(authentication.getName(), filter), HttpStatus.OK);
    }

    @GetMapping("/tags")
    public ResponseEntity<List<String>> getTags() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(searchService.getTags(authentication.getName()), HttpStatus.OK);
    }

    @GetMapping("/types")
    public ResponseEntity<List<PersonType>> getTypes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(searchService.getTypes(authentication.getName()), HttpStatus.OK);
    }

    @GetMapping("/statuses")
    public ResponseEntity<List<PersonStatus>> getStatuses() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(searchService.getStatuses(authentication.getName()), HttpStatus.OK);
    }
}
