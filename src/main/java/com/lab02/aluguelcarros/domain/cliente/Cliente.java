package com.lab02.aluguelcarros.domain.cliente;

import com.lab02.aluguelcarros.domain.shared.CPF;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @NotBlank(message = "Nome e obrigatorio")
    @Size(max = 200)
    private String nome;

    @Embedded
    @AttributeOverride(name = "valor", column = @Column(name = "cpf", nullable = false, unique = true, length = 11))
    private CPF cpf;

    @Column(nullable = false)
    @NotBlank(message = "RG e obrigatorio")
    private String rg;

    @Column(nullable = false)
    @NotBlank(message = "Endereco e obrigatorio")
    private String endereco;

    @Column(nullable = false)
    @NotBlank(message = "Profissao e obrigatoria")
    private String profissao;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Size(max = 3, message = "Maximo de 3 rendimentos por cliente")
    private List<Rendimento> rendimentos = new ArrayList<>();

    protected Cliente() {}

    public Cliente(String nome, CPF cpf, String rg, String endereco, String profissao) {
        this.nome = nome;
        this.cpf = cpf;
        this.rg = rg;
        this.endereco = endereco;
        this.profissao = profissao;
    }

    public void addRendimento(Rendimento rendimento) {
        if (rendimentos.size() >= 3) {
            throw new IllegalStateException("Cliente ja possui o maximo de 3 rendimentos");
        }
        rendimento.setCliente(this);
        rendimentos.add(rendimento);
    }

    public void removeRendimento(Rendimento rendimento) {
        rendimentos.remove(rendimento);
        rendimento.setCliente(null);
    }

    public void clearRendimentos() {
        rendimentos.forEach(r -> r.setCliente(null));
        rendimentos.clear();
    }

    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public CPF getCpf() { return cpf; }
    public String getRg() { return rg; }
    public String getEndereco() { return endereco; }
    public String getProfissao() { return profissao; }
    public List<Rendimento> getRendimentos() { return Collections.unmodifiableList(rendimentos); }

    public void setNome(String nome) { this.nome = nome; }
    public void setCpf(CPF cpf) { this.cpf = cpf; }
    public void setRg(String rg) { this.rg = rg; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public void setProfissao(String profissao) { this.profissao = profissao; }
}
