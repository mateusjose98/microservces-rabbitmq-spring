package io.github.mateusjose98.msavaliadorcredito.application;

import feign.FeignException;
import io.github.mateusjose98.msavaliadorcredito.application.ex.DadosClienteNaoEncontradoException;
import io.github.mateusjose98.msavaliadorcredito.application.ex.ErroComunicacaoMicroservicesException;
import io.github.mateusjose98.msavaliadorcredito.application.ex.ErroSolicitacaoCartaoException;
import io.github.mateusjose98.msavaliadorcredito.domain.model.*;
import io.github.mateusjose98.msavaliadorcredito.infra.clients.CartoesResourceClient;
import io.github.mateusjose98.msavaliadorcredito.infra.clients.ClienteResourceClient;
import io.github.mateusjose98.msavaliadorcredito.infra.clients.mq.SolicitacaoEmissaoCartaoPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteResourceClient clienteResourceClient;
    private final CartoesResourceClient cartoesResourceClient;

    private final SolicitacaoEmissaoCartaoPublisher emissaoCartaoPublisher;

    public ProtocoloSolicitacaoCartao solicitarEmissaoCartao(DadosSolicitacaoEmissaoCartao dadosSolicitacaoEmissaoCartao){
        try {
            emissaoCartaoPublisher.solicitarCartao(dadosSolicitacaoEmissaoCartao);
            var protocolo = UUID.randomUUID().toString();
            return new ProtocoloSolicitacaoCartao(protocolo);
        }catch (Exception e){
            throw new ErroSolicitacaoCartaoException(e.getMessage());
        }
    }

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

    public RetornoAvaliacaoCliente realizarAvaliacaoCliente(String cpf, Long renda) throws ErroComunicacaoMicroservicesException, DadosClienteNaoEncontradoException{
       try {
           ResponseEntity<DadosCliente> resposta = clienteResourceClient.dadosCliente(cpf);
           ResponseEntity<List<Cartao>> cartoesResponse = cartoesResourceClient.getCartoesRendaAte(renda);
           List<Cartao> cartoes = cartoesResponse.getBody();
           List<CartaoAprovado> listaCartoesAprovados = cartoes.stream().map(cartao -> {
               BigDecimal limiteBasico = cartao.getLimiteBasico();
               BigDecimal rendaDB = BigDecimal.valueOf(renda);
               BigDecimal idade = BigDecimal.valueOf(resposta.getBody().getIdade());

               BigDecimal limiteAprovado = (idade.divide(BigDecimal.TEN)).multiply(limiteBasico);

               CartaoAprovado aprovado = new CartaoAprovado();
               aprovado.setLimiteAprovado(limiteAprovado);
               aprovado.setCartao(cartao.getNome());
               aprovado.setBandeira(cartao.getBandeiraCartao());
               return aprovado;
           }).collect(Collectors.toList());

           return new RetornoAvaliacaoCliente(listaCartoesAprovados);
       }catch (FeignException.FeignClientException e) {
           int status = e.status();
           if (HttpStatus.NOT_FOUND.value() == status){
               throw new DadosClienteNaoEncontradoException();
           }
           throw new ErroComunicacaoMicroservicesException(e.getMessage(), status);
       }


    }


}
