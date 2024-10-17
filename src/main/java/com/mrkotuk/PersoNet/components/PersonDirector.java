package com.mrkotuk.PersoNet.components;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.mrkotuk.PersoNet.model.Person;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class PersonDirector {
    private final IPersonBuilder builder;

    public List<Person> createAll() {
        List<Person> persons = new ArrayList<>();
        persons.add(createGeneralPerson());
        persons.add(createFriendPerson());
        persons.add(createColleaguePerson());
        persons.add(createFamilyPerson());
        persons.add(createClientPerson());
        persons.add(createCustomPerson());

        return persons;
    }

    public Person createCustomPerson() {
        Person person = builder
                .build();

        person.setPersonType(PersonType.CUSTOM);
        builder.reset();
        return person;
    }

    public Person createGeneralPerson() {
        Person person = builder
                .addAttribute("First Name", "string")
                .addAttribute("Last Name", "string")
                .addAttribute("Phone Number", "string")
                .addAttribute("Email", "string")
                .addAttribute("Birthday", "string")
                .addAttribute("Age", "int")
                .addAttribute("Address", "string")
                .addAttribute("Social Media", "string")
                .addAttribute("Notes", "string")
                .build();

        person.setPersonType(PersonType.GENERAL);
        builder.reset();
        return person;
    }

    public Person createFriendPerson() {
        Person person = builder
                .addAttribute("First Name", "string")
                .addAttribute("Last Name", "string")
                .addAttribute("Nickname", "string")
                .addAttribute("Phone Number", "string")
                .addAttribute("Email", "string")
                .addAttribute("Birthday", "string")
                .addAttribute("Age", "int")
                .addAttribute("Address", "string")
                .addAttribute("Is Best Friend", "boolean")
                .build();

        person.setPersonType(PersonType.FRIEND);
        builder.reset();
        return person;
    }

    public Person createColleaguePerson() {
        Person person = builder
                .addAttribute("First Name", "string")
                .addAttribute("Last Name", "string")
                .addAttribute("Company", "string")
                .addAttribute("Position", "string")
                .addAttribute("Work Email", "string")
                .addAttribute("Work Phone", "string")
                .addAttribute("Department", "string")
                .addAttribute("Years Worked", "int")
                .addAttribute("Is Remote Worker", "boolean")
                .build();

        person.setPersonType(PersonType.COLLEAGUE);
        builder.reset();
        return person;
    }

    public Person createFamilyPerson() {
        Person person = builder
                .addAttribute("First Name", "string")
                .addAttribute("Last Name", "string")
                .addAttribute("Relation", "string")
                .addAttribute("Phone Number", "string")
                .addAttribute("Email", "string")
                .addAttribute("Birthday", "string")
                .addAttribute("Is Emergency Contact", "boolean")
                .addAttribute("Address", "string")
                .build();

        person.setPersonType(PersonType.FAMILY);
        builder.reset();
        return person;
    }

    public Person createClientPerson() {
        Person person = builder
                .addAttribute("First Name", "string")
                .addAttribute("Last Name", "string")
                .addAttribute("Company", "string")
                .addAttribute("Purchase Count", "int")
                .addAttribute("Last Purchase Date", "string")
                .addAttribute("Preferred Contact Method", "string")
                .addAttribute("Is VIP", "boolean")
                .addAttribute("Phone Number", "string")
                .addAttribute("Email", "string")
                .build();

        person.setPersonType(PersonType.CLIENT);
        builder.reset();
        return person;
    }
}
