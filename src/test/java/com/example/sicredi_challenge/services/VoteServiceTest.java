package com.example.sicredi_challenge.services;

import com.example.sicredi_challenge.entities.Agenda;
import com.example.sicredi_challenge.entities.Vote;
import com.example.sicredi_challenge.entities.dtos.VoteRequest;
import com.example.sicredi_challenge.entities.dtos.VoteResponse;
import com.example.sicredi_challenge.entities.enums.VoteChoice;
import com.example.sicredi_challenge.exceptions.BusinessException;
import com.example.sicredi_challenge.exceptions.ResourceNotFoundException;
import com.example.sicredi_challenge.repositories.AgendaRepository;
import com.example.sicredi_challenge.repositories.VoteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private AgendaRepository agendaRepository;

    @Mock
    private UserInfoService userInfoService;

    @InjectMocks
    private VoteService voteService;

    @Test
    void deveRegistrarVotoSimParaAssociadoHabilitado() {
        Agenda agenda = new Agenda("Assembleia", "Pauta anual");
        agenda.open(10);
        VoteRequest request = new VoteRequest("12345678900", "Sim");
        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));
        when(voteRepository.saveAndFlush(any(Vote.class))).thenAnswer(invocation -> invocation.getArgument(0));

        VoteResponse response = voteService.vote(1L, request);

        assertEquals("12345678900", response.associateId());
        assertEquals("SIM", response.vote());
        verify(userInfoService).validateAssociateCanVote(request.associateId());
        verify(voteRepository).saveAndFlush(any(Vote.class));
    }

    @Test
    void deveRejeitarVotoQuandoAgendaNaoExiste() {
        when(agendaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> voteService.vote(1L, new VoteRequest("123", "Sim")));
        verifyNoInteractions(userInfoService, voteRepository);
    }

    @Test
    void deveRejeitarVotoQuandoAgendaEstaFechada() {
        Agenda agenda = new Agenda("Assembleia", "Pauta anual");
        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> voteService.vote(1L, new VoteRequest("123", "Sim")));

        assertEquals("A votação para esta pauta não está aberta ou já foi encerrada.", exception.getMessage());
        verifyNoInteractions(userInfoService, voteRepository);
    }

    @Test
    void deveRejeitarAssociadoSemIdentificador() {
        Agenda agenda = new Agenda("Assembleia", "Pauta anual");
        agenda.open(10);
        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));

        assertThrows(BusinessException.class,
                () -> voteService.vote(1L, new VoteRequest(" ", "Sim")));
        verifyNoInteractions(userInfoService);
        verify(voteRepository, never()).save(any());
    }

    @Test
    void deveRejeitarVotoDuplicado() {
        Agenda agenda = new Agenda("Assembleia", "Pauta anual");
        agenda.open(10);
        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));
        when(voteRepository.saveAndFlush(any(Vote.class)))
            .thenThrow(new DataIntegrityViolationException("violação da constraint de voto único"));

        BusinessException exception = assertThrows(BusinessException.class,
            () -> voteService.vote(1L, new VoteRequest("123", "Não")));

        assertEquals("Associado já votou nesta pauta.", exception.getMessage());
        verify(userInfoService).validateAssociateCanVote("123");
        verify(voteRepository).saveAndFlush(any(Vote.class));
    }

    @Test
    void deveRejeitarVotoComOpcaoInvalida() {
        Agenda agenda = new Agenda("Assembleia", "Pauta anual");
        agenda.open(10);
        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));

        assertThrows(IllegalArgumentException.class,
                () -> voteService.vote(1L, new VoteRequest("123", "Talvez")));
        verifyNoInteractions(userInfoService, voteRepository);
    }
}