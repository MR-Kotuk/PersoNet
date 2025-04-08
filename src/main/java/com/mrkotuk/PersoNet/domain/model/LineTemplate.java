package com.mrkotuk.PersoNet.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LineTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String lineName;
    private String lineValue = "";

    public LineTemplate(String lineName) {
        this.lineName = lineName;
    }

    public LineTemplate(String lineName, String lineValue) {
        this.lineName = lineName;
        this.lineValue = lineValue;
    }
}
