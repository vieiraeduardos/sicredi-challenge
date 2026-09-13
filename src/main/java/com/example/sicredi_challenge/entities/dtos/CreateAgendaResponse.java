package com.example.sicredi_challenge.entities.dtos;

import com.example.sicredi_challenge.entities.Agenda;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public record CreateAgendaResponse(
    Long id,
    String title,
    String description,
    @JsonProperty("created_at") LocalDateTime createdAt,
    @JsonProperty("opened_at") LocalDateTime openedAt,
    @JsonProperty("expired_at") LocalDateTime expiredAt,
    @JsonProperty("voting_period") Integer votingPeriod
) {
    public CreateAgendaResponse(Agenda agenda) {
        this(
            agenda.getId(),
            agenda.getTitle(),
            agenda.getDescription(),
            agenda.getCreatedAt(),
            agenda.getOpenedAt(),
            agenda.getExpiredAt(),
            agenda.getVotingPeriod()
        );
    }
}
