package com.example.sicredi_challenge.entities.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateAgendaRequest (
        @JsonProperty("voting_period") Integer votingPeriod
){ }
