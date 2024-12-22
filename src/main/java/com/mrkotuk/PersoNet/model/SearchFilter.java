package com.mrkotuk.PersoNet.model;

import java.util.HashSet;

import com.mrkotuk.PersoNet.components.PersonStatus;
import com.mrkotuk.PersoNet.components.PersonType;

import lombok.Getter;

@Getter
public class SearchFilter {
    private String keyword;

    private HashSet<PersonStatus> status;
    private HashSet<PersonType> types;
    private HashSet<String> tags;
}