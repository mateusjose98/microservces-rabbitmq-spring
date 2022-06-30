package io.github.mateusjose98.mscartoes.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@Entity
@NoArgsConstructor
@Data
public class ClienteCartao {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cpf;

    @ManyToOne
    @JoinColumn(name = "id_cartao")
    private Cartao cartao;
    private BigDecimal limite;
}
