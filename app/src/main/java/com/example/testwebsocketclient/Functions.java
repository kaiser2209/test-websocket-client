package com.example.testwebsocketclient;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Functions {
    private static WebSocketClient client;

    public static void startWebSocket() {
        URI uri = URI.create("ws://192.168.0.118:4251");
        Map<String, String> headers = new HashMap<>();
        //headers.put("empresaId", "PeOioGKz1M4y5ANk38Ip");
        //headers.put("deviceId", "4EnMM3ZspmpNuVUj5FQS");
        headers.put("empresaId", "T2fZy4iQih5jFoiUBI1q");
        headers.put("deviceId", "0RJiBnpeAL5gu7YZjhuG");
        //headers.put("empresaId", "Sa6YOQUOFpAJmHImmbFw");
        //headers.put("deviceId", "tzszjp90EdrrsMIFPnnx");
        headers.put("nick", "Charles");
        client = new WebSocketClient(uri, headers);

        client.setOpenHandler(new WebSocketClient.OpenHandler() {
            @Override
            public void onOpen() {
                client.addDeviceListener(new WebSocketClient.DeviceListener() {
                    @Override
                    public void onReceive(Map<String, Object> deviceData) {
                        Log.d("WebSocketClient", "DeviceListener: " + deviceData);
                        String filialId = String.valueOf(deviceData.get("filialId"));

                        client.addFilialListener(filialId, new WebSocketClient.FilialListener() {
                            @Override
                            public void onReceive(Map<String, Object> filialData) {
                                Log.d("WebSocketClient", "FilialListener: " + filialData);
                            }
                        });
                    }
                });

                //119wexezx9iBa5nRdAW7
                client.addDefinitionListener("1K9CqLFEw331SSdBI81U", new WebSocketClient.DefinitionListener() {
                    @Override
                    public void onReceive(Map<String, Object> definitionData) {
                        Log.d("WebSocketClient", "DefinitionListener: " + definitionData);
                    }
                });

                client.addMediaListener("c9b10UykX1pCFor9VyaC", new WebSocketClient.MediaListener() {
                    @Override
                    public void onReceive(Map<String, Object> mediaData) {
                        Log.d("WebSocketClient", "MediaListener: " + mediaData);
                        List<Map<String, Object>> lista = (List<Map<String, Object>>) mediaData.get("data");
                        for(Map<String, Object> l : lista) {
                            Log.d("WebSocketClient", "Type (" + l.get("type") + "): " + l);
                        }
                    }
                });

                client.setCloseHandler(new WebSocketClient.CloseHandler() {
                    @Override
                    public void onClose() {
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                client.reconnect();
                            }
                        }, 1000);
                    }
                });
                /*client.addCommandListener(new WebSocketClient.CommandListener() {
                    @Override
                    public void onReceive(Map<String, Object> commandData) {
                        Log.d("WebSocketClient", "CommandData" + commandData);
                    }
                });*/
            }
        });

        client.setMessageHanlder(new WebSocketClient.MessageHanlder() {
            @Override
            public void handle(Map<String, Object> message) {
                Log.d("WebSocketClient", "Map: " + message);
            }
        });

        client.connect();

        Runnable reconnect = () -> reconnect(client);

        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(reconnect, 60, 60, TimeUnit.SECONDS);
    }

    public static void reconnect(WebSocketClient client) {
        if(!client.isOpen()) {
            Log.d("WebSocketClient", "Reabrindo conexão...");
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    client.reconnect();
                }
            }, 500);
        }
    }

    public static void closeClient() {
        client.close();
    }
}
