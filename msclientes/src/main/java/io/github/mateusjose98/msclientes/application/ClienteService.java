package io.github.mateusjose98.msclientes.application;

import io.github.mateusjose98.msclientes.domain.Cliente;
import io.github.mateusjose98.msclientes.infra.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Transactional
    public Cliente save(Cliente cliente){
        return clienteRepository.save(cliente);
    }

    public Optional<Cliente> getByCPF(String cpf){
        return clienteRepository.findByCpf(cpf);
    }
}
