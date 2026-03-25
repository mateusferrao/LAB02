package com.lab02.aluguelcarros.interfaces;

import com.lab02.aluguelcarros.domain.cliente.Cliente;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClienteRepository {
    Cliente save(Cliente cliente);
    Optional<Cliente> findById(UUID id);
    List<Cliente> findAll();
    Cliente update(Cliente cliente);
    void deleteById(UUID id);
    Optional<Cliente> findByCpf(String cpfValor);
}
