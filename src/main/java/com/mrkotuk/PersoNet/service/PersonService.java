package com.mrkotuk.PersoNet.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mrkotuk.PersoNet.model.Person;
import com.mrkotuk.PersoNet.repo.PersonRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PersonService {
    private final PersonRepo repo;

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
}
