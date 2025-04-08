package com.mrkotuk.PersoNet.domain.model;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class ErrorResponse {
    private LocalDateTime time;
    private int status;
    private String error;
    private String message;
    private String path;
}
