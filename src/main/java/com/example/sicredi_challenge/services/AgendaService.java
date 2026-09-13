package com.example.sicredi_challenge.services;

import com.example.sicredi_challenge.entities.Agenda;
import com.example.sicredi_challenge.entities.dtos.CreateAgendaRequest;
import com.example.sicredi_challenge.entities.dtos.CreateAgendaResponse;
import com.example.sicredi_challenge.entities.dtos.UpdateAgendaRequest;
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
        Agenda agenda = agendaRepository.findById(id).orElseThrow(() -> new RuntimeException("Agenda não encontrada."));
        Integer period = (updateAgendaRequest != null) ? updateAgendaRequest.votingPeriod() : null;
        agenda.open(period);
        Agenda response = agendaRepository.save(agenda);
        return new CreateAgendaResponse(response);
    }
}
