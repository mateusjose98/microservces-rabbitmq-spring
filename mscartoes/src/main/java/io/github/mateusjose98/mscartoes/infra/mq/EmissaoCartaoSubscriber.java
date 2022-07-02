package io.github.mateusjose98.mscartoes.infra.mq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mateusjose98.mscartoes.domain.Cartao;
import io.github.mateusjose98.mscartoes.domain.ClienteCartao;
import io.github.mateusjose98.mscartoes.domain.DadosSolicitacaoEmissaoCartao;
import io.github.mateusjose98.mscartoes.infra.CartaoRepository;
import io.github.mateusjose98.mscartoes.infra.ClienteCartaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmissaoCartaoSubscriber {

    private final CartaoRepository repository;
    private final ClienteCartaoRepository clienteCartaoRepository;


    @RabbitListener(queues = "${mq.queues.emissao-cartoes}")
    public void receberSolicitacaoEmissao(@Payload String payload) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            DadosSolicitacaoEmissaoCartao dados = mapper.readValue(payload, DadosSolicitacaoEmissaoCartao.class);
            Cartao cartao = repository.findById(dados.getIdCartao()).orElseThrow();
            ClienteCartao clienteCartao = new ClienteCartao();
            clienteCartao.setCartao(cartao);
            clienteCartao.setCpf(dados.getCpf());
            clienteCartao.setLimite(dados.getLimiteLiberado());
            clienteCartaoRepository.save(clienteCartao);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


    }
}
