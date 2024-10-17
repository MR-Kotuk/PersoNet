package com.mrkotuk.PersoNet.components;

import org.springframework.stereotype.Component;

import com.mrkotuk.PersoNet.model.LineTemplate;
import com.mrkotuk.PersoNet.model.Person;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class PersonBuilder implements IPersonBuilder {
    private Person person;

    @Override
    public IPersonBuilder addAttribute(String name, String type) {
        LineTemplate line = new LineTemplate();
        line.setLineName(name);
        line.setLineType(type);
        person.getLineTemplates().add(line);

        return this;
    }

    @Override
    public void reset() {
        person = new Person();
    }

    @Override
    public Person build() {
        return person;
    }
}