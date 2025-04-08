package com.mrkotuk.PersoNet.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.mrkotuk.PersoNet.exception.NotFoundException;
import org.springframework.stereotype.Service;

import com.mrkotuk.PersoNet.domain.enums.PersonStatus;
import com.mrkotuk.PersoNet.domain.enums.PersonType;
import com.mrkotuk.PersoNet.domain.model.Person;
import com.mrkotuk.PersoNet.repository.PersonRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PersonService {
    private final PersonRepository repository;

    public String getPersonAnalytic(String email) {
        List<PersonType> persons = repository.findPersonTypesByStatusAndEmail(PersonStatus.ACTIVE, email);
        Map<PersonType, Integer> analytic = new HashMap<>();

        for (PersonType person : PersonType.values())
            analytic.put(person, (int) persons.stream().filter(type -> type == person).count());

        return analytic.toString();
    }

    public List<Person> getPersons(String email) {
        List<Person> persons = repository.findByStatusAndEmail(PersonStatus.ACTIVE, email);
        if (persons == null || persons.isEmpty())
            throw new NotFoundException("Persons not found");

        return persons;
    }

    public Person getPerson(int personId) {
        Optional<Person> persons = repository.findByStatusAndId(PersonStatus.ACTIVE, personId);
        if (persons.isEmpty())
            throw new NotFoundException("Person not found");

        return persons.get();
    }

    public Person addPerson(Person person, String email) {
        person.setEmail(email);
        person.setPersonStatus(PersonStatus.ACTIVE);
        person.setCreationDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

        return repository.save(person);
    }

    public Person updatePerson(Person person) {
        return repository.save(person);
    }

    public String deletePersonsById(List<Integer> id) {
        for (Person person : repository.findAllById(id)) {
            person.setPersonStatus(PersonStatus.DELETED);
            repository.save(person);
        }

        return "Persons deleted successfully";
    }
}
