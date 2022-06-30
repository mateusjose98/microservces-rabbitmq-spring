package io.github.mateusjose98.msavaliadorcredito.application.ex;

public class DadosClienteNaoEncontradoException extends Exception {
    public DadosClienteNaoEncontradoException(){
        super("Dados do cliente não encontrados para o CPF informado");
    }
}
