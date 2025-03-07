package com.mrkotuk.PersoNet.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.mrkotuk.PersoNet.components.PersonStatus;
import com.mrkotuk.PersoNet.components.PersonType;
import com.mrkotuk.PersoNet.domain.model.Person;
import com.mrkotuk.PersoNet.repo.PersonRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PersonService {
    private final PersonRepo repo;

    public String getPersonAnalytic(String email) {
        List<PersonType> persons = repo.findPersonTypesByStatusAndEmail(PersonStatus.ACTIVE, email);
        Map<PersonType, Integer> analytic = new HashMap<>();

        for (PersonType person : PersonType.values())
            analytic.put(person, (int) persons.stream().filter(type -> type == person).count());

        return analytic.toString();
    }

    public List<Person> getPersons(String email) {
        return repo.findByStatusAndEmail(PersonStatus.ACTIVE, email);
    }

    public Person getPerson(int personId) {
        return repo.findByStatusAndId(PersonStatus.ACTIVE, personId).get();
    }

    public Person addPerson(Person person, String email) {
        person.setEmail(email);
        person.setPersonStatus(PersonStatus.ACTIVE);
        person.setCreationDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

        return repo.save(person);
    }

    public Person updatePerson(Person person) {
        return repo.save(person);
    }

    public String deletePersonsById(List<Integer> id) {
        for (Person person : repo.findAllById(id)) {
            person.setPersonStatus(PersonStatus.DELETED);
            repo.save(person);
        }

        return "Persons deleted successfully";
    }
}