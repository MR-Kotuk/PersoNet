package com.mrkotuk.PersoNet.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mrkotuk.PersoNet.exception.NotFoundException;
import com.mrkotuk.PersoNet.repository.PersonRepository;
import org.springframework.stereotype.Service;

import com.mrkotuk.PersoNet.domain.enums.PersonStatus;
import com.mrkotuk.PersoNet.domain.enums.PersonType;
import com.mrkotuk.PersoNet.domain.model.Person;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;

    public Map<PersonType, Integer> getPersonAnalytic(String email) {
        List<PersonType> persons = personRepository.findPersonTypesByStatusAndEmail(PersonStatus.ACTIVE, email);
        Map<PersonType, Integer> analytic = new HashMap<>();

        for (PersonType person : PersonType.values())
            analytic.put(person, (int) persons.stream().filter(type -> type == person).count());

        return analytic;
    }

    public List<Person> getPersons(String email) {
        return personRepository.findByStatusAndEmail(PersonStatus.ACTIVE, email);
    }

    public Person getPerson(int personId) {
        return personRepository.findByStatusAndId(PersonStatus.ACTIVE, personId)
                .orElseThrow(() -> new NotFoundException("Not found person with id: " + personId));
    }

    public Person addPerson(Person person, String email) {
        person.setEmail(email);
        person.setPersonStatus(PersonStatus.ACTIVE);
        person.setCreationDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

        return personRepository.save(person);
    }

    public Person updatePerson(Person person) {
        return personRepository.save(person);
    }

    public void deletePersonsById(List<Integer> id) {
        for (Person person : personRepository.findAllById(id)) {
            person.setPersonStatus(PersonStatus.DELETED);
            personRepository.save(person);
        }
    }
}
