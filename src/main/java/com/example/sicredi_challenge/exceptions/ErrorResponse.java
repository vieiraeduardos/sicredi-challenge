package com.example.sicredi_challenge.exceptions;

public record ErrorResponse(String code, String message) {
    public ErrorResponse(int code, String message) {
        this(String.valueOf(code), message);
    }
}