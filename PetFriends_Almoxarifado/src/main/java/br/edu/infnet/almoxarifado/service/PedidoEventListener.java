package br.edu.infnet.almoxarifado.service;

import br.edu.infnet.almoxarifado.domain.Estoque;
import br.edu.infnet.almoxarifado.events.PedidoDespachadoEvent;
import br.edu.infnet.almoxarifado.infra.repository.EstoqueRepository;
import br.edu.infnet.almoxarifado.events.PedidoCriadoEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PedidoEventListener {

    private static final Logger LOG = LoggerFactory.getLogger(PedidoEventListener.class);

    private final EstoqueRepository estoqueRepository;
    private final StreamBridge streamBridge;

    public PedidoEventListener(EstoqueRepository estoqueRepository, StreamBridge streamBridge) {
        this.estoqueRepository = estoqueRepository;
        this.streamBridge = streamBridge;
    }

    public void handlePedidoCriado(PedidoCriadoEvent event) {
        LOG.info("Recebido evento de pedido criado: {}", event.getPedidoId());

        for (PedidoCriadoEvent.ItemPedido item : event.getItens()) {
            Estoque estoque = estoqueRepository.findByProductId(item.getProdutoId());

            if (estoque != null) {
                estoque.removerQuantidade(item.getQuantidade());
                estoqueRepository.save(estoque);
                LOG.info("Estoque atualizado para produto: {}", item.getProdutoId());
            } else {
                LOG.warn("Produto não encontrado no estoque: {}", item.getProdutoId());
            }
        }

        LOG.info("Pedido {} processado pelo almoxarifado", event.getPedidoId());

        PedidoDespachadoEvent despachadoEvent = PedidoDespachadoEvent.builder()
                .pedidoId(event.getPedidoId())
                .endereco(event.getEndereco() != null ? PedidoDespachadoEvent.EnderecoDTO.builder()
                        .rua(event.getEndereco().getRua())
                        .numero(event.getEndereco().getNumero())
                        .cidade(event.getEndereco().getCidade())
                        .estado(event.getEndereco().getEstado())
                        .cep(event.getEndereco().getCep())
                        .build() : null)
                .dataDespacho(LocalDateTime.now())
                .build();

        streamBridge.send("pedidoDespachado-out-0", despachadoEvent);
        LOG.info("Evento PedidoDespachadoEvent publicado para o pedido: {}", event.getPedidoId());
    }
}
