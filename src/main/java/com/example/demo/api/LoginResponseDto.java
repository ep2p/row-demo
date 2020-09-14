package com.example.demo.api;


public class LoginResponseDto {
    private String response;

    public LoginResponseDto() {
    }

    public LoginResponseDto(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "LoginResponseDto{" +
                "response='" + response + '\'' +
                '}';
    }
}
