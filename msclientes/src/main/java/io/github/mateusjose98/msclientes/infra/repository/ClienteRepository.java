package io.github.mateusjose98.msclientes.infra.repository;

import io.github.mateusjose98.msclientes.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {


    @Query(value = "SELECT c FROM Cliente c WHERE c.cpf = ?1")
   Optional<Cliente> findByCpf(String cpf);
}
