package com.example.demo;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ClientEndpointConfig extends javax.websocket.ClientEndpointConfig.Configurator {
    private static final String adminToken = "adminToken";
    private static final String userToken = "userToken";

    private static int index = 0;

    @Override
    public void beforeRequest(Map<String, List<String>> headers) {
        System.out.println("Adding headers");
        headers.put("X-Auth-Token", Collections.singletonList(adminToken));
        headers.put("Sec-WebSocket-Protocol", Collections.singletonList("row-protocol"));
        headers.put("Origin", Collections.singletonList("myOrigin"));
    }
}
