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

    public List<Person> searchPersons(String email, String keyword) {
        return repo.findByEmailAndStatusAndLineValue(email, PersonStatus.DELETED, keyword);
    }

    public List<Person> getPersons(String email) {
        return repo.findByStatusAndEmail(PersonStatus.DELETED, email);
    }

    public List<Person> removeFromRecycleBin(List<Integer> id, String email) {
        for (Person person : repo.findAllById(id))
            if (person.getPersonStatus().equals(PersonStatus.DELETED))
                repo.delete(person);

        return repo.findByStatusAndEmail(PersonStatus.DELETED, email);
    }

    public void cleanRecycleBin(String email) {
        repo.deleteAll(repo.findByStatusAndEmail(PersonStatus.DELETED, email));
    }

    public List<Person> returnFromRecycleBin(List<Integer> id, String email) {
        for (Person person : repo.findAllById(id)) {
            if (person.getPersonStatus().equals(PersonStatus.DELETED)) {
                person.setPersonStatus(PersonStatus.ACTIVE);
                repo.save(person);
            }
        }

        return repo.findByStatusAndEmail(PersonStatus.DELETED, email);
    }
}