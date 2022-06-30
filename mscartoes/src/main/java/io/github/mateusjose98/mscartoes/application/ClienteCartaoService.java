package io.github.mateusjose98.mscartoes.application;

import io.github.mateusjose98.mscartoes.domain.ClienteCartao;
import io.github.mateusjose98.mscartoes.infra.ClienteCartaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteCartaoService {

    private final ClienteCartaoRepository clienteCartaoRepository;

    public List<ClienteCartao> listCartoesByCpf(String cpf){
        return clienteCartaoRepository.findByCpf(cpf);
    }
}
