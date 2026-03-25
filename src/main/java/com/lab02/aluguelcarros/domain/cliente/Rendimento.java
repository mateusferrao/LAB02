package com.lab02.aluguelcarros.domain.cliente;

import com.lab02.aluguelcarros.domain.shared.Money;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name = "rendimentos")
public class Rendimento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Embedded
    @AttributeOverride(name = "valor", column = @Column(name = "valor", nullable = false))
    @NotNull(message = "Valor do rendimento e obrigatorio")
    @Valid
    private Money valor;

    @Column(nullable = false)
    @NotBlank(message = "Fonte do rendimento e obrigatoria")
    private String fonte;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    protected Rendimento() {}

    public Rendimento(Money valor, String fonte) {
        this.valor = valor;
        this.fonte = fonte;
    }

    public UUID getId() { return id; }
    public Money getValor() { return valor; }
    public String getFonte() { return fonte; }
    public Cliente getCliente() { return cliente; }

    void setCliente(Cliente cliente) { this.cliente = cliente; }
    public void setValor(Money valor) { this.valor = valor; }
    public void setFonte(String fonte) { this.fonte = fonte; }
}
