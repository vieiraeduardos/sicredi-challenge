package com.example.sicredi_challenge.controllers;

import com.example.sicredi_challenge.entities.dtos.ScreenResponse;
import com.example.sicredi_challenge.entities.dtos.ScreenResponse.Botao;
import com.example.sicredi_challenge.entities.dtos.ScreenResponse.FormItem;
import com.example.sicredi_challenge.entities.dtos.ScreenResponse.SelecaoItem;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/screens")
public class ScreenController {

    @Value("${app.host}")
    private String host;

    @GetMapping("/create-agenda")
    public ResponseEntity<ScreenResponse> getCreateAgendaScreen() {
        List<FormItem> itens = List.of(
                FormItem.texto("Preencha os dados abaixo para cadastrar uma nova pauta para votação."),
                FormItem.inputTexto("title", "Título da pauta", ""),
                FormItem.inputTexto("description", "Descrição da pauta", "")
        );

        Botao botaoOk = new Botao("Criar Pauta", host + "/api/agendas", Map.of(
                "title", "",
                "description", ""
        ));

        Botao botaoCancelar = new Botao("Cancelar", host);

        ScreenResponse response = ScreenResponse.formulario(
                "Criação de Pauta",
                itens,
                botaoOk,
                botaoCancelar
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/agendas/{agendaId}/open-agenda")
    public ResponseEntity<ScreenResponse> getOpenAgendaScreen(@PathVariable Long agendaId) {
        List<FormItem> itens = List.of(
                FormItem.texto("Defina o tempo de votação para abrir esta pauta."),
                FormItem.inputNumero("voting_period", "Tempo de votação (minutos)", 1)
        );

        String url = host + "/api/agendas/" + agendaId;
        Botao botaoOk = new Botao("Abrir Pauta", url, Map.of("voting_period", 1));
        Botao botaoCancelar = new Botao("Cancelar", host);

        ScreenResponse response = ScreenResponse.formulario(
                "Abertura de Pauta",
                itens,
                botaoOk,
                botaoCancelar
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/agendas/{agendaId}/submit-vote")
    public ResponseEntity<ScreenResponse> getVoteScreenForAgenda(@PathVariable Long agendaId) {
        return getVoteSelectionScreenForAgenda(agendaId);
    }

    @GetMapping("/agendas/{agendaId}/select-vote")
    public ResponseEntity<ScreenResponse> getVoteSelectionScreenForAgenda(@PathVariable Long agendaId) {
        String url = host + "/api/agendas/" + agendaId + "/votes";

        List<SelecaoItem> itens = List.of(
                new SelecaoItem("Opção Sim", url, Map.of("vote", "Sim", "associate_id", "")),
                new SelecaoItem("Opção Não", url, Map.of("vote", "Não", "associate_id", ""))
        );

        ScreenResponse response = ScreenResponse.selecao("Selecione seu Voto", itens);

        return ResponseEntity.ok(response);
    }
}
