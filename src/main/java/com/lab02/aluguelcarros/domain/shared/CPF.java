package com.lab02.aluguelcarros.domain.shared;

import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class CPF {

    private String valor;

    protected CPF() {}

    public CPF(String valor) {
        String normalizado = normalizar(valor);
        if (!isValid(normalizado)) {
            throw new IllegalArgumentException("CPF invalido: " + valor);
        }
        this.valor = normalizado;
    }

    /** Remove mascara e valida digitos verificadores. */
    public static boolean isValid(String cpf) {
        if (cpf == null) return false;
        String digits = normalizar(cpf);
        if (digits.length() != 11 || digits.chars().distinct().count() == 1) return false;

        int soma = 0;
        for (int i = 0; i < 9; i++) soma += (digits.charAt(i) - '0') * (10 - i);
        int d1 = (soma * 10) % 11;
        if (d1 == 10) d1 = 0;
        if (d1 != (digits.charAt(9) - '0')) return false;

        soma = 0;
        for (int i = 0; i < 10; i++) soma += (digits.charAt(i) - '0') * (11 - i);
        int d2 = (soma * 10) % 11;
        if (d2 == 10) d2 = 0;
        return d2 == (digits.charAt(10) - '0');
    }

    private static String normalizar(String cpf) {
        return cpf == null ? "" : cpf.replaceAll("[^\\d]", "");
    }

    public String getValor() { return valor; }

    /** Retorna CPF no formato xxx.xxx.xxx-xx */
    @Override
    public String toString() {
        if (valor == null || valor.length() != 11) return valor;
        return valor.substring(0, 3) + "." + valor.substring(3, 6) + "."
                + valor.substring(6, 9) + "-" + valor.substring(9);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CPF c)) return false;
        return Objects.equals(valor, c.valor);
    }

    @Override
    public int hashCode() { return Objects.hash(valor); }
}
