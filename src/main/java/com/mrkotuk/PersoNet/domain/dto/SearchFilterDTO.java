package com.mrkotuk.PersoNet.domain.dto;

import java.util.HashSet;

import com.mrkotuk.PersoNet.domain.enums.PersonStatus;
import com.mrkotuk.PersoNet.domain.enums.PersonType;

import lombok.Getter;

@Getter
public class SearchFilterDTO {
    private String keyword;

    private HashSet<PersonStatus> status;
    private HashSet<PersonType> types;
    private HashSet<String> tags;
}
