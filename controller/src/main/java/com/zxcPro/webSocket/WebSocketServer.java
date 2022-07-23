package com.zxcPro.webSocket;

import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/webSocket/{oid}")
public class WebSocketServer {

    private static ConcurrentHashMap<String, Session> sessionMap = new ConcurrentHashMap<>();

    @OnOpen
    public void open(@PathParam("oid") String oid, Session session) {
        sessionMap.put(oid, session);
    }

    @OnClose
    public void close(@PathParam("oid") String oid) {
        sessionMap.remove(oid);
    }

    public static void sendMsg(String oid, String msg) {
        try {
            Session session = sessionMap.get(oid);
            session.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
