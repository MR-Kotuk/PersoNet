package com.mrkotuk.PersoNet.domain.dto;

import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@AllArgsConstructor
public class ErrorResponse {
    private final LocalDateTime time;
    private final int status;
    private final String error;
    private final String message;
    private final String path;
}
