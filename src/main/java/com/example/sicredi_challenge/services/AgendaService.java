package com.example.sicredi_challenge.services;

import com.example.sicredi_challenge.entities.Agenda;
import com.example.sicredi_challenge.entities.dtos.CreateAgendaRequest;
import com.example.sicredi_challenge.entities.dtos.CreateAgendaResponse;
import com.example.sicredi_challenge.repositories.AgendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgendaService {
    @Autowired
    private AgendaRepository agendaRepository;

    public CreateAgendaResponse createAgenda(CreateAgendaRequest createAgendaRequest) {
        Agenda agenda = new Agenda(
                createAgendaRequest.title(),
                createAgendaRequest.description()
        );

        Agenda response = agendaRepository.save(agenda);
        return new CreateAgendaResponse(response);
    }
}
