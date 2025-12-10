package br.edu.infnet.pedidos.service;

import br.edu.infnet.pedidos.domain.Pedido;
import br.edu.infnet.pedidos.domain.StatusPedido;
import br.edu.infnet.pedidos.events.PedidoCriadoEvent;
import br.edu.infnet.pedidos.infra.repository.PedidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private static final Logger LOG = LoggerFactory.getLogger(PedidoService.class);

    private final PedidoRepository pedidoRepository;
    private final StreamBridge streamBridge;

    public PedidoService(PedidoRepository pedidoRepository, StreamBridge streamBridge) {
        this.pedidoRepository = pedidoRepository;
        this.streamBridge = streamBridge;
    }

    public Pedido criarPedido(Pedido pedido) {
        pedido.setDataCriacao(LocalDateTime.now());
        pedido.setStatus(StatusPedido.NOVO);

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        PedidoCriadoEvent.EnderecoDTO enderecoDTO = pedidoSalvo.getEndereco() != null
                ? PedidoCriadoEvent.EnderecoDTO.builder()
                        .rua(pedidoSalvo.getEndereco().getRua())
                        .numero(pedidoSalvo.getEndereco().getNumero())
                        .cidade(pedidoSalvo.getEndereco().getCidade())
                        .estado(pedidoSalvo.getEndereco().getEstado())
                        .cep(pedidoSalvo.getEndereco().getCep())
                        .build()
                : null;

        PedidoCriadoEvent event = PedidoCriadoEvent.builder()
                .pedidoId(pedidoSalvo.getId())
                .itens(pedidoSalvo.getItens().stream()
                        .map(item -> new PedidoCriadoEvent.ItemPedido(
                                item.getProdutoId(),
                                item.getQuantidade()
                        ))
                        .collect(Collectors.toList()))
                .endereco(enderecoDTO)
                .dataCriacao(pedidoSalvo.getDataCriacao())
                .build();

        streamBridge.send("pedidoCriado-out-0", event);
        LOG.info("Evento PedidoCriadoEvent publicado para o pedido: {}", pedidoSalvo.getId());

        return pedidoSalvo;
    }
}
