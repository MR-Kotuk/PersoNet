package com.mrkotuk.PersoNet.controller;

import java.util.List;

import com.mrkotuk.PersoNet.domain.enums.PersonType;
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
    private final PersonTemplatesService personTemplatesService;

    @GetMapping("/")
    public ResponseEntity<List<Person>> getPersonTemplates() {
        return new ResponseEntity<>(personTemplatesService.getPersonTemplates(), HttpStatus.OK);
    }

    @GetMapping("/{personType}")
    public ResponseEntity<Person> getPersonTemplate(@PathVariable PersonType personType) {
        return new ResponseEntity<>(personTemplatesService.getPersonTemplate(personType), HttpStatus.OK);
    }

    @GetMapping("/types/all")
    public ResponseEntity<PersonType[]> getPersonTypes() {
        return new ResponseEntity<>(personTemplatesService.getPersonTypes(), HttpStatus.OK);
    }
}
