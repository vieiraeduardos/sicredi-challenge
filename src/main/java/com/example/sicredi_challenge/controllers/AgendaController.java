package com.example.sicredi_challenge.controllers;

import com.example.sicredi_challenge.entities.dtos.AgendaResultResponse;
import com.example.sicredi_challenge.entities.dtos.CreateAgendaRequest;
import com.example.sicredi_challenge.entities.dtos.CreateAgendaResponse;
import com.example.sicredi_challenge.entities.dtos.UpdateAgendaRequest;
import com.example.sicredi_challenge.entities.dtos.VoteRequest;
import com.example.sicredi_challenge.entities.dtos.VoteResponse;
import com.example.sicredi_challenge.exceptions.ErrorResponse;
import com.example.sicredi_challenge.services.AgendaService;
import com.example.sicredi_challenge.services.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(name = "Agendas", description = "Operações de criação, abertura, votação e consulta de pautas")
public class AgendaController {
    @Autowired
    private AgendaService agendaService;

    @Autowired
    private VoteService voteService;

    @PostMapping("/agendas")
        @Operation(
            summary = "Criar uma pauta",
            description = "Cria uma nova pauta com título e descrição. A pauta é criada fechada e deve ser aberta antes do início da votação."
        )
        @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pauta criada com sucesso", content = @Content(schema = @Schema(implementation = CreateAgendaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "405", description = "Método HTTP não permitido", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
    public ResponseEntity<CreateAgendaResponse> createAgenda(@RequestBody CreateAgendaRequest createAgendaRequest) {
        CreateAgendaResponse response = agendaService.createAgenda(createAgendaRequest);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/agendas/{id}")
    @Operation(
        summary = "Abrir uma pauta",
        description = "Abre uma pauta existente e inicia o período de votação informado em minutos."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pauta aberta com sucesso", content = @Content(schema = @Schema(implementation = CreateAgendaResponse.class))),
        @ApiResponse(responseCode = "400", description = "Período de votação ou corpo da requisição inválido", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Pauta não encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "405", description = "Método HTTP não permitido", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<CreateAgendaResponse> openAgenda(@PathVariable Long id, @RequestBody(required = false) UpdateAgendaRequest updateAgendaRequest) {
        CreateAgendaResponse response = agendaService.openAgenda(id, updateAgendaRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/agendas/{id}/votes")
    @Operation(
        summary = "Registrar um voto",
        description = "Registra o voto de um associado em uma pauta aberta."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Voto registrado com sucesso", content = @Content(schema = @Schema(implementation = VoteResponse.class))),
        @ApiResponse(responseCode = "400", description = "Voto inválido, associado já votou ou pauta fechada", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Pauta não encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "405", description = "Método HTTP não permitido", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<VoteResponse> vote(@PathVariable Long id, @RequestBody VoteRequest voteRequest) {
        VoteResponse response = voteService.vote(id, voteRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/agendas/{id}/result")
    @Operation(
        summary = "Consultar resultado da pauta",
        description = "Retorna o status da pauta, o total de votos e a apuração entre Sim e Não."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Resultado retornado com sucesso", content = @Content(schema = @Schema(implementation = AgendaResultResponse.class))),
        @ApiResponse(responseCode = "404", description = "Pauta não encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "405", description = "Método HTTP não permitido", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<AgendaResultResponse> getResult(@PathVariable Long id) {
        AgendaResultResponse response = agendaService.getAgendaResult(id);
        return ResponseEntity.ok(response);
    }
}
