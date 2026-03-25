package com.lab02.aluguelcarros.presentation.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class RendimentoForm {

    private String fonte;
    private String valor; // String para evitar falha de binding em campos vazios

    public RendimentoForm() {}

    public RendimentoForm(String fonte, String valor) {
        this.fonte = fonte;
        this.valor = valor;
    }

    public String getFonte() { return fonte; }
    public void setFonte(String fonte) { this.fonte = fonte; }
    public String getValor() { return valor; }
    public void setValor(String valor) { this.valor = valor; }
}
