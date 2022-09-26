package com.example.testwebsocketclient;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class WebSocketOptions {
    private String serverIP;
    private int serverPort;
    private Map<String, String> headers;

    public WebSocketOptions(@NonNull Builder builder) {
        this.serverIP = builder.serverIP;
        this.serverPort = builder.serverPort;
        this.headers = builder.headers;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getServerIP() {
        return serverIP;
    }

    public int getServerPort() {
        return serverPort;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public static class Builder {
        private String serverIP;
        private int serverPort;
        private Map<String, String> headers;

        public Builder setServerIP(String serverIP) {
            this.serverIP = serverIP;
            return this;
        }

        public Builder setServerPort(int serverPort) {
            this.serverPort = serverPort;
            return this;
        }

        public Builder setHeaders(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder addHeader(String key, String value) {
            if(this.headers == null) {
                this.headers = new HashMap<>();
            }

            this.headers.put(key, value);
            return this;
        }

        public WebSocketOptions build() {
            return new WebSocketOptions(this);
        }
    }
}
