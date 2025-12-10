package br.edu.infnet.almoxarifado.config;

import br.edu.infnet.almoxarifado.service.PedidoEventListener;
import br.edu.infnet.almoxarifado.events.PedidoCriadoEvent;
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
    public Consumer<Message<PedidoCriadoEvent>> receberPedidoCriado() {
        return message -> {
            pedidoEventListener.handlePedidoCriado(message.getPayload());
        };
    }
}
