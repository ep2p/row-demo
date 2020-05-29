package com.example.demo;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ClientEndpointConfig extends javax.websocket.ClientEndpointConfig.Configurator {
    private static final String[] tokens = new String[]{
            "1e87d425-e1c5-4996-9620-555c3e9e60f0",
            "8b66be68-90f7-44cd-9898-1e63edbdf2c4",
            "ce1a0888-1b76-4540-a58b-7c393aa51dea",
//            "jkl",

    };

    private static int index = 0;

    @Override
    public void beforeRequest(Map<String, List<String>> headers) {
        System.out.println("Adding headers");
        headers.put("Sec-WebSocket-Protocol", Arrays.asList("token:" + tokens[index++]));
        headers.put("Origin", Arrays.asList("myOrigin"));
    }
}
