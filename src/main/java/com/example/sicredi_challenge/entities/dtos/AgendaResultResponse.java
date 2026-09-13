package com.example.sicredi_challenge.entities.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AgendaResultResponse(
    @JsonProperty("agenda_id") Long agendaId,
    String title,
    String status,
    @JsonProperty("total_votes") long totalVotes,
    @JsonProperty("total_yes") long totalYes,
    @JsonProperty("total_no") long totalNo,
    String result
) { }