package io.github.mateusjose98.mscartoes.application.representation;

import io.github.mateusjose98.mscartoes.domain.BandeiraCartao;
import io.github.mateusjose98.mscartoes.domain.Cartao;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class CartaoSaveRequest {

    private String nome;
    private BandeiraCartao bandeiraCartao;
    private BigDecimal renda;
    private BigDecimal limiteBasico;

    public Cartao toModel() {
        return new Cartao(nome, bandeiraCartao, renda, limiteBasico);
    }


}
