package com.example.sicredi_challenge.controllers;

import com.example.sicredi_challenge.entities.dtos.ScreenResponse;
import com.example.sicredi_challenge.entities.dtos.ScreenResponse.Botao;
import com.example.sicredi_challenge.entities.dtos.ScreenResponse.FormItem;
import com.example.sicredi_challenge.entities.dtos.ScreenResponse.SelecaoItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ScreenControllerTest {

    private static final String HOST = "http://localhost:8080";

    private ScreenController screenController;

    @BeforeEach
    void setUp() {
        screenController = new ScreenController();
        ReflectionTestUtils.setField(screenController, "host", HOST);
    }

    @Test
    void deveRetornarFormularioDeCriacaoComUrlCompleta() {
        ScreenResponse response = screenController.getCreateAgendaScreen().getBody();

        assertNotNull(response);
        assertEquals("FORMULARIO", response.tipo());
        assertEquals("Criação de Pauta", response.titulo());
        assertEquals(3, response.itens().size());

        Botao botaoOk = response.botaoOk();
        assertEquals("Criar Pauta", botaoOk.texto());
        assertEquals(HOST + "/api/agendas", botaoOk.url());
        assertEquals(Map.of("title", "", "description", ""), botaoOk.body());
        assertEquals(HOST, response.botaoCancelar().url());
    }

    @Test
    void deveRetornarFormularioDeAberturaComPeriodoDeVotacao() {
        ScreenResponse response = screenController.getOpenAgendaScreen(7L).getBody();

        assertNotNull(response);
        assertEquals("FORMULARIO", response.tipo());
        assertEquals("Abertura de Pauta", response.titulo());
        assertEquals(2, response.itens().size());

        FormItem periodo = (FormItem) response.itens().get(1);
        assertEquals("voting_period", periodo.id());
        assertEquals(1, periodo.valor());
        assertEquals(HOST + "/api/agendas/7", response.botaoOk().url());
        assertEquals(Map.of("voting_period", 1), response.botaoOk().body());
    }

    @Test
    void deveRetornarTelaDeVotacaoComOpcoesSimENao() {
        ScreenResponse response = screenController.getVoteScreenForAgenda(7L).getBody();

        assertNotNull(response);
        assertEquals("SELECAO", response.tipo());
        assertEquals("Selecione seu Voto", response.titulo());
        assertEquals(2, response.itens().size());

        SelecaoItem sim = (SelecaoItem) response.itens().get(0);
        SelecaoItem nao = (SelecaoItem) response.itens().get(1);
        assertEquals("Opção Sim", sim.texto());
        assertEquals("Opção Não", nao.texto());
        assertEquals(HOST + "/api/agendas/7/votes", sim.url());
        assertEquals(sim.url(), nao.url());
        assertEquals(Map.of("vote", "Sim", "associate_id", ""), sim.body());
        assertEquals(Map.of("vote", "Não", "associate_id", ""), nao.body());
    }

    @Test
    void deveRetornarAsMesmasOpcoesNaTelaDeSelecao() {
        ScreenResponse response = screenController.getVoteSelectionScreenForAgenda(3L).getBody();

        assertNotNull(response);
        assertEquals("SELECAO", response.tipo());
        assertEquals(HOST + "/api/agendas/3/votes", ((SelecaoItem) response.itens().get(0)).url());
        assertEquals("Sim", ((Map<?, ?>) ((SelecaoItem) response.itens().get(0)).body()).get("vote"));
        assertEquals("Não", ((Map<?, ?>) ((SelecaoItem) response.itens().get(1)).body()).get("vote"));
    }
}
