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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendaServiceTest {

    @Mock
    private AgendaRepository agendaRepository;

    @Mock
    private VoteRepository voteRepository;

    @InjectMocks
    private AgendaService agendaService;

    @Test
    void deveCriarAgendaComTituloEDescricao() {
        CreateAgendaRequest request = new CreateAgendaRequest("Assembleia", "Pauta anual");
        when(agendaRepository.save(any(Agenda.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CreateAgendaResponse response = agendaService.createAgenda(request);

        assertEquals("Assembleia", response.title());
        assertEquals("Pauta anual", response.description());
        assertNull(response.openedAt());
        assertNull(response.expiredAt());
        verify(agendaRepository).save(any(Agenda.class));
    }

    @Test
    void deveAbrirAgendaComPeriodoInformado() {
        Agenda agenda = new Agenda("Assembleia", "Pauta anual");
        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));
        when(agendaRepository.save(agenda)).thenReturn(agenda);

        CreateAgendaResponse response = agendaService.openAgenda(1L, new UpdateAgendaRequest(15));

        assertNotNull(response.openedAt());
        assertNotNull(response.expiredAt());
        assertEquals(15, response.votingPeriod());
        assertTrue(agenda.isOpen());
        verify(agendaRepository).save(agenda);
    }

    @Test
    void deveUsarUmMinutoQuandoPeriodoNaoForInformado() {
        Agenda agenda = new Agenda("Assembleia", "Pauta anual");
        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));
        when(agendaRepository.save(agenda)).thenReturn(agenda);

        agendaService.openAgenda(1L, null);

        assertEquals(1, agenda.getVotingPeriod());
        assertNotNull(agenda.getExpiredAt());
    }

    @Test
    void deveLancarExcecaoAoAbrirAgendaInexistente() {
        when(agendaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> agendaService.openAgenda(99L, new UpdateAgendaRequest(10)));
        verify(agendaRepository, never()).save(any());
    }

    @Test
    void deveRetornarResultadoAprovado() {
        Agenda agenda = new Agenda("Assembleia", "Pauta anual");
        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));
        when(voteRepository.countByAgendaIdAndChoice(1L, VoteChoice.SIM)).thenReturn(3L);
        when(voteRepository.countByAgendaIdAndChoice(1L, VoteChoice.NAO)).thenReturn(1L);

        AgendaResultResponse response = agendaService.getAgendaResult(1L);

        assertEquals("NAO_ABERTA", response.status());
        assertEquals(4, response.totalVotes());
        assertEquals(3, response.totalYes());
        assertEquals(1, response.totalNo());
        assertEquals("APROVADA", response.result());
    }

    @Test
    void deveRetornarResultadoEmpatadoSemVotosEFinalizado() {
        Agenda semVotos = new Agenda("Sem votos", "");
        when(agendaRepository.findById(1L)).thenReturn(Optional.of(semVotos));
        when(voteRepository.countByAgendaIdAndChoice(1L, VoteChoice.SIM)).thenReturn(0L);
        when(voteRepository.countByAgendaIdAndChoice(1L, VoteChoice.NAO)).thenReturn(0L);

        assertEquals("SEM_VOTOS", agendaService.getAgendaResult(1L).result());

        Agenda finalizada = new Agenda("Finalizada", "");
        finalizada.open(1);
        finalizada.setExpiredAt(LocalDateTime.now().minusMinutes(1));
        when(agendaRepository.findById(2L)).thenReturn(Optional.of(finalizada));
        when(voteRepository.countByAgendaIdAndChoice(2L, VoteChoice.SIM)).thenReturn(2L);
        when(voteRepository.countByAgendaIdAndChoice(2L, VoteChoice.NAO)).thenReturn(2L);

        AgendaResultResponse response = agendaService.getAgendaResult(2L);

        assertEquals("FINALIZADA", response.status());
        assertEquals("EMPATE", response.result());
    }
}