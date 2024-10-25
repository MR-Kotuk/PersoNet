package com.mrkotuk.PersoNet.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public String getPersonAnalytic(String username) {
        List<PersonType> persons = repo.findPersonTypesByUsername(username);
        Map<PersonType, Integer> analytic = new HashMap<>();

        for (PersonType person : PersonType.values())
            analytic.put(person, (int) persons.stream().filter(type -> type == person).count());

        return analytic.toString();
    }

    public List<Person> getPersonTemplates() {
        return director.getAllPersons();
    }

    public Person getPersonTemplate(String personType) {
        return director.getPerson(PersonType.valueOf(personType.toUpperCase()));
    }

    public List<Person> getPersons(String username) {
        return repo.findByUsername(username);
    }

    public Person getPerson(int personId) {
        return repo.findById(personId).isPresent()
                ? repo.findById(personId).get()
                : null;
    }

    public void addPerson(Person person, String username) {
        for (int i = 0; i < person.getLineTemplates().size(); i++)
            person.getLineTemplates().get(i).setOrderId(i + 1);

        person.setUsername(username);
        repo.save(person);
    }

    public void updatePerson(Person person) {
        repo.save(person);
    }

    public void deletePerson(int personId) {
        repo.deleteById(personId);
    }
}