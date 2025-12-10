package br.edu.infnet.almoxarifado.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoCriadoEvent {

    private Long pedidoId;
    private List<ItemPedido> itens;
    private EnderecoDTO endereco;
    private LocalDateTime dataCriacao;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemPedido {
        private Long produtoId;
        private Integer quantidade;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnderecoDTO {
        private String rua;
        private String numero;
        private String cidade;
        private String estado;
        private String cep;
    }
}
