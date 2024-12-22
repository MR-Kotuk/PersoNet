package com.mrkotuk.PersoNet.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mrkotuk.PersoNet.components.PersonStatus;
import com.mrkotuk.PersoNet.components.PersonType;
import com.mrkotuk.PersoNet.model.Person;
import com.mrkotuk.PersoNet.model.SearchFilter;
import com.mrkotuk.PersoNet.repo.PersonSearchRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SearchService {
    private final PersonSearchRepo repo;

    public List<Person> searchPersons(String email, SearchFilter filter) {
        List<Person> persons = repo.findByFilters(email, filter.getKeyword(), filter.getStatus(), filter.getTypes());

        Iterator<Person> iterator = persons.iterator();
        while (iterator.hasNext()) {
            Person person = iterator.next();
            for (String tag : filter.getTags()) {
                if (!person.getTags().contains(tag)) {
                    iterator.remove();
                    break;
                }
            }
        }

        return persons;
    }

    public List<String> getTags(String email) {
        return repo.findTagsByEmail(email).stream()
                .flatMap(tags -> Arrays.stream(tags.split(",")))
                .distinct()
                .collect(Collectors.toList());
    }

    public List<PersonType> getTypes(String email) {
        return new ArrayList<>(repo.findTypesByEmail(email));
    }

    public List<PersonStatus> getStatuses(String email) {
        return new ArrayList<>(repo.findStatusesByEmail(email));
    }
}