package com.example.testwebsocketclient;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class WebSocketApp {
    private final String name;
    private final WebSocketOptions webSocketOptions;
    private static final Map<String, WebSocketApp> instances = new HashMap<>();

    private WebSocketApp(String name, WebSocketOptions webSocketOptions) {
        this.name = name;
        this.webSocketOptions = webSocketOptions;
    }

    public static WebSocketApp initializeApp(WebSocketOptions options, String name) {
        if(!instances.containsKey(name)) {
            WebSocketApp webSocketApp = new WebSocketApp(name, options);
            instances.put(name, webSocketApp);
            return webSocketApp;
        }

        throw new RuntimeException("WebSocketApp " + name + " já existe!");
    }

    public WebSocketOptions getWebSocketOptions() {
        return webSocketOptions;
    }

    public static List<WebSocketApp> getApps() {
        List<WebSocketApp> list = new ArrayList<>(instances.values());
        return Collections.unmodifiableList(list);

    }

    public static WebSocketApp getInstance() {
        return getInstance("[DEFAULT]");
    }

    public static WebSocketApp getInstance(@NonNull String name) {
        WebSocketApp webSocketApp = instances.get(name);

        if(webSocketApp != null) {
            return webSocketApp;
        }

        throw new RuntimeException(String.format("WebSocketApp com o nome %s não existe.", name));
    }

    public static WebSocketApp initializeApp(WebSocketOptions options) {
        return initializeApp(options, "[DEFAULT]");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WebSocketApp that = (WebSocketApp) o;
        return Objects.equals(name, that.name) && Objects.equals(webSocketOptions, that.webSocketOptions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, webSocketOptions);
    }
}
