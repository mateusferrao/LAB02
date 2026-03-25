package com.lab02.aluguelcarros.application;

import com.lab02.aluguelcarros.domain.cliente.Cliente;
import com.lab02.aluguelcarros.domain.cliente.Rendimento;
import com.lab02.aluguelcarros.domain.shared.CPF;
import com.lab02.aluguelcarros.domain.shared.Money;
import com.lab02.aluguelcarros.interfaces.ClienteRepository;
import com.lab02.aluguelcarros.presentation.dto.ClienteForm;
import com.lab02.aluguelcarros.presentation.dto.RendimentoForm;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Singleton
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public List<Cliente> listarTodos() {
        return repository.findAll();
    }

    public Cliente buscarPorId(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente nao encontrado: " + id));
    }

    @Transactional
    public Cliente criar(ClienteForm form) {
        validarForm(form);

        String cpfNormalizado = form.getCpf().replaceAll("[^\\d]", "");
        repository.findByCpf(cpfNormalizado).ifPresent(c -> {
            throw new CpfJaExisteException(form.getCpf());
        });

        Cliente cliente = new Cliente(
                form.getNome().trim(),
                new CPF(form.getCpf()),
                form.getRg().trim(),
                form.getEndereco().trim(),
                form.getProfissao().trim()
        );

        adicionarRendimentos(cliente, form.getRendimentos());
        return repository.save(cliente);
    }

    @Transactional
    public Cliente atualizar(UUID id, ClienteForm form) {
        validarForm(form);
        Cliente cliente = buscarPorId(id);

        String cpfNormalizado = form.getCpf().replaceAll("[^\\d]", "");
        repository.findByCpf(cpfNormalizado).ifPresent(existente -> {
            if (!existente.getId().equals(id)) {
                throw new CpfJaExisteException(form.getCpf());
            }
        });

        cliente.setNome(form.getNome().trim());
        cliente.setCpf(new CPF(form.getCpf()));
        cliente.setRg(form.getRg().trim());
        cliente.setEndereco(form.getEndereco().trim());
        cliente.setProfissao(form.getProfissao().trim());

        cliente.clearRendimentos();
        adicionarRendimentos(cliente, form.getRendimentos());

        return repository.update(cliente);
    }

    @Transactional
    public void deletar(UUID id) {
        repository.deleteById(id);
    }

    private void validarForm(ClienteForm form) {
        if (isBlank(form.getNome()))       throw new IllegalArgumentException("Nome e obrigatorio");
        if (isBlank(form.getCpf()))        throw new IllegalArgumentException("CPF e obrigatorio");
        if (isBlank(form.getRg()))         throw new IllegalArgumentException("RG e obrigatorio");
        if (isBlank(form.getEndereco()))   throw new IllegalArgumentException("Endereco e obrigatorio");
        if (isBlank(form.getProfissao()))  throw new IllegalArgumentException("Profissao e obrigatoria");
        if (!CPF.isValid(form.getCpf()))   throw new IllegalArgumentException("CPF invalido. Informe um CPF valido (ex: 935.411.347-80)");
    }

    private boolean isBlank(String s) { return s == null || s.isBlank(); }

    private void adicionarRendimentos(Cliente cliente, List<RendimentoForm> rendimentoForms) {
        if (rendimentoForms == null) return;
        rendimentoForms.stream()
                .filter(r -> r.getFonte() != null && !r.getFonte().isBlank())
                .limit(3)
                .forEach(r -> {
                    BigDecimal valor = parseBigDecimal(r.getValor());
                    cliente.addRendimento(new Rendimento(new Money(valor), r.getFonte().trim()));
                });
    }

    private BigDecimal parseBigDecimal(String s) {
        if (s == null || s.isBlank()) return BigDecimal.ZERO;
        try {
            return new BigDecimal(s.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }
}
