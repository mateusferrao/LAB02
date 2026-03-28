package com.lab02.aluguelcarros.domain.shared;

import io.micronaut.core.annotation.Introspected;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Introspected
@Embeddable
public class Money {

    private BigDecimal valor;

    protected Money() {}

    public Money(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor monetario deve ser nao-negativo");
        }
        this.valor = valor;
    }

    public static Money of(double valor) {
        return new Money(BigDecimal.valueOf(valor));
    }

    public BigDecimal getValor() { return valor; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money m)) return false;
        return Objects.equals(valor, m.valor);
    }

    @Override
    public int hashCode() { return Objects.hash(valor); }

    @Override
    public String toString() {
        return valor != null ? String.format("R$ %,.2f", valor) : "R$ 0,00";
    }
}
