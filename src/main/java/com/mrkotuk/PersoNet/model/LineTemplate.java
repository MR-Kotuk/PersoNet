package com.mrkotuk.PersoNet.model;

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
public class LineTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int orderId;

    private String lineName;
    private String lineType;
    private String lineValue;

    public LineTemplate(String lineName, String lineType) {
        this.lineName = lineName;
        this.lineType = lineType;
    }
}