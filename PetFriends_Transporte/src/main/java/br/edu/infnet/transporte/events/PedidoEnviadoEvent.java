package br.edu.infnet.transporte.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoEnviadoEvent {

    private Long pedidoId;
    private String codigoRastreio;
    private LocalDateTime dataEnvio;
}
