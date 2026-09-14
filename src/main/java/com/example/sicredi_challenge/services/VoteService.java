package com.example.sicredi_challenge.services;

import com.example.sicredi_challenge.entities.Agenda;
import com.example.sicredi_challenge.entities.dtos.VoteRequest;
import com.example.sicredi_challenge.entities.dtos.VoteResponse;
import com.example.sicredi_challenge.entities.enums.VoteChoice;
import com.example.sicredi_challenge.exceptions.BusinessException;
import com.example.sicredi_challenge.exceptions.ResourceNotFoundException;
import com.example.sicredi_challenge.repositories.AgendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteService {

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private VotePersistenceService votePersistenceService;

    public VoteResponse vote(Long agendaId, VoteRequest voteRequest) {
        if (voteRequest == null || voteRequest.associateId() == null || voteRequest.associateId().isBlank()) {
            throw new BusinessException("Os campos 'associate_id' e 'vote' são obrigatórios.");
        }

        VoteChoice choice = VoteChoice.fromString(voteRequest.vote());

        Agenda agenda = agendaRepository.findById(agendaId)
                .orElseThrow(() -> new ResourceNotFoundException("Pauta não encontrada."));

        if (!agenda.isOpen()) {
            throw new BusinessException("A votação para esta pauta não está aberta ou já foi encerrada.");
        }

        userInfoService.validateAssociateCanVote(voteRequest.associateId());

        return votePersistenceService.save(agenda, voteRequest.associateId(), choice);
    }
}