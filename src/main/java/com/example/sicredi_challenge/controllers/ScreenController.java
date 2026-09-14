package com.example.sicredi_challenge.controllers;

import com.example.sicredi_challenge.entities.dtos.ScreenResponse;
import com.example.sicredi_challenge.entities.dtos.ScreenResponse.Botao;
import com.example.sicredi_challenge.entities.dtos.ScreenResponse.FormItem;
import com.example.sicredi_challenge.entities.dtos.ScreenResponse.SelecaoItem;
import com.example.sicredi_challenge.exceptions.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/screens")
@Tag(name = "Telas", description = "Telas e formulários para interação com pautas e votos")
public class ScreenController {

    @Value("${app.host}")
    private String host;

    @GetMapping("/create-agenda")
    @Operation(
            summary = "Obter formulário de criação de pauta",
            description = "Retorna os campos e as ações necessárias para criar uma nova pauta."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Formulário retornado com sucesso", content = @Content(schema = @Schema(implementation = ScreenResponse.class))),
            @ApiResponse(responseCode = "405", description = "Método HTTP não permitido", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
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
    @Operation(
            summary = "Obter formulário de abertura de pauta",
            description = "Retorna o formulário para informar o tempo de votação e abrir uma pauta existente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Formulário retornado com sucesso", content = @Content(schema = @Schema(implementation = ScreenResponse.class))),
            @ApiResponse(responseCode = "400", description = "Identificador da pauta inválido", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "405", description = "Método HTTP não permitido", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
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
    @Operation(
            summary = "Obter tela de votação",
            description = "Retorna a tela de votação para uma pauta, com as opções Sim e Não."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tela de votação retornada com sucesso", content = @Content(schema = @Schema(implementation = ScreenResponse.class))),
            @ApiResponse(responseCode = "400", description = "Identificador da pauta inválido", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "405", description = "Método HTTP não permitido", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ScreenResponse> getVoteScreenForAgenda(@PathVariable Long agendaId) {
        return getVoteSelectionScreenForAgenda(agendaId);
    }

    @GetMapping("/agendas/{agendaId}/select-vote")
    @Operation(
            summary = "Obter opções de voto",
            description = "Retorna as opções Sim e Não e a URL para registrar o voto da pauta."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Opções de voto retornadas com sucesso", content = @Content(schema = @Schema(implementation = ScreenResponse.class))),
            @ApiResponse(responseCode = "400", description = "Identificador da pauta inválido", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "405", description = "Método HTTP não permitido", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
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
