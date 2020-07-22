package com.example.demo;

import com.example.demo.api.SampleDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import labs.psychogen.row.RowEndpoint;

import javax.websocket.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ClientEndpoint(configurator = ClientEndpointConfig.class)
public class WebSocketHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @OnMessage
    public void onPong(PongMessage pongMessage){
        System.out.println("PONGED");
        System.out.println(new String(pongMessage.getApplicationData().array()));
    }

    @OnMessage
    public void onMessage(String message, Session session) throws JsonProcessingException, InterruptedException {
        System.out.println(" RECEIVED : " + message);
        ResponseDto responseDto = objectMapper.readValue(message, ResponseDto.class);
        //unsubscribe and publish a message to see if its received again
        if(responseDto.getHeaders() != null && responseDto.getHeaders().containsKey("row-subs-id")){
            Thread.sleep(1000);
            sendUnSubscribe(session, responseDto.getHeaders().get("row-subs-id"));
            Thread.sleep(500);
            publish(session);
        }
    }

    private void publish(Session session) throws JsonProcessingException {
        //Subs - publish - t1
        RequestDto requestDto = new RequestDto();

        requestDto.setMethod(RowEndpoint.RowMethod.POST.getName().toUpperCase());
        requestDto.setAddress("/subs/publish/t1");
        requestDto.setId(UUID.randomUUID().toString());
        requestDto.setQuery(null);
        requestDto.setBody(new SampleDto("this should not be published"));

        String data = objectMapper.writeValueAsString(requestDto);
        System.out.println(data);

        session.getAsyncRemote().sendText(data);
    }

    private void sendUnSubscribe(Session session, String subscriptionId) throws JsonProcessingException {
        RequestDto requestDto = new RequestDto();
        requestDto.setMethod(RowEndpoint.RowMethod.GET.getName().toUpperCase());
        requestDto.setAddress("/subs/t1");
        requestDto.setId(UUID.randomUUID().toString());
        requestDto.setQuery(null);
        requestDto.setBody(null);
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("row-subs-id", subscriptionId);
        stringMap.put("row-unsubscribe", "1");
        requestDto.setHeaders(stringMap);

        String data = objectMapper.writeValueAsString(requestDto);
        System.out.println(data);
        session.getAsyncRemote().sendText(data);
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
