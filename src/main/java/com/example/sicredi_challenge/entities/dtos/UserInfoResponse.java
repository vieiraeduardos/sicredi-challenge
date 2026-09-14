package com.example.sicredi_challenge.entities.dtos;

public record UserInfoResponse(String status) {
    public boolean isAbleToVote() {
        return "ABLE_TO_VOTE".equalsIgnoreCase(status);
    }
}