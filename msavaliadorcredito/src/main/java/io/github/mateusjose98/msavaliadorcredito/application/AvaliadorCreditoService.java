package io.github.mateusjose98.msavaliadorcredito.application;

import feign.FeignException;
import io.github.mateusjose98.msavaliadorcredito.application.ex.DadosClienteNaoEncontradoException;
import io.github.mateusjose98.msavaliadorcredito.application.ex.ErroComunicacaoMicroservicesException;
import io.github.mateusjose98.msavaliadorcredito.domain.model.CartaoCliente;
import io.github.mateusjose98.msavaliadorcredito.domain.model.DadosCliente;
import io.github.mateusjose98.msavaliadorcredito.domain.model.SituacaoCliente;
import io.github.mateusjose98.msavaliadorcredito.infra.clients.CartoesResourceClient;
import io.github.mateusjose98.msavaliadorcredito.infra.clients.ClienteResourceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteResourceClient clienteResourceClient;
    private final CartoesResourceClient cartoesResourceClient;

    public SituacaoCliente obterSituacaoCliente(String cpf) throws ErroComunicacaoMicroservicesException, DadosClienteNaoEncontradoException{
     try {
         ResponseEntity<DadosCliente> resposta = clienteResourceClient.dadosCliente(cpf);
         ResponseEntity<List<CartaoCliente>> cartoes = cartoesResourceClient.listCartoesByCliente(cpf);
         return SituacaoCliente.builder().cliente(resposta.getBody()).cartoes(cartoes.getBody()).build();
     }catch (FeignException.FeignClientException e) {
        int status = e.status();
        if (HttpStatus.NOT_FOUND.value() == status){
            throw new DadosClienteNaoEncontradoException();
        }
        throw new ErroComunicacaoMicroservicesException(e.getMessage(), status);
     }



    }


}
