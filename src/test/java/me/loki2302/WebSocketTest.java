package me.loki2302;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.Exchanger;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;

public class WebSocketTest {
    @Test
    public void canUseWebSockets() throws Exception {
        Server server = new Server(8080);
        server.setHandler(new WebSocketHandler() {
            @Override
            public void configure(WebSocketServletFactory webSocketServletFactory) {
                webSocketServletFactory.getPolicy().setIdleTimeout(10000);
                webSocketServletFactory.register(MyServerSocket.class);
            }
        });

        server.start();

        WebSocketClient client = new WebSocketClient();
        client.start();

        Exchanger<String> messageExchanger = new Exchanger<String>();

        Future<Session> sessionFuture = client.connect(
                new MyClientSocket(messageExchanger), URI.create("ws://localhost:8080/"));
        Session session = sessionFuture.get();

        String message = messageExchanger.exchange(null);
        assertEquals("hello", message);

        session.close();

        client.stop();

        server.stop();
    }

    public static class MyServerSocket extends WebSocketAdapter {
        @Override
        public void onWebSocketConnect(Session sess) {
            super.onWebSocketConnect(sess);

            System.out.println("Someone connected!");

            try {
                sess.getRemote().sendString("hello");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void onWebSocketText(String message) {
            super.onWebSocketText(message);
        }

        @Override
        public void onWebSocketClose(int statusCode, String reason) {
            super.onWebSocketClose(statusCode, reason);
        }

        @Override
        public void onWebSocketError(Throwable cause) {
            super.onWebSocketError(cause);
        }
    }

    public static class MyClientSocket extends WebSocketAdapter {
        private final Exchanger<String> messageExchanger;

        public MyClientSocket(Exchanger<String> messageExchanger) {
            this.messageExchanger = messageExchanger;
        }

        @Override
        public void onWebSocketConnect(Session sess) {
            super.onWebSocketConnect(sess);
            System.out.printf("ClientSocket: connected\n");
        }

        @Override
        public void onWebSocketText(String message) {
            super.onWebSocketText(message);
            System.out.printf("ClientSocket: message - %s\n", message);

            try {
                messageExchanger.exchange(message);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            getSession().close();
        }

        @Override
        public void onWebSocketClose(int statusCode, String reason) {
            super.onWebSocketClose(statusCode, reason);
            System.out.printf("ClientSocket: closed - %d, %s\n", statusCode, reason);
        }

        @Override
        public void onWebSocketError(Throwable cause) {
            super.onWebSocketError(cause);
            System.out.printf("ClientSocket: error\n");
        }
    }
}
