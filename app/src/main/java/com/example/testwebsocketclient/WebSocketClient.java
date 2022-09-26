package com.example.testwebsocketclient;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class WebSocketClient extends org.java_websocket.client.WebSocketClient {
    private OpenHandler openHandler = null;
    private MessageHanlder messageHanlder = null;
    private CloseHandler closeHandler = null;
    private CommandListener commandListener = null;
    private DeviceListener deviceListener = null;
    private FilialListener filialListener = null;
    private DefinitionListener definitionListener = null;
    private MediaListener mediaListener = null;
    private boolean connected = false;
    private boolean autoReconnect = false;
    private WebSocketApp webSocketApp;
    private static List<WebSocketClient> instances = new ArrayList<>();

    @Deprecated
    private WebSocketClient(URI serverUri) {
        super(serverUri);
    }

    private WebSocketClient(URI serverUri, Map<String, String> httpHeaders) {
        super(serverUri, new Draft_6455(), httpHeaders, 0);
    }

    private WebSocketClient(WebSocketOptions webSocketOptions) {
        this(
                URI.create("ws://" + webSocketOptions.getServerIP() + ":" + webSocketOptions.getServerPort()),
                webSocketOptions.getHeaders()
        );
    }

    private WebSocketClient(WebSocketApp webSocketApp) {
        this(webSocketApp.getWebSocketOptions());
        this.webSocketApp = webSocketApp;
    }

    public WebSocketClient getWebSocketClient(WebSocketApp webSocketApp) {
        WebSocketClient webSocketClient = new WebSocketClient(webSocketApp);
        int indexOf = instances.indexOf(webSocketClient);
        if(indexOf > -1) {
            webSocketClient = instances.get(indexOf);
        } else {
            instances.add(webSocketClient);
        }

        if(!webSocketClient.isOpen()) {
            webSocketClient.connect();
        }

        return webSocketClient;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.d("WebSocketClient", "Abriu");
        if (openHandler != null) {
            openHandler.onOpen();
        }

        connected = true;
    }

    @Override
    public void onMessage(String message) {
        Log.d("Message", message);
        Map<String, Object> msg = new Gson().fromJson(
                message, new TypeToken<HashMap<String, Object>>() {
                }.getType()
        );
        if (messageHanlder != null) {
            messageHanlder.handle(msg);
        }

        if (msg.containsKey("id")) {
            String id = String.valueOf(msg.get("id"));
            Map<String, Object> data = (Map<String, Object>) msg.get("data");

            switch (id) {
                case "commandListener":
                    if (commandListener != null)
                        commandListener.onReceive(data);
                    break;
                case "deviceListener":
                    if (deviceListener != null)
                        deviceListener.onReceive(data);
                    break;
                case "filialListener":
                    if (filialListener != null)
                        filialListener.onReceive(data);
                    break;
                case "definitionListener":
                    if (definitionListener != null)
                        definitionListener.onReceive(data);
                    break;
                case "mediaListener":
                    if (mediaListener != null)
                        mediaListener.onReceive(data);
                    break;
            }
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.d("WebSocketClient", "Fechou: " + reason + "(" + code + ")");
        if (this.closeHandler != null) {
            closeHandler.onClose();
        }
    }

    @Override
    public void onError(Exception ex) {
        Log.d("WebSocketClient", "Erro: " + ex.getMessage());
    }

    public void addFirestoreListener() {
        send(JsonUtils.CreateJson.getInstance()
                .addProperty("type", "command")
                .addProperty("cmd", "addFirestoreListener")
                .build()
        );
    }

    public void addCommandListener() {
        send(JsonUtils.CreateJson.getInstance()
                .addProperty("type", "command")
                .addProperty("cmd", "addCommandListener")
                .build()
        );
    }

    public void addDeviceListener() {
        send(JsonUtils.CreateJson.getInstance()
                .addProperty("type", "command")
                .addProperty("cmd", "addDeviceListener")
                .build()
        );
    }

    private void addDefinitionListener(String idDefinition) {
        send(JsonUtils.CreateJson.getInstance()
                .addProperty("type", "command")
                .addProperty("cmd", "addDefinitionListener")
                .addProperty("idDefinition", idDefinition)
                .build()
        );
    }

    public void addFilialListener(String idFilial) {
        send(JsonUtils.CreateJson.getInstance()
                .addProperty("type", "command")
                .addProperty("cmd", "addFilialListener")
                .addProperty("idFilial", idFilial)
                .build()
        );
    }

    public void addMediaListener(String idGroup) {
        send(JsonUtils.CreateJson.getInstance()
                .addProperty("type", "command")
                .addProperty("cmd", "addMediaListener")
                .addProperty("idGroup", idGroup)
                .build()
        );
    }

    public void addCommandListener(CommandListener commandListener) {
        addCommandListener();
        this.commandListener = commandListener;
    }

    public void addDeviceListener(DeviceListener deviceListener) {
        addDeviceListener();
        this.deviceListener = deviceListener;
    }

    public void addFilialListener(String idFilial, FilialListener filialListener) {
        addFilialListener(idFilial);
        this.filialListener = filialListener;
    }

    public void addDefinitionListener(String idDefinition, DefinitionListener definitionListener) {
        addDefinitionListener(idDefinition);
        this.definitionListener = definitionListener;
    }

    public void addMediaListener(String idGroup, MediaListener mediaListener) {
        addMediaListener(idGroup);
        this.mediaListener = mediaListener;
    }

    public void removeFirestoreCommand(String commandId) {
        send(JsonUtils.CreateJson.getInstance()
                .addProperty("type", "command")
                .addProperty("cmd", "removeCommand")
                .addProperty("commandId", commandId)
                .build()
        );
    }

    public void setOpenHandler(OpenHandler openHandler) {
        this.openHandler = openHandler;
    }

    public void setMessageHanlder(MessageHanlder messageHanlder) {
        this.messageHanlder = messageHanlder;
    }

    public void setCloseHandler(CloseHandler closeHandler) {
        this.closeHandler = closeHandler;
    }

    public boolean isConnected() {
        return this.connected;
    }

    public void setAutoReconnect(boolean autoReconnect) {
        this.autoReconnect = autoReconnect;
    }

    public static interface OpenHandler {
        public void onOpen();
    }

    public static interface MessageHanlder {
        public void handle(Map<String, Object> command);
    }

    public static interface CloseHandler {
        public void onClose();
    }

    public static interface CommandListener {
        public void onReceive(Map<String, Object> commandData);
    }

    public static interface DeviceListener {
        public void onReceive(Map<String, Object> deviceData);
    }

    public static interface FilialListener {
        public void onReceive(Map<String, Object> filialData);
    }

    public static interface DefinitionListener {
        public void onReceive(Map<String, Object> definitionData);
    }

    public static interface MediaListener {
        public void onReceive(Map<String, Object> mediaData);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WebSocketClient that = (WebSocketClient) o;
        return Objects.equals(webSocketApp, that.webSocketApp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(webSocketApp);
    }
}
