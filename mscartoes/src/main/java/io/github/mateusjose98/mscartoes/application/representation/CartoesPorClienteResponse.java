package io.github.mateusjose98.mscartoes.application.representation;

import io.github.mateusjose98.mscartoes.domain.ClienteCartao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartoesPorClienteResponse {

    private String nome;
    private String bandeira;
    private BigDecimal limiteBandeira;

    public static CartoesPorClienteResponse fromModel(ClienteCartao model){

        return new CartoesPorClienteResponse(
                                            model.getCartao().getNome(),
                                            model.getCartao().getBandeiraCartao().toString(),
                                            model.getLimite());

    }
}
