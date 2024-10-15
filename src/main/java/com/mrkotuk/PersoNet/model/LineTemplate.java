package com.mrkotuk.PersoNet.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LineTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String lineName;

    private Integer intLine;
    private String stringLine;
    private Boolean boolLine;
    private Date dateLine;
}