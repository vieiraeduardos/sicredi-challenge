package com.example.sicredi_challenge.entities.enums;

public enum VoteChoice {
    SIM,
    NAO;

    public static VoteChoice fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("O voto não pode ser nulo ou vazio.");
        }
        String normalized = value.trim().toUpperCase();
        if ("SIM".equals(normalized) || "S".equals(normalized)) {
            return SIM;
        }
        if ("NÃO".equals(normalized) || "NAO".equals(normalized) || "N".equals(normalized)) {
            return NAO;
        }
        throw new IllegalArgumentException("Voto inválido. Escolha 'Sim' ou 'Não'.");
    }
}