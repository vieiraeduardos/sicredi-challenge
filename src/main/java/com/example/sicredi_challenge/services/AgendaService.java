package com.example.sicredi_challenge.services;

import com.example.sicredi_challenge.entities.Agenda;
import com.example.sicredi_challenge.entities.dtos.AgendaResultResponse;
import com.example.sicredi_challenge.entities.dtos.CreateAgendaRequest;
import com.example.sicredi_challenge.entities.dtos.CreateAgendaResponse;
import com.example.sicredi_challenge.entities.dtos.UpdateAgendaRequest;
import com.example.sicredi_challenge.entities.enums.VoteChoice;
import com.example.sicredi_challenge.exceptions.ResourceNotFoundException;
import com.example.sicredi_challenge.repositories.AgendaRepository;
import com.example.sicredi_challenge.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgendaService {
    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private VoteRepository voteRepository;

    public CreateAgendaResponse createAgenda(CreateAgendaRequest createAgendaRequest) {
        Agenda agenda = new Agenda(
                createAgendaRequest.title(),
                createAgendaRequest.description()
        );

        Agenda response = agendaRepository.save(agenda);
        return new CreateAgendaResponse(response);
    }

    public CreateAgendaResponse openAgenda(Long id, UpdateAgendaRequest updateAgendaRequest) {
        Agenda agenda = agendaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Agenda não encontrada."));
        Integer period = (updateAgendaRequest != null) ? updateAgendaRequest.votingPeriod() : null;
        agenda.open(period);
        Agenda response = agendaRepository.save(agenda);
        return new CreateAgendaResponse(response);
    }

    public AgendaResultResponse getAgendaResult(Long id) {
        Agenda agenda = agendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pauta não encontrada."));

        long totalYes = voteRepository.countByAgendaIdAndChoice(id, VoteChoice.SIM);
        long totalNo = voteRepository.countByAgendaIdAndChoice(id, VoteChoice.NAO);
        long totalVotes = totalYes + totalNo;

        String status;
        if (agenda.getOpenedAt() == null) {
            status = "NAO_ABERTA";
        } else if (agenda.isOpen()) {
            status = "EM_ANDAMENTO";
        } else {
            status = "FINALIZADA";
        }

        String result;
        if (totalVotes == 0) {
            result = "SEM_VOTOS";
        } else if (totalYes > totalNo) {
            result = "APROVADA";
        } else if (totalNo > totalYes) {
            result = "REPROVADA";
        } else {
            result = "EMPATE";
        }

        return new AgendaResultResponse(
                agenda.getId(),
                agenda.getTitle(),
                status,
                totalVotes,
                totalYes,
                totalNo,
                result
        );
    }
}
