package com.mrkotuk.PersoNet.domain.dto;

import java.util.HashSet;

import com.mrkotuk.PersoNet.domain.enums.PersonStatus;
import com.mrkotuk.PersoNet.domain.enums.PersonType;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchFilterDTO {
    private String keyword;

    private HashSet<PersonStatus> status;
    private HashSet<PersonType> types;
    private HashSet<String> tags;
}
