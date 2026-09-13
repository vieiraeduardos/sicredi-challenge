package com.example.sicredi_challenge.controllers;

import com.example.sicredi_challenge.entities.dtos.CreateAgendaRequest;
import com.example.sicredi_challenge.entities.dtos.CreateAgendaResponse;
import com.example.sicredi_challenge.entities.dtos.UpdateAgendaRequest;
import com.example.sicredi_challenge.services.AgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/agendas/{id}")
    public ResponseEntity<CreateAgendaResponse> openAgenda(@PathVariable Long id, @RequestBody(required = false) UpdateAgendaRequest updateAgendaRequest) {
        CreateAgendaResponse response = agendaService.openAgenda(id, updateAgendaRequest);
        return ResponseEntity.ok(response);
    }
}
