package br.edu.infnet.almoxarifado.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;
    private Integer quantity;

    @Embedded
    private Localizacao localizacao;

    public void adicionarQuantidade(Integer qtd) {
        if (qtd == null || qtd <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }
        this.quantity += qtd;
    }

    public void removerQuantidade(Integer qtd) {
        if (qtd == null || qtd <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }
        if (qtd > this.quantity) {
            throw new IllegalArgumentException("Estoque insuficiente");
        }
        this.quantity -= qtd;
    }
}
