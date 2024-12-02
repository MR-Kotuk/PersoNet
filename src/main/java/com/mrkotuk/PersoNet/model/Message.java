package com.mrkotuk.PersoNet.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {
    private String subject;
    private String message;
    private List<Integer> recipient;
}