package com.mrkotuk.PersoNet.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mrkotuk.PersoNet.components.PersonDirector;
import com.mrkotuk.PersoNet.model.Person;
import com.mrkotuk.PersoNet.repo.PersonRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PersonService {
    private final PersonRepo repo;
    private final PersonDirector director;

    public void addPerson(Person person, String username) {
        person.setUsername(username);
        repo.save(person);
    }

    public ResponseEntity<List<Person>> getPerson(String username) {
        List<Person> persons = repo.findByUsername(username);

        return persons != null
                ? new ResponseEntity<>(persons, HttpStatus.FOUND)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<List<Person>> getPersonTemplates() {
        return new ResponseEntity<>(director.createAll(), HttpStatus.CREATED);
    }

    public ResponseEntity<Person> getPersonTemplate(String personType) {
        return switch (personType) {
            case "general" -> new ResponseEntity<>(director.createGeneralPerson(), HttpStatus.CREATED);
            case "friend" -> new ResponseEntity<>(director.createFriendPerson(), HttpStatus.CREATED);
            case "colleague" -> new ResponseEntity<>(director.createColleaguePerson(), HttpStatus.CREATED);
            case "family" -> new ResponseEntity<>(director.createFamilyPerson(), HttpStatus.CREATED);
            case "client" -> new ResponseEntity<>(director.createClientPerson(), HttpStatus.CREATED);
            case "custom" -> new ResponseEntity<>(director.createCustomPerson(), HttpStatus.CREATED);
            default -> new ResponseEntity<>(HttpStatus.NOT_FOUND);
        };
    }
}
