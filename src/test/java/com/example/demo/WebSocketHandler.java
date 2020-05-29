package com.example.demo;

import javax.websocket.*;
import java.util.HashMap;
import java.util.Map;

@ClientEndpoint(configurator = ClientEndpointConfig.class)
public class WebSocketHandler {

    @OnMessage
    public void onPong(PongMessage pongMessage){
        System.out.println("PONGED");
        System.out.println(new String(pongMessage.getApplicationData().array()));
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println(" RECEIVED : " + message);
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        System.out.println(" CONNECTION OPENED ");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println(" CONNECTION CLOSED : " + closeReason.toString());
    }

}
