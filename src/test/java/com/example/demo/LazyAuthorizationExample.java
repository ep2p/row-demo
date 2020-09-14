package com.example.demo;

import com.example.demo.api.LoginDto;
import com.example.demo.api.LoginResponseDto;
import com.example.demo.api.SampleDto;
import lab.idioglossia.row.client.RowClient;
import lab.idioglossia.row.client.callback.ResponseCallback;
import lab.idioglossia.row.client.model.RowRequest;
import lab.idioglossia.row.client.model.RowResponse;
import lab.idioglossia.row.client.tyrus.RowClientConfig;
import lab.idioglossia.row.client.tyrus.TyrusRowWebsocketClient;

import java.io.IOException;

public class LazyAuthorizationExample {
    public static void main(String[] args) throws IOException, InterruptedException {
        RowClient rowClient = new TyrusRowWebsocketClient(RowClientConfig.builder()
                .address("ws://localhost:8080/ws")
                .build());
        rowClient.open();

        //check access before logging in
        RowRequest<SampleDto, SampleDto> checkAccessRequest = RowRequest.<SampleDto, SampleDto>builder()
                .address("/checkAccess")
                .method(RowRequest.RowMethod.GET)
                .build();
        rowClient.sendRequest(checkAccessRequest, new ResponseCallback<SampleDto>(SampleDto.class) {
            @Override
            public void onResponse(RowResponse<SampleDto> rowResponse) {
                System.out.println(rowResponse);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("No access. Error message: "+ throwable.getMessage());
            }
        });

        Thread.sleep(500);

        //invalid login
        RowRequest<LoginDto, Void> loginRequest = RowRequest.<LoginDto, Void>builder()
                .address("/login")
                .method(RowRequest.RowMethod.POST)
                .body(new LoginDto("sepehr", "123456"))
                .build();
        rowClient.sendRequest(loginRequest, new ResponseCallback<LoginResponseDto>(LoginResponseDto.class) {
            @Override
            public void onResponse(RowResponse<LoginResponseDto> rowResponse) {
                System.out.println("Invalid login response: "+ rowResponse);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }
        });

        Thread.sleep(500);

        loginRequest.setBody(new LoginDto("sepehr", "12345"));
        rowClient.sendRequest(loginRequest, new ResponseCallback<LoginResponseDto>(LoginResponseDto.class) {
            @Override
            public void onResponse(RowResponse<LoginResponseDto> rowResponse) {
                System.out.println("Valid login response: "+ rowResponse);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }
        });

        Thread.sleep(500);

        //recheck access
        rowClient.sendRequest(checkAccessRequest, new ResponseCallback<SampleDto>(SampleDto.class) {
            @Override
            public void onResponse(RowResponse<SampleDto> rowResponse) {
                System.out.println(rowResponse);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("No access. Error message: "+ throwable.getMessage());
            }
        });

        rowClient.close();
        Thread.sleep(1000);
    }
}
