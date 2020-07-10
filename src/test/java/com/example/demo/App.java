package com.example.demo;

import com.example.demo.api.SampleDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import labs.psychogen.row.RowEndpoint;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.util.UUID;

public class App {
    public static void main(String[] args) throws IOException, DeploymentException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.setAsyncSendTimeout(20 * 60 * 1000);
        container.setDefaultMaxSessionIdleTimeout(20 * 60 * 1000);
        container.setDefaultMaxBinaryMessageBufferSize(8192 * 1000);
        container.setDefaultMaxTextMessageBufferSize(8192 * 1000);

        Session session = container.connectToServer(WebSocketHandler.class, URI.create("ws://localhost:8080/ws"));



        RequestDto requestDto = new RequestDto();
        ObjectMapper objectMapper = new ObjectMapper();


        //T1
        requestDto.setMethod(RowEndpoint.RowMethod.GET.getName().toUpperCase());
        requestDto.setAddress("/t1");
        requestDto.setId(UUID.randomUUID().toString());

        String data = objectMapper.writeValueAsString(requestDto);
        System.out.println(data);

        session.getAsyncRemote().sendText(data);


        //T2
        requestDto.setMethod(RowEndpoint.RowMethod.POST.getName().toUpperCase());
        requestDto.setAddress("/t2");
        requestDto.setId(UUID.randomUUID().toString());
        requestDto.setBody(new SampleDto("test"));

        data = objectMapper.writeValueAsString(requestDto);
        System.out.println(data);

        session.getAsyncRemote().sendText(data);

        //T3
        requestDto.setMethod(RowEndpoint.RowMethod.POST.getName().toUpperCase());
        requestDto.setAddress("/t3");
        requestDto.setId(UUID.randomUUID().toString());
        requestDto.setBody(new SampleDto("test"));
        requestDto.setQuery(new SampleDto("query"));

        data = objectMapper.writeValueAsString(requestDto);
        System.out.println(data);

        session.getAsyncRemote().sendText(data);

        //T4
        requestDto.setMethod(RowEndpoint.RowMethod.GET.getName().toUpperCase());
        requestDto.setAddress("/t4/hello");
        requestDto.setId(UUID.randomUUID().toString());
        requestDto.setQuery(null);
        requestDto.setBody(null);

        data = objectMapper.writeValueAsString(requestDto);
        System.out.println(data);

        session.getAsyncRemote().sendText(data);


        //T4
        requestDto.setMethod(RowEndpoint.RowMethod.GET.getName().toUpperCase());
        requestDto.setAddress("/t5");
        requestDto.setId(UUID.randomUUID().toString());
        requestDto.setQuery(null);
        requestDto.setBody(null);

        data = objectMapper.writeValueAsString(requestDto);
        System.out.println(data);

        session.getAsyncRemote().sendText(data);

        //Subs - T1
        requestDto.setMethod(RowEndpoint.RowMethod.GET.getName().toUpperCase());
        requestDto.setAddress("/subs/t1");
        requestDto.setId(UUID.randomUUID().toString());
        requestDto.setQuery(null);
        requestDto.setBody(null);

        data = objectMapper.writeValueAsString(requestDto);
        System.out.println(data);

        session.getAsyncRemote().sendText(data);


        //Subs - publish - t1
        requestDto.setMethod(RowEndpoint.RowMethod.POST.getName().toUpperCase());
        requestDto.setAddress("/subs/publish/t1");
        requestDto.setId(UUID.randomUUID().toString());
        requestDto.setQuery(null);
        requestDto.setBody(new SampleDto("this should be published :D"));

        data = objectMapper.writeValueAsString(requestDto);
        System.out.println(data);

        session.getAsyncRemote().sendText(data);


        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
