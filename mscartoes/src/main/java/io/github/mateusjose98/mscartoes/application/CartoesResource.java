package io.github.mateusjose98.mscartoes.application;

import io.github.mateusjose98.mscartoes.application.representation.CartaoSaveRequest;
import io.github.mateusjose98.mscartoes.application.representation.CartoesPorClienteResponse;
import io.github.mateusjose98.mscartoes.domain.Cartao;
import io.github.mateusjose98.mscartoes.domain.ClienteCartao;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("cartoes")
@RequiredArgsConstructor
public class CartoesResource {

    private final CartaoService cartaoService;

    private final ClienteCartaoService clienteCartaoService;

    @GetMapping("status")
    public String status() {
        return "ok";
    }



    @PostMapping
    public ResponseEntity cadastra(@RequestBody CartaoSaveRequest request){
        Cartao cartao = request.toModel();
        cartaoService.save(cartao);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @GetMapping
    public ResponseEntity<List<Cartao>> getCartoesRendaAte(@RequestParam("renda") Long renda){
        List<Cartao> list = cartaoService.getCartoesRendaMenorIgual(renda);
        return ResponseEntity.ok(list);
    }

    @GetMapping(params="cpf")
    public ResponseEntity<List<CartoesPorClienteResponse>> listCartoesByCliente(@RequestParam String cpf){
        List<ClienteCartao> lista = clienteCartaoService.listCartoesByCpf(cpf);
        List<CartoesPorClienteResponse> result = lista.stream()
                                        .map(CartoesPorClienteResponse::fromModel)
                                        .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

}
