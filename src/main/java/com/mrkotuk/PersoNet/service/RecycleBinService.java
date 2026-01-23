package com.mrkotuk.PersoNet.service;

import java.util.List;

import com.mrkotuk.PersoNet.repository.PersonRepository;
import org.springframework.stereotype.Service;

import com.mrkotuk.PersoNet.domain.enums.PersonStatus;
import com.mrkotuk.PersoNet.domain.model.Person;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RecycleBinService {
    private final PersonRepository personRepository;

    public List<Person> getPersons(String email) {
        return personRepository.findByStatusAndEmail(PersonStatus.DELETED, email);
    }

    public void removeFromRecycleBin(List<Integer> id) {
        for (Person person : personRepository.findAllById(id)) {
            if (person.getPersonStatus().equals(PersonStatus.DELETED)) {
                personRepository.delete(person);
            }
        }
    }

    public void cleanRecycleBin(String email) {
        removeFromRecycleBin(personRepository.findByStatusAndEmail(
                PersonStatus.DELETED, email).stream().map(Person::getPersonId).toList());
    }

    public void returnFromRecycleBin(List<Integer> id) {
        for (Person person : personRepository.findAllById(id)) {
            if (person.getPersonStatus().equals(PersonStatus.DELETED)) {
                person.setPersonStatus(PersonStatus.ACTIVE);
                personRepository.save(person);
            }
        }
    }
}
