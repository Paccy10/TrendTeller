package com.paccy.TrendTeller.dto;

import lombok.Getter;

import java.util.Date;

@Getter
public class ErrorDetailsDTO {
    private final Date timestamp;
    private final String message;
    private final String details;

    public ErrorDetailsDTO(Date timestamp, String message, String details) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }
}
