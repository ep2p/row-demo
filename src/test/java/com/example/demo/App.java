package com.example.demo;

import com.example.demo.api.SampleDto;
import lab.idioglossia.row.client.RowClient;
import lab.idioglossia.row.client.Subscription;
import lab.idioglossia.row.client.callback.ResponseCallback;
import lab.idioglossia.row.client.callback.SubscriptionListener;
import lab.idioglossia.row.client.model.RowRequest;
import lab.idioglossia.row.client.model.RowResponse;
import lab.idioglossia.row.client.tyrus.RowClientConfig;
import lab.idioglossia.row.client.tyrus.TyrusRowWebsocketClient;
import lab.idioglossia.row.client.ws.HandshakeHeadersProvider;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {
    public static void main(String[] args) throws IOException, InterruptedException {
        RowClient rowClient = new TyrusRowWebsocketClient(RowClientConfig.builder()
                .address("ws://localhost:8080/ws")
                .handshakeHeadersProvider(new HandshakeHeadersProvider() {
                    @Override
                    public Map<String, List<String>> getHeaders() {
                        Map<String, List<String>> headers = new HashMap<>();
                        headers.put("X-Auth-Token", Collections.singletonList("adminToken"));
                        return headers;
                    }
                })
                .build());
        rowClient.open();

        //t1
        RowRequest<SampleDto, SampleDto> request = RowRequest.<SampleDto, SampleDto>builder()
                .address("/t1")
                .method(RowRequest.RowMethod.GET)
                .build();
        rowClient.sendRequest(request, new ResponseCallback<SampleDto>(SampleDto.class) {
            @Override
            public void onResponse(RowResponse<SampleDto> rowResponse) {
                System.out.println(rowResponse);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }
        });

        //n1
        request = RowRequest.<SampleDto, SampleDto>builder()
                .address("/n1")
                .method(RowRequest.RowMethod.GET)
                .build();
        rowClient.sendRequest(request, new ResponseCallback<SampleDto>(SampleDto.class) {
            @Override
            public void onResponse(RowResponse<SampleDto> rowResponse) {
                System.out.println(rowResponse);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }
        });

        //n2
        request = RowRequest.<SampleDto, SampleDto>builder()
                .address("/n2")
                .method(RowRequest.RowMethod.GET)
                .build();
        rowClient.sendRequest(request, new ResponseCallback<SampleDto>(SampleDto.class) {
            @Override
            public void onResponse(RowResponse<SampleDto> rowResponse) {
                System.out.println(rowResponse);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }
        });

        //t2
        request.setAddress("/t2");
        request.setMethod(RowRequest.RowMethod.POST);
        request.setBody(new SampleDto("alter me :P "));
        rowClient.sendRequest(request, new ResponseCallback<SampleDto>(SampleDto.class) {
            @Override
            public void onResponse(RowResponse<SampleDto> rowResponse) {
                System.out.println(rowResponse);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }
        });


        //t3
        request.setAddress("/t3");
        request.setQuery(new SampleDto("This is my query"));
        rowClient.sendRequest(request, new ResponseCallback<SampleDto>(SampleDto.class) {
            @Override
            public void onResponse(RowResponse<SampleDto> rowResponse) {
                System.out.println(rowResponse);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }
        });

        //t4
        request.setAddress("/t4/hello");
        request.setMethod(RowRequest.RowMethod.GET);
        request.setQuery(null);
        request.setBody(null);
        rowClient.sendRequest(request, new ResponseCallback<SampleDto>(SampleDto.class) {
            @Override
            public void onResponse(RowResponse<SampleDto> rowResponse) {
                System.out.println(rowResponse);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }
        });


        //subs/t1
        request.setAddress("/subs/t1");
        rowClient.subscribe(request, new ResponseCallback<SampleDto>(SampleDto.class) {
            @Override
            public void onResponse(RowResponse<SampleDto> rowResponse) {
                System.out.println(rowResponse);
                System.out.println(rowResponse.getSubscription());
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }
        }, new SubscriptionListener<SampleDto>(SampleDto.class) {
            @Override
            public void onMessage(Subscription subscription, SampleDto sampleDto) {
                System.out.println(sampleDto);
                System.out.println(subscription);
            }
        });

        Thread.sleep(1000);

        //publish
        request.setAddress("/subs/publish/t1");
        request.setMethod(RowRequest.RowMethod.POST);
        request.setBody(new SampleDto("publish me :P "));
        rowClient.sendRequest(request, new ResponseCallback<SampleDto>(SampleDto.class) {
            @Override
            public void onResponse(RowResponse<SampleDto> rowResponse) {
                System.out.println(rowResponse);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }
        });

        Thread.sleep(5000);

        System.out.println("\n\n-----\n\n");

        //subs/t1
        Map<String, String> headers = new HashMap<>();
        headers.put("CUSTOM_HEADER_SUBS", "myCustomEvent");
        request.setMethod(RowRequest.RowMethod.GET);
        request.setAddress("/subs/t2");
        request.setHeaders(headers);
        rowClient.subscribe(request, new ResponseCallback<SampleDto>(SampleDto.class) {
            @Override
            public void onResponse(RowResponse<SampleDto> rowResponse) {
                System.out.println(rowResponse);
                System.out.println(rowResponse.getSubscription());
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }
        }, new SubscriptionListener<SampleDto>(SampleDto.class) {
            @Override
            public void onMessage(Subscription subscription, SampleDto sampleDto) {
                System.out.println("custom event: "+ sampleDto);
                System.out.println("custom event: "+ subscription);
                subscription.close(new ResponseCallback<SampleDto>(SampleDto.class) {
                    @Override
                    public void onResponse(RowResponse<SampleDto> rowResponse) {
                        System.out.println("Finished un-subscribing :)");
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }
                });
            }
        });

        Thread.sleep(1000);

        //publish
        request.setAddress("/subs/publish/t2");
        request.setMethod(RowRequest.RowMethod.POST);
        request.setBody(new SampleDto("new message to publish for custom event"));
        request.setHeaders(headers);
        rowClient.sendRequest(request, new ResponseCallback<SampleDto>(SampleDto.class) {
            @Override
            public void onResponse(RowResponse<SampleDto> rowResponse) {
                System.out.println(rowResponse);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }
        });

        Thread.sleep(5000);

    }
}
