package com.lab02.aluguelcarros.infrastructure;

import com.lab02.aluguelcarros.domain.cliente.Cliente;
import com.lab02.aluguelcarros.interfaces.ClienteRepository;
import io.micronaut.transaction.annotation.ReadOnly;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class ClienteRepositoryImpl implements ClienteRepository {

    private final EntityManager em;

    public ClienteRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    @Transactional
    public Cliente save(Cliente cliente) {
        em.persist(cliente);
        return cliente;
    }

    @Override
    @ReadOnly
    public Optional<Cliente> findById(UUID id) {
        return Optional.ofNullable(em.find(Cliente.class, id));
    }

    @Override
    @ReadOnly
    public List<Cliente> findAll() {
        return em.createQuery("SELECT c FROM Cliente c ORDER BY c.nome", Cliente.class)
                .getResultList();
    }

    @Override
    @Transactional
    public Cliente update(Cliente cliente) {
        return em.merge(cliente);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        findById(id).ifPresent(em::remove);
    }

    @Override
    @ReadOnly
    public Optional<Cliente> findByCpf(String cpfValor) {
        try {
            Cliente c = em.createQuery(
                    "SELECT c FROM Cliente c WHERE c.cpf.valor = :cpf", Cliente.class)
                    .setParameter("cpf", cpfValor)
                    .getSingleResult();
            return Optional.of(c);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
