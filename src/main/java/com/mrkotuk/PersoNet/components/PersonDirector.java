package com.mrkotuk.PersoNet.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.mrkotuk.PersoNet.model.LineTemplate;
import com.mrkotuk.PersoNet.model.Person;

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
                return new ArrayList<>(Arrays.asList(new Person[] {
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
                                .lineTemplates(new ArrayList<>(Arrays.asList(new LineTemplate[] {
                                                new LineTemplate("First Name", "string"),
                                                new LineTemplate("Last Name", "string")
                                })))
                                .personType(PersonType.CUSTOM)
                                .build();

                return person;
        }

        private Person createGeneralPerson() {
                return Person.builder()
                                .lineTemplates(new ArrayList<>(Arrays.asList(new LineTemplate[] {
                                                new LineTemplate("First Name", "string"),
                                                new LineTemplate("Last Name", "string"),
                                                new LineTemplate("Phone Number", "string"),
                                                new LineTemplate("Email", "string"),
                                                new LineTemplate("Birthday", "string"),
                                                new LineTemplate("Age", "int"),
                                                new LineTemplate("Address", "string"),
                                                new LineTemplate("Social Media", "string"),
                                                new LineTemplate("Notes", "string")
                                })))
                                .personType(PersonType.GENERAL)
                                .build();
        }

        private Person createFriendPerson() {
                return Person.builder()
                                .lineTemplates(new ArrayList<>(Arrays.asList(new LineTemplate[] {
                                                new LineTemplate("First Name", "string"),
                                                new LineTemplate("Last Name", "string"),
                                                new LineTemplate("Nickname", "string"),
                                                new LineTemplate("Phone Number", "string"),
                                                new LineTemplate("Email", "string"),
                                                new LineTemplate("Birthday", "string"),
                                                new LineTemplate("Age", "int"),
                                                new LineTemplate("Address", "string"),
                                                new LineTemplate("Is Best Friend", "boolean")
                                })))
                                .personType(PersonType.FRIEND)
                                .build();
        }

        private Person createColleaguePerson() {
                return Person.builder()
                                .lineTemplates(new ArrayList<>(Arrays.asList(new LineTemplate[] {
                                                new LineTemplate("First Name", "string"),
                                                new LineTemplate("Last Name", "string"),
                                                new LineTemplate("Company", "string"),
                                                new LineTemplate("Position", "string"),
                                                new LineTemplate("Work Email", "string"),
                                                new LineTemplate("Work Phone", "string"),
                                                new LineTemplate("Department", "string"),
                                                new LineTemplate("Years Worked", "int"),
                                                new LineTemplate("Is Remote Worker", "boolean")
                                })))
                                .personType(PersonType.COLLEAGUE)
                                .build();
        }

        private Person createFamilyPerson() {
                return Person.builder()
                                .lineTemplates(new ArrayList<>(Arrays.asList(new LineTemplate[] {
                                                new LineTemplate("First Name", "string"),
                                                new LineTemplate("Last Name", "string"),
                                                new LineTemplate("Relation", "string"),
                                                new LineTemplate("Phone Number", "string"),
                                                new LineTemplate("Email", "string"),
                                                new LineTemplate("Birthday", "string"),
                                                new LineTemplate("Is Emergency Contact", "boolean"),
                                                new LineTemplate("Address", "string")
                                })))
                                .personType(PersonType.FAMILY)
                                .build();
        }

        private Person createClientPerson() {
                return Person.builder()
                                .lineTemplates(new ArrayList<>(Arrays.asList(new LineTemplate[] {
                                                new LineTemplate("First Name", "string"),
                                                new LineTemplate("Last Name", "string"),
                                                new LineTemplate("Company", "string"),
                                                new LineTemplate("Purchase Count", "int"),
                                                new LineTemplate("Last Purchase Date", "string"),
                                                new LineTemplate("Preferred Contact Method", "string"),
                                                new LineTemplate("Is VIP", "boolean"),
                                                new LineTemplate("Phone Number", "string"),
                                                new LineTemplate("Email", "string")
                                })))
                                .personType(PersonType.CLIENT)
                                .build();
        }
}