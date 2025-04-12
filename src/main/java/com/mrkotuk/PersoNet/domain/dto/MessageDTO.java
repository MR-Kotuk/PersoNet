package com.mrkotuk.PersoNet.domain.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDTO {
    private String subject;
    private String message;
    private List<Integer> recipient;
}
