package com.mrkotuk.PersoNet.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mrkotuk.PersoNet.components.PersonDirector;
import com.mrkotuk.PersoNet.components.PersonType;
import com.mrkotuk.PersoNet.domain.model.Person;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PersonTemplatesService {
    private final PersonDirector director;

    public List<Person> getPersonTemplates() {
        return director.getAllPersons();
    }

    public Person getPersonTemplate(String personType) {
        return director.getPerson(PersonType.valueOf(personType.toUpperCase()));
    }
}