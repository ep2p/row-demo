package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

public class ResponseDto {
    private String requestId;
    private JsonNode body;
    private int status;
    private Map<String, String> headers;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public JsonNode getBody() {
        return body;
    }

    public void setBody(JsonNode body) {
        this.body = body;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
