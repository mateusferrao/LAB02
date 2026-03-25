package com.lab02.aluguelcarros.presentation.dto;

import com.lab02.aluguelcarros.domain.cliente.Cliente;
import io.micronaut.serde.annotation.Serdeable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Serdeable
public class ClienteForm {

    private UUID id;
    private String nome;
    private String cpf;
    private String rg;
    private String endereco;
    private String profissao;
    private List<RendimentoForm> rendimentos = new ArrayList<>();

    public ClienteForm() {
        // inicializa 3 slots vazios para o form HTML
        rendimentos.add(new RendimentoForm("", ""));
        rendimentos.add(new RendimentoForm("", ""));
        rendimentos.add(new RendimentoForm("", ""));
    }

    public static ClienteForm fromEntity(Cliente c) {
        ClienteForm form = new ClienteForm();
        form.id = c.getId();
        form.nome = c.getNome();
        form.cpf = c.getCpf() != null ? c.getCpf().getValor() : "";
        form.rg = c.getRg();
        form.endereco = c.getEndereco();
        form.profissao = c.getProfissao();

        form.rendimentos.clear();
        c.getRendimentos().forEach(r -> form.rendimentos.add(
                new RendimentoForm(r.getFonte(), r.getValor().getValor().toPlainString())));
        // completar ate 3 slots
        while (form.rendimentos.size() < 3) {
            form.rendimentos.add(new RendimentoForm("", ""));
        }
        return form;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getRg() { return rg; }
    public void setRg(String rg) { this.rg = rg; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getProfissao() { return profissao; }
    public void setProfissao(String profissao) { this.profissao = profissao; }
    public List<RendimentoForm> getRendimentos() { return rendimentos; }
    public void setRendimentos(List<RendimentoForm> rendimentos) { this.rendimentos = rendimentos; }
}
