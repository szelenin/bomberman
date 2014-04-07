package com.codenjoy.bomberman;

import com.codenjoy.bomberman.utils.Board;
import org.apache.commons.io.FileUtils;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketClientFactory;

import javax.rmi.CORBA.Util;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: serhiy.zelenin
 * Date: 4/8/13
 * Time: 11:42 PM
 */
public class WebSocketRunner {

    private static final String SERVER = "ws://tetrisj.jvmhost.net:12270/codenjoy-contest/ws";
    //    private static final String SERVER = "ws://127.0.0.1:8080/codenjoy-contest/ws";
    private static String USER_NAME = "sergey";

    private WebSocket.Connection connection;
    private WebSocketClientFactory factory;


    public static void main(String[] args) throws Exception {
        run(SERVER, USER_NAME);
    }

    private static void run(String server, String userName) throws Exception {
        final WebSocketRunner client = new WebSocketRunner();
        client.start(server, userName);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    client.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void stop() throws Exception {
        connection.close();
        factory.stop();
    }

    private void start(String server, String userName) throws Exception {
        final Pattern urlPattern = Pattern.compile("^board=(.*)$");
        factory = new WebSocketClientFactory();
        factory.start();
        org.eclipse.jetty.websocket.WebSocketClient client = factory.newWebSocketClient();
        connection = client.open(new URI(server + "?user=" + userName), new WebSocket.OnTextMessage() {
            public void onOpen(Connection connection) {
                System.out.println("Opened");
            }

            public void onClose(int closeCode, String message) {
                System.out.println("Closed");
            }

            public void onMessage(String data) {
                System.out.println("data = " + data);
                Matcher matcher = urlPattern.matcher(data);
                if (!matcher.matches()) {
                    throw new RuntimeException("WTF? " + data);
                }
                String boardString = matcher.group(1);
                try {
                    GameState state = new GameState(boardString);
                    FileUtils.write(new File("out.txt"), boardString+"\n", true);

                    if (state.isDead()) {
                        System.out.println("DEAD!!!");
                    }
                    Action action = new AstarBombChoppersAgent().getAction(state);
                    connection.sendMessage(action.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        connection.sendMessage(Action.STOP.toString());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }).get(5000, TimeUnit.MILLISECONDS);
    }
}
