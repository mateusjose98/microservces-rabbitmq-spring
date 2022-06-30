package io.github.mateusjose98.msavaliadorcredito.application;

import io.github.mateusjose98.msavaliadorcredito.application.ex.DadosClienteNaoEncontradoException;
import io.github.mateusjose98.msavaliadorcredito.application.ex.ErroComunicacaoMicroservicesException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("avaliacoes-credito")
@RequiredArgsConstructor
public class AvaliadorCreditoController {

    final private AvaliadorCreditoService avaliadorCreditoService;

    @GetMapping("status")
    public String status() {
        return "ok";
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

}
