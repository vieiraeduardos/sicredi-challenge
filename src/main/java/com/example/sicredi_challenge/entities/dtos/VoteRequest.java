package com.example.sicredi_challenge.entities.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VoteRequest(
    @JsonProperty("associate_id") String associateId,
    String vote
) { }