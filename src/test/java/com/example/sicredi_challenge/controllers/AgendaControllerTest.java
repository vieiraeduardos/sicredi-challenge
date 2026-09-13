package com.example.sicredi_challenge.controllers;

import com.example.sicredi_challenge.entities.dtos.AgendaResultResponse;
import com.example.sicredi_challenge.entities.dtos.CreateAgendaRequest;
import com.example.sicredi_challenge.entities.dtos.CreateAgendaResponse;
import com.example.sicredi_challenge.entities.dtos.UpdateAgendaRequest;
import com.example.sicredi_challenge.entities.dtos.VoteRequest;
import com.example.sicredi_challenge.entities.dtos.VoteResponse;
import com.example.sicredi_challenge.services.AgendaService;
import com.example.sicredi_challenge.services.VoteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgendaControllerTest {

    @Mock
    private AgendaService agendaService;

    @Mock
    private VoteService voteService;

    @InjectMocks
    private AgendaController agendaController;

    @Test
    void deveCriarAgendaERetornarRespostaDoServico() {
        CreateAgendaRequest request = new CreateAgendaRequest("Assembleia", "Pauta anual");
        CreateAgendaResponse response = new CreateAgendaResponse(
                1L, "Assembleia", "Pauta anual", null, null, null, null);
        when(agendaService.createAgenda(request)).thenReturn(response);

        ResponseEntity<CreateAgendaResponse> result = agendaController.createAgenda(request);

        assertSame(response, result.getBody());
        verify(agendaService).createAgenda(request);
    }

    @Test
    void deveAbrirAgendaERetornarRespostaDoServico() {
        UpdateAgendaRequest request = new UpdateAgendaRequest(10);
        CreateAgendaResponse response = new CreateAgendaResponse(
                1L, "Assembleia", "Pauta anual", null, null, null, 10);
        when(agendaService.openAgenda(1L, request)).thenReturn(response);

        ResponseEntity<CreateAgendaResponse> result = agendaController.openAgenda(1L, request);

        assertSame(response, result.getBody());
        verify(agendaService).openAgenda(1L, request);
    }

    @Test
    void deveRegistrarVotoERetornarRespostaDoServico() {
        VoteRequest request = new VoteRequest("12345678900", "Sim");
        VoteResponse response = new VoteResponse(1L, 1L, "12345678900", "SIM", null);
        when(voteService.vote(1L, request)).thenReturn(response);

        ResponseEntity<VoteResponse> result = agendaController.vote(1L, request);

        assertSame(response, result.getBody());
        verify(voteService).vote(1L, request);
    }

    @Test
    void deveConsultarResultadoERetornarRespostaDoServico() {
        AgendaResultResponse response = new AgendaResultResponse(
                1L, "Assembleia", "EM_ANDAMENTO", 3, 2, 1, "APROVADA");
        when(agendaService.getAgendaResult(1L)).thenReturn(response);

        ResponseEntity<AgendaResultResponse> result = agendaController.getResult(1L);

        assertSame(response, result.getBody());
        verify(agendaService).getAgendaResult(1L);
    }
}
