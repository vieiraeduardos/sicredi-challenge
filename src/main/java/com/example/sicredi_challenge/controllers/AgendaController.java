package com.example.sicredi_challenge.controllers;

import com.example.sicredi_challenge.entities.dtos.CreateAgendaRequest;
import com.example.sicredi_challenge.entities.dtos.CreateAgendaResponse;
import com.example.sicredi_challenge.services.AgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AgendaController {
    @Autowired
    private AgendaService agendaService;

    @PostMapping("/agendas")
    public ResponseEntity<CreateAgendaResponse> createAgenda(@RequestBody CreateAgendaRequest createAgendaRequest) {
        CreateAgendaResponse response = agendaService.createAgenda(createAgendaRequest);

        return ResponseEntity.ok(response);
    }
}
