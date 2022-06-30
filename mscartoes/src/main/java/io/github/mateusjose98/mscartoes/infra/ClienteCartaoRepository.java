package io.github.mateusjose98.mscartoes.infra;

import io.github.mateusjose98.mscartoes.domain.ClienteCartao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteCartaoRepository extends JpaRepository<ClienteCartao, Long> {

    List<ClienteCartao> findByCpf(String cpf);
}
