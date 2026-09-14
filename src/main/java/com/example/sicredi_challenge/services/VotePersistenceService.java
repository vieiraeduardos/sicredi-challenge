package com.example.sicredi_challenge.services;

import com.example.sicredi_challenge.entities.Agenda;
import com.example.sicredi_challenge.entities.Vote;
import com.example.sicredi_challenge.entities.dtos.VoteResponse;
import com.example.sicredi_challenge.entities.enums.VoteChoice;
import com.example.sicredi_challenge.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VotePersistenceService {

    @Autowired
    private VoteRepository voteRepository;

    @Transactional
    public VoteResponse save(Agenda agenda, String associateId, VoteChoice choice) {
        Vote vote = new Vote(agenda, associateId, choice);
        Vote response = voteRepository.save(vote);
        return new VoteResponse(response);
    }
}