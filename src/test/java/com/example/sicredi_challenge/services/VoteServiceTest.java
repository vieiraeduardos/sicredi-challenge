package com.example.sicredi_challenge.services;

import com.example.sicredi_challenge.entities.Agenda;
import com.example.sicredi_challenge.entities.dtos.VoteRequest;
import com.example.sicredi_challenge.entities.dtos.VoteResponse;
import com.example.sicredi_challenge.exceptions.BusinessException;
import com.example.sicredi_challenge.exceptions.ResourceNotFoundException;
import com.example.sicredi_challenge.repositories.AgendaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {

    @Mock
    private AgendaRepository agendaRepository;

    @Mock
    private UserInfoService userInfoService;

    @Mock
    private VotePersistenceService votePersistenceService;

    @InjectMocks
    private VoteService voteService;

    @Test
    void deveRegistrarVotoSimParaAssociadoHabilitado() {
        Agenda agenda = new Agenda("Assembleia", "Pauta anual");
        agenda.open(10);
        VoteRequest request = new VoteRequest("12345678900", "Sim");
        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));
        when(votePersistenceService.save(any(), eq(request.associateId()), any()))
            .thenReturn(new VoteResponse(1L, 1L, request.associateId(), "SIM", null));

        VoteResponse response = voteService.vote(1L, request);

        assertEquals("12345678900", response.associateId());
        assertEquals("SIM", response.vote());
        verify(userInfoService).validateAssociateCanVote(request.associateId());
        verify(votePersistenceService).save(any(), eq(request.associateId()), any());
    }

    @Test
    void deveRejeitarVotoQuandoAgendaNaoExiste() {
        when(agendaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> voteService.vote(1L, new VoteRequest("123", "Sim")));
        verifyNoInteractions(userInfoService, votePersistenceService);
    }

    @Test
    void deveRejeitarVotoQuandoAgendaEstaFechada() {
        Agenda agenda = new Agenda("Assembleia", "Pauta anual");
        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> voteService.vote(1L, new VoteRequest("123", "Sim")));

        assertEquals("A votação para esta pauta não está aberta ou já foi encerrada.", exception.getMessage());
        verifyNoInteractions(userInfoService, votePersistenceService);
    }

    @Test
    void deveRejeitarAssociadoSemIdentificador() {
        Agenda agenda = new Agenda("Assembleia", "Pauta anual");
        agenda.open(10);
        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));

        assertThrows(BusinessException.class,
                () -> voteService.vote(1L, new VoteRequest(" ", "Sim")));
        verifyNoInteractions(userInfoService);
        verifyNoInteractions(votePersistenceService);
    }

    @Test
    void deveDelegarPersistenciaParaUmaUnicaTransacao() {
        Agenda agenda = new Agenda("Assembleia", "Pauta anual");
        agenda.open(10);
        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));
        when(votePersistenceService.save(any(), eq("123"), any()))
                .thenReturn(new VoteResponse(1L, 1L, "123", "NAO", null));

        VoteResponse response = voteService.vote(1L, new VoteRequest("123", "Não"));

        assertEquals("NAO", response.vote());
        verify(userInfoService).validateAssociateCanVote("123");
        verify(votePersistenceService).save(any(), eq("123"), any());
    }

    @Test
    void deveRejeitarVotoComOpcaoInvalida() {
        Agenda agenda = new Agenda("Assembleia", "Pauta anual");
        agenda.open(10);
        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));

        assertThrows(IllegalArgumentException.class,
                () -> voteService.vote(1L, new VoteRequest("123", "Talvez")));
        verifyNoInteractions(userInfoService, votePersistenceService);
    }
}