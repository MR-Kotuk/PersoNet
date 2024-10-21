package com.mrkotuk.PersoNet.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.mrkotuk.PersoNet.model.LineTemplate;
import com.mrkotuk.PersoNet.model.Person;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class PersonDirector {
    private Map<PersonType, Person> persons = new HashMap<>();

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
        List<Person> persons = new ArrayList<>();
        persons.add(createGeneralPerson());
        persons.add(createFriendPerson());
        persons.add(createColleaguePerson());
        persons.add(createFamilyPerson());
        persons.add(createClientPerson());
        persons.add(createCustomPerson());

        return persons;
    }

    private Person createCustomPerson() {
        Person person = Person.builder()
                .personType(PersonType.CUSTOM)
                .build();

        return person;
    }

    private Person createGeneralPerson() {
        List<LineTemplate> lines = new ArrayList<>();
        lines.add(new LineTemplate("First Name", "string"));
        lines.add(new LineTemplate("Last Name", "string"));
        lines.add(new LineTemplate("Phone Number", "string"));
        lines.add(new LineTemplate("Email", "string"));
        lines.add(new LineTemplate("Birthday", "string"));
        lines.add(new LineTemplate("Age", "int"));
        lines.add(new LineTemplate("Address", "string"));
        lines.add(new LineTemplate("Social Media", "string"));
        lines.add(new LineTemplate("Notes", "string"));

        Person person = Person.builder()
                .lineTemplates(lines)
                .personType(PersonType.GENERAL)
                .build();

        return person;
    }

    private Person createFriendPerson() {
        List<LineTemplate> lines = new ArrayList<>();
        lines.add(new LineTemplate("First Name", "string"));
        lines.add(new LineTemplate("Last Name", "string"));
        lines.add(new LineTemplate("Nickname", "string"));
        lines.add(new LineTemplate("Phone Number", "string"));
        lines.add(new LineTemplate("Email", "string"));
        lines.add(new LineTemplate("Birthday", "string"));
        lines.add(new LineTemplate("Age", "int"));
        lines.add(new LineTemplate("Address", "string"));
        lines.add(new LineTemplate("Is Best Friend", "boolean"));

        Person person = Person.builder()
                .lineTemplates(lines)
                .personType(PersonType.FRIEND)
                .build();

        return person;
    }

    private Person createColleaguePerson() {
        List<LineTemplate> lines = new ArrayList<>();
        lines.add(new LineTemplate("First Name", "string"));
        lines.add(new LineTemplate("Last Name", "string"));
        lines.add(new LineTemplate("Company", "string"));
        lines.add(new LineTemplate("Position", "string"));
        lines.add(new LineTemplate("Work Email", "string"));
        lines.add(new LineTemplate("Work Phone", "string"));
        lines.add(new LineTemplate("Department", "string"));
        lines.add(new LineTemplate("Years Worked", "int"));
        lines.add(new LineTemplate("Is Remote Worker", "boolean"));

        Person person = Person.builder()
                .lineTemplates(lines)
                .personType(PersonType.COLLEAGUE)
                .build();

        return person;
    }

    private Person createFamilyPerson() {
        List<LineTemplate> lines = new ArrayList<>();
        lines.add(new LineTemplate("First Name", "string"));
        lines.add(new LineTemplate("Last Name", "string"));
        lines.add(new LineTemplate("Relation", "string"));
        lines.add(new LineTemplate("Phone Number", "string"));
        lines.add(new LineTemplate("Email", "string"));
        lines.add(new LineTemplate("Birthday", "string"));
        lines.add(new LineTemplate("Is Emergency Contact", "boolean"));
        lines.add(new LineTemplate("Address", "string"));

        Person person = Person.builder()
                .lineTemplates(lines)
                .personType(PersonType.FAMILY)
                .build();

        return person;
    }

    private Person createClientPerson() {
        List<LineTemplate> lines = new ArrayList<>();
        lines.add(new LineTemplate("First Name", "string"));
        lines.add(new LineTemplate("Last Name", "string"));
        lines.add(new LineTemplate("Company", "string"));
        lines.add(new LineTemplate("Purchase Count", "int"));
        lines.add(new LineTemplate("Last Purchase Date", "string"));
        lines.add(new LineTemplate("Preferred Contact Method", "string"));
        lines.add(new LineTemplate("Is VIP", "boolean"));
        lines.add(new LineTemplate("Phone Number", "string"));
        lines.add(new LineTemplate("Email", "string"));

        Person person = Person.builder()
                .lineTemplates(lines)
                .personType(PersonType.CLIENT)
                .build();

        return person;
    }
}
