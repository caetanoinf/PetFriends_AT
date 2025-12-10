package br.edu.infnet.almoxarifado.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDespachadoEvent {

    private Long pedidoId;
    private EnderecoDTO endereco;
    private LocalDateTime dataDespacho;

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
