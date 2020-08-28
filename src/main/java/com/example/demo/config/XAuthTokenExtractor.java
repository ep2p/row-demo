package com.example.demo.config;

import lab.idioglossia.row.exception.AuthenticationFailedException;
import lab.idioglossia.row.ws.TokenExtractor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component("tokenExtractor")
public class XAuthTokenExtractor implements TokenExtractor {
    private final String HEADER_NAME = "X-Auth-Token";

    @Override
    public String getToken(ServerHttpRequest serverHttpRequest) throws AuthenticationFailedException {
        System.out.println("Using X-Auth-Token extractor");
        HttpHeaders handshakeHeaders = serverHttpRequest.getHeaders();
        if (handshakeHeaders.containsKey(HEADER_NAME)) {
            return handshakeHeaders.get(HEADER_NAME).get(0);
        }
        throw new AuthenticationFailedException();
    }
}
