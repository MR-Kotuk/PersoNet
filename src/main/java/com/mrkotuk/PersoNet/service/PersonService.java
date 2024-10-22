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
        for (int i = 0; i < person.getLineTemplates().size(); i++)
            person.getLineTemplates().get(i).setOrderId(i + 1);

        person.setUsername(username);
        repo.save(person);
    }

    public List<Person> getPersons(String username) {
        return repo.findByUsername(username);
    }

    public Person getPerson(int personId) {
        return repo.findById(personId).isPresent()
                ? repo.findById(personId).get()
                : null;
    }

    public List<Person> getPersonTemplates() {
        return director.getAllPersons();
    }

    public Person getPersonTemplate(String personType) {
        return director.getPerson(PersonType.valueOf(personType.toUpperCase()));
    }

    public void updatePerson(Person person) {
        repo.save(person);
    }

    public void deletePerson(int personId) {
        repo.deleteById(personId);
    }
}
