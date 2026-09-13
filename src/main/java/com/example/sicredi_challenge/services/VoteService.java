package com.example.sicredi_challenge.services;

import com.example.sicredi_challenge.entities.Agenda;
import com.example.sicredi_challenge.entities.Vote;
import com.example.sicredi_challenge.entities.dtos.VoteRequest;
import com.example.sicredi_challenge.entities.dtos.VoteResponse;
import com.example.sicredi_challenge.entities.enums.VoteChoice;
import com.example.sicredi_challenge.repositories.AgendaRepository;
import com.example.sicredi_challenge.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteService {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private AgendaRepository agendaRepository;

    public VoteResponse vote(Long agendaId, VoteRequest voteRequest) {
        Agenda agenda = agendaRepository.findById(agendaId)
                .orElseThrow(() -> new RuntimeException("Pauta não encontrada."));

        if (!agenda.isOpen()) {
            throw new RuntimeException("A votação para esta pauta não está aberta ou já foi encerrada.");
        }

        if (voteRequest == null || voteRequest.associateId() == null || voteRequest.associateId().isBlank()) {
            throw new RuntimeException("O ID do associado é obrigatório.");
        }

        if (voteRepository.existsByAgendaIdAndAssociateId(agendaId, voteRequest.associateId())) {
            throw new RuntimeException("Associado já votou nesta pauta.");
        }

        VoteChoice choice = VoteChoice.fromString(voteRequest.vote());

        Vote vote = new Vote(agenda, voteRequest.associateId(), choice);
        Vote response = voteRepository.save(vote);

        return new VoteResponse(response);
    }
}