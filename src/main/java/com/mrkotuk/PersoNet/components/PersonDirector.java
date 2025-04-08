package com.mrkotuk.PersoNet.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mrkotuk.PersoNet.domain.enums.PersonType;
import org.springframework.stereotype.Component;

import com.mrkotuk.PersoNet.domain.model.LineTemplate;
import com.mrkotuk.PersoNet.domain.model.Person;

import io.jsonwebtoken.lang.Arrays;

@Component
public class PersonDirector {
    private final Map<PersonType, Person> persons = new HashMap<>();

    public PersonDirector() {
        for (Person person : createAll())
            persons.put(person.getPersonType(), person);
    }

    public Person getPerson(PersonType type) {
        return persons.get(type);
    }

    public List<Person> getAllPersons() {
        return new ArrayList<>(persons.values());
    }

    private List<Person> createAll() {
        return new ArrayList<>(Arrays.asList(
                new Person[] {
                        createGeneralPerson(),
                        createFriendPerson(),
                        createColleaguePerson(),
                        createFamilyPerson(),
                        createClientPerson(),
                        createCustomPerson()
                }));
    }

    private Person createCustomPerson() {
        Person person = Person.builder()
                .lineTemplates(new ArrayList<>(Arrays.asList(
                        new LineTemplate[] {
                                new LineTemplate("First Name"),
                                new LineTemplate("Last Name")
                        })))
                .personType(PersonType.CUSTOM)
                .build();

        return person;
    }

    private Person createGeneralPerson() {
        return Person.builder()
                .lineTemplates(new ArrayList<>(Arrays.asList(
                        new LineTemplate[] {
                                new LineTemplate("First Name"),
                                new LineTemplate("Last Name"),
                                new LineTemplate("Phone Number"),
                                new LineTemplate("Email"),
                                new LineTemplate("Birthday"),
                                new LineTemplate("Age"),
                                new LineTemplate("Address"),
                                new LineTemplate("Social Media"),
                                new LineTemplate("Notes")
                        })))
                .personType(PersonType.GENERAL)
                .build();
    }

    private Person createFriendPerson() {
        return Person.builder()
                .lineTemplates(new ArrayList<>(Arrays.asList(
                        new LineTemplate[] {
                                new LineTemplate("First Name"),
                                new LineTemplate("Last Name"),
                                new LineTemplate("Nickname"),
                                new LineTemplate("Phone Number"),
                                new LineTemplate("Email"),
                                new LineTemplate("Birthday"),
                                new LineTemplate("Age"),
                                new LineTemplate("Address"),
                                new LineTemplate("Is Best Friend")
                        })))
                .personType(PersonType.FRIEND)
                .build();
    }

    private Person createColleaguePerson() {
        return Person.builder()
                .lineTemplates(new ArrayList<>(Arrays.asList(
                        new LineTemplate[] {
                                new LineTemplate("First Name"),
                                new LineTemplate("Last Name"),
                                new LineTemplate("Company"),
                                new LineTemplate("Position"),
                                new LineTemplate("Work Email"),
                                new LineTemplate("Work Phone"),
                                new LineTemplate("Department"),
                                new LineTemplate("Years Worked"),
                                new LineTemplate("Is Remote Worker")
                        })))
                .personType(PersonType.COLLEAGUE)
                .build();
    }

    private Person createFamilyPerson() {
        return Person.builder()
                .lineTemplates(new ArrayList<>(Arrays.asList(
                        new LineTemplate[] {
                                new LineTemplate("First Name"),
                                new LineTemplate("Last Name"),
                                new LineTemplate("Relation"),
                                new LineTemplate("Phone Number"),
                                new LineTemplate("Email"),
                                new LineTemplate("Birthday"),
                                new LineTemplate("Is Emergency Contact"),
                                new LineTemplate("Address")
                        })))
                .personType(PersonType.FAMILY)
                .build();
    }

    private Person createClientPerson() {
        return Person.builder()
                .lineTemplates(new ArrayList<>(Arrays.asList(
                        new LineTemplate[] {
                                new LineTemplate("First Name"),
                                new LineTemplate("Last Name"),
                                new LineTemplate("Company"),
                                new LineTemplate("Purchase Count"),
                                new LineTemplate("Last Purchase Date"),
                                new LineTemplate("Preferred Contact Method"),
                                new LineTemplate("Is VIP"),
                                new LineTemplate("Phone Number"),
                                new LineTemplate("Email")
                        })))
                .personType(PersonType.CLIENT)
                .build();
    }
}
