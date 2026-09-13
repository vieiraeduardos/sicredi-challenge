package com.example.sicredi_challenge.entities.dtos;

import com.example.sicredi_challenge.entities.Vote;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public record VoteResponse(
    Long id,
    @JsonProperty("agenda_id") Long agendaId,
    @JsonProperty("associate_id") String associateId,
    String vote,
    @JsonProperty("voted_at") LocalDateTime votedAt
) {
    public VoteResponse(Vote voteEntity) {
        this(
            voteEntity.getId(),
            voteEntity.getAgenda().getId(),
            voteEntity.getAssociateId(),
            voteEntity.getChoice().name(),
            voteEntity.getVotedAt()
        );
    }
}