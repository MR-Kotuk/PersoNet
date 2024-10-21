package com.mrkotuk.PersoNet.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mrkotuk.PersoNet.components.PersonDirector;
import com.mrkotuk.PersoNet.components.PersonType;
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

    public List<Person> getPerson(String username) {
        return repo.findByUsername(username);
    }

    public List<Person> getPersonTemplates() {
        return director.getAllPersons();
    }

    public Person getPersonTemplate(String personType) {
        return director.getPerson(PersonType.valueOf(personType.toUpperCase()));
    }
}
