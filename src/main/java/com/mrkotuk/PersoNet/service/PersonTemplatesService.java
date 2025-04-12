package com.mrkotuk.PersoNet.service;

import java.util.List;

import com.mrkotuk.PersoNet.exception.NotFoundException;
import org.springframework.stereotype.Service;

import com.mrkotuk.PersoNet.components.PersonDirector;
import com.mrkotuk.PersoNet.domain.enums.PersonType;
import com.mrkotuk.PersoNet.domain.model.Person;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PersonTemplatesService {
    private final PersonDirector director;

    public List<Person> getPersonTemplates() {
        return director.getAllPersons();
    }

    public Person getPersonTemplate(PersonType personType) {
        Person person = director.getPerson(personType);
        if (person == null) {
            throw new NotFoundException("Not found person template with type: " + personType);
        }

        return person;
    }
}
