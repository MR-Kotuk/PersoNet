package com.mrkotuk.PersoNet.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.mrkotuk.PersoNet.components.PersonStatus;
import com.mrkotuk.PersoNet.components.PersonType;
import com.mrkotuk.PersoNet.model.Person;
import com.mrkotuk.PersoNet.repo.PersonRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PersonService {
    private final PersonRepo repo;

    public String getPersonAnalytic(String username) {
        List<PersonType> persons = repo.findPersonTypesByStatusAndUsername(PersonStatus.ACTIVE, username);
        Map<PersonType, Integer> analytic = new HashMap<>();

        for (PersonType person : PersonType.values())
            analytic.put(person, (int) persons.stream().filter(type -> type == person).count());

        return analytic.toString();
    }

    public List<Person> searchPersons(String username, String keyword) {
        return repo.findByUsernameAndStatusAndLineValue(username, PersonStatus.ACTIVE, keyword);
    }

    public List<Person> getPersons(String username) {
        return repo.findByStatusAndUsername(PersonStatus.ACTIVE, username);
    }

    public Person getPerson(int personId) {
        return repo.findByStatusAndId(PersonStatus.ACTIVE, personId).get();
    }

    public void addPerson(Person person, String username) {
        for (int i = 0; i < person.getLineTemplates().size(); i++)
            person.getLineTemplates().get(i).setOrderId(i + 1);

        person.setUsername(username);
        person.setPersonStatus(PersonStatus.ACTIVE);
        person.setCreationDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        repo.save(person);
    }

    public void updatePerson(Person person) {
        repo.save(person);
    }

    public void deletePersons(List<Person> persons) {
        for (Person person : persons) {
            person.setPersonStatus(PersonStatus.DELETED);
            repo.save(person);
        }
    }
}