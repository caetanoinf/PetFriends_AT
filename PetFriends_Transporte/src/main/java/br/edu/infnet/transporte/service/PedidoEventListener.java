package br.edu.infnet.transporte.service;

import br.edu.infnet.transporte.events.PedidoDespachadoEvent;
import br.edu.infnet.transporte.events.PedidoEnviadoEvent;
import br.edu.infnet.transporte.domain.Endereco;
import br.edu.infnet.transporte.domain.Entrega;
import br.edu.infnet.transporte.infra.repository.EntregaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PedidoEventListener {

    private static final Logger LOG = LoggerFactory.getLogger(PedidoEventListener.class);

    private final EntregaRepository entregaRepository;
    private final StreamBridge streamBridge;

    public PedidoEventListener(EntregaRepository entregaRepository, StreamBridge streamBridge) {
        this.entregaRepository = entregaRepository;
        this.streamBridge = streamBridge;
    }

    public void handlePedidoDespachado(PedidoDespachadoEvent event) {
        LOG.info("Recebido evento de pedido despachado: {}", event.getPedidoId());

        Endereco endereco = new Endereco(
                event.getEndereco().getRua(),
                event.getEndereco().getNumero(),
                event.getEndereco().getCidade(),
                event.getEndereco().getEstado(),
                event.getEndereco().getCep()
        );

        Entrega entrega = Entrega.builder()
                .pedidoId(event.getPedidoId())
                .endereco(endereco)
                .build();

        entregaRepository.save(entrega);

        LOG.info("Entrega criada para o pedido: {}", event.getPedidoId());

        PedidoEnviadoEvent pedidoEnviadoEvent = PedidoEnviadoEvent.builder()
                .pedidoId(event.getPedidoId())
                .codigoRastreio("BR" + event.getPedidoId() + "PET")
                .dataEnvio(LocalDateTime.now())
                .build();

        streamBridge.send("pedidoEnviado-out-0", pedidoEnviadoEvent);
        LOG.info("Evento PedidoEnviadoEvent publicado para o pedido: {}", event.getPedidoId());
    }
}
