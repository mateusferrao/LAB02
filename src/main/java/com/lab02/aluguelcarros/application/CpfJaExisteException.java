package com.lab02.aluguelcarros.application;

public class CpfJaExisteException extends RuntimeException {
    public CpfJaExisteException(String cpf) {
        super("Ja existe um cliente cadastrado com o CPF: " + cpf);
    }
}
