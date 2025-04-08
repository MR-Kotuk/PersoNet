package com.mrkotuk.PersoNet.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mrkotuk.PersoNet.domain.enums.PersonStatus;
import com.mrkotuk.PersoNet.domain.enums.PersonType;
import com.mrkotuk.PersoNet.domain.model.Person;
import com.mrkotuk.PersoNet.domain.model.SearchFilter;
import com.mrkotuk.PersoNet.repository.PersonSearchRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SearchService {
    private final PersonSearchRepository repository;

    public List<Person> searchPersons(String email, SearchFilter filter) {
        List<Person> persons = repository.findByFilters(email, filter.getKeyword(), filter.getStatus(), filter.getTypes());

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
        return repository.findTagsByEmail(email).stream()
                .flatMap(tags -> Arrays.stream(tags.split(",")))
                .distinct()
                .collect(Collectors.toList());
    }

    public List<PersonType> getTypes(String email) {
        return new ArrayList<>(repository.findTypesByEmail(email));
    }

    public List<PersonStatus> getStatuses(String email) {
        return new ArrayList<>(repository.findStatusesByEmail(email));
    }
}
