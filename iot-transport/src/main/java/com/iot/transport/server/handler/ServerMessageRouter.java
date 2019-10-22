package com.iot.transport.server.handler;

import com.iot.common.connection.TransportConnection;
import com.iot.config.RsocketServerConfig;
import com.iot.transport.DirectHandler;
import com.iot.transport.DirectHandlerAdaptor;
import com.iot.transport.DirectHandlerFactory;
import io.netty.handler.codec.mqtt.MqttMessage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Getter
@Slf4j
public class ServerMessageRouter {


    private final RsocketServerConfig config;

    private final DirectHandlerAdaptor directHandlerAdaptor;

    public ServerMessageRouter(RsocketServerConfig config) {
        this.config=config;
        this.directHandlerAdaptor= DirectHandlerFactory::new;
    }

    public Mono<Void> handler(MqttMessage message, TransportConnection connection) {
        log.info("accept message {}",message);
        DirectHandler handler=directHandlerAdaptor.handler(message.fixedHeader().messageType()).loadHandler();
        return handler.handler(message,connection,config);
    }

}