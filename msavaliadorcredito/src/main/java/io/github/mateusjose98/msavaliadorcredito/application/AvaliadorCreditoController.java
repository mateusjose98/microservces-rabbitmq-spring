package io.github.mateusjose98.msavaliadorcredito.application;

import io.github.mateusjose98.msavaliadorcredito.application.ex.DadosClienteNaoEncontradoException;
import io.github.mateusjose98.msavaliadorcredito.application.ex.ErroComunicacaoMicroservicesException;
import io.github.mateusjose98.msavaliadorcredito.application.ex.ErroSolicitacaoCartaoException;
import io.github.mateusjose98.msavaliadorcredito.domain.model.DadosAvaliacao;
import io.github.mateusjose98.msavaliadorcredito.domain.model.DadosSolicitacaoEmissaoCartao;
import io.github.mateusjose98.msavaliadorcredito.domain.model.ProtocoloSolicitacaoCartao;
import io.github.mateusjose98.msavaliadorcredito.domain.model.RetornoAvaliacaoCliente;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("avaliacoes-credito")
@RequiredArgsConstructor
public class AvaliadorCreditoController {

    final private AvaliadorCreditoService avaliadorCreditoService;

    @GetMapping("status")
    public String status() {
        return "ok";
    }


    @PostMapping("solicitacoes-cartao")
    public ResponseEntity solicitarCartao(@RequestBody DadosSolicitacaoEmissaoCartao dados){
        try {
            ProtocoloSolicitacaoCartao protocoloSolicitacaoCartao = avaliadorCreditoService.solicitarEmissaoCartao(dados);
            return ResponseEntity.ok(protocoloSolicitacaoCartao);
        }catch (ErroSolicitacaoCartaoException e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value = "situacao-cliente", params = "cpf")
    public ResponseEntity consultaSituacaoCliente(@RequestParam String cpf){

        try {
            var situacaoCliente = avaliadorCreditoService.obterSituacaoCliente(cpf);
            return ResponseEntity.ok(situacaoCliente);
        } catch (ErroComunicacaoMicroservicesException e) {
            return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());

        } catch (DadosClienteNaoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }

    }
    @PostMapping
    public ResponseEntity realizarAvaliacao(@RequestBody DadosAvaliacao dadosAvaliacao){
        try {
            RetornoAvaliacaoCliente retornoAvaliacaoCliente = avaliadorCreditoService.realizarAvaliacaoCliente(dadosAvaliacao.getCpf(), dadosAvaliacao.getRenda());
            return ResponseEntity.ok(retornoAvaliacaoCliente);
        } catch (ErroComunicacaoMicroservicesException e) {
            return ResponseEntity.status(HttpStatus.resolve(e.getStatus())).body(e.getMessage());

        } catch (DadosClienteNaoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
