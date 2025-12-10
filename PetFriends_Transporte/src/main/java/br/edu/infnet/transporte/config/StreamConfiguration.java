package br.edu.infnet.transporte.config;

import br.edu.infnet.transporte.events.PedidoDespachadoEvent;
import br.edu.infnet.transporte.service.PedidoEventListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Configuration
public class StreamConfiguration {

    private final PedidoEventListener pedidoEventListener;

    public StreamConfiguration(PedidoEventListener pedidoEventListener) {
        this.pedidoEventListener = pedidoEventListener;
    }

    @Bean
    public Consumer<Message<PedidoDespachadoEvent>> receberPedidoDespachado() {
        return message -> {
            pedidoEventListener.handlePedidoDespachado(message.getPayload());
        };
    }
}
