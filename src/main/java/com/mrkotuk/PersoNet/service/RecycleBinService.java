package com.mrkotuk.PersoNet.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mrkotuk.PersoNet.components.PersonStatus;
import com.mrkotuk.PersoNet.model.Person;
import com.mrkotuk.PersoNet.repo.PersonRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RecycleBinService {
    private final PersonRepo repo;

    public List<Person> searchPersons(String username, String keyword) {
        return repo.findByUsernameAndStatusAndLineValue(username, PersonStatus.DELETED, keyword);
    }

    public List<Person> getPersons(String username) {
        return repo.findByStatusAndUsername(PersonStatus.DELETED, username);
    }

    public List<Person> removeFromRecycleBin(List<Person> persons, String username) {
        for (Person person : persons)
            if (person.getPersonStatus().equals(PersonStatus.DELETED))
                repo.delete(person);

        return repo.findByStatusAndUsername(PersonStatus.DELETED, username);
    }

    public void cleanRecycleBin(String username) {
        repo.deleteAll(repo.findByStatusAndUsername(PersonStatus.DELETED, username));
    }

    public List<Person> returnFromRecycleBin(List<Person> persons, String username) {
        for (Person person : persons) {
            if (person.getPersonStatus().equals(PersonStatus.DELETED)) {
                person.setPersonStatus(PersonStatus.ACTIVE);
                repo.save(person);
            }
        }

        return repo.findByStatusAndUsername(PersonStatus.DELETED, username);
    }
}