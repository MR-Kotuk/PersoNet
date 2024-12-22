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

    public List<Person> getPersons(String email) {
        return repo.findByStatusAndEmail(PersonStatus.DELETED, email);
    }

    public String removeFromRecycleBin(List<Integer> id) {
        for (Person person : repo.findAllById(id))
            if (person.getPersonStatus().equals(PersonStatus.DELETED))
                repo.delete(person);

        return "Persons removed from the recycle bin successfully";
    }

    public String cleanRecycleBin(String email) {
        repo.deleteAll(repo.findByStatusAndEmail(PersonStatus.DELETED, email));
        return "Recycle bin cleaned successfully";
    }

    public String returnFromRecycleBin(List<Integer> id) {
        for (Person person : repo.findAllById(id)) {
            if (person.getPersonStatus().equals(PersonStatus.DELETED)) {
                person.setPersonStatus(PersonStatus.ACTIVE);
                repo.save(person);
            }
        }

        return "Persons returned from the recycle bin successfully";
    }
}