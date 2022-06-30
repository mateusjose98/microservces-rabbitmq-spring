package io.github.mateusjose98.msclientes.application.representation;

import io.github.mateusjose98.msclientes.domain.Cliente;
import lombok.Data;

@Data
public class ClienteSaveRequest {

    private String cpf;
    private String nome;
    private Integer idade;

    public Cliente toModel(){
        return new Cliente(cpf, nome, idade);
    }

    public ClienteSaveRequest(String cpf, String nome, Integer idade) {
        this.cpf = cpf;
        this.nome = nome;
        this.idade = idade;
    }

}
