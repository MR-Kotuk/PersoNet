package com.mrkotuk.PersoNet.components;

import com.mrkotuk.PersoNet.model.Person;

public interface IPersonBuilder {
    public IPersonBuilder addAttribute(String name, String type);

    public void reset();

    public Person build();
}