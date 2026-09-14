package com.example.sicredi_challenge.entities.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ScreenResponse(
    String tipo,
    String titulo,
    List<?> itens,
    Botao botaoOk,
    Botao botaoCancelar
) {
    public static ScreenResponse formulario(String titulo, List<FormItem> itens, Botao botaoOk, Botao botaoCancelar) {
        return new ScreenResponse("FORMULARIO", titulo, itens, botaoOk, botaoCancelar);
    }

    public static ScreenResponse selecao(String titulo, List<SelecaoItem> itens) {
        return new ScreenResponse("SELECAO", titulo, itens, null, null);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Botao(
        String texto,
        String url,
        Object body
    ) {
        public Botao(String texto, String url) {
            this(texto, url, null);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record FormItem(
        String tipo,
        String id,
        String titulo,
        String texto,
        Object valor
    ) {
        public static FormItem texto(String texto) {
            return new FormItem("TEXTO", null, null, texto, null);
        }

        public static FormItem inputTexto(String id, String titulo, Object valor) {
            return new FormItem("INPUT_TEXTO", id, titulo, null, valor);
        }

        public static FormItem inputNumero(String id, String titulo, Object valor) {
            return new FormItem("INPUT_NUMERO", id, titulo, null, valor);
        }

        public static FormItem inputData(String id, String titulo, Object valor) {
            return new FormItem("INPUT_DATA", id, titulo, null, valor);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record SelecaoItem(
        String texto,
        String url,
        Object body
    ) {
        public SelecaoItem(String texto, String url) {
            this(texto, url, null);
        }
    }
}