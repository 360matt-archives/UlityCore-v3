package fr.ulity.socketexchange.api;


import fr.ulity.socketexchange.config.ExpressionSettings;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SocketClient {
    private static final ConcurrentHashMap<String, Consumer<JSONObject>> listeners = new ConcurrentHashMap<>();

    private Socket client;
    private PrintWriter out;
    private BufferedReader in;

    public SocketClient (String name, String host, int port) {
        new Thread(() -> {
            try {
                registerDefaultListener();
                while (true) {
                    // auto-reconnect
                    try {
                        client = new Socket(host, port);
                        client.setTcpNoDelay(true);
                        out = new PrintWriter(new BufferedOutputStream(client.getOutputStream()), true);
                        in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                        out.println("{\"__type\": \"auth\", \"__auth\": \"" + name + "\"}");
                        out.flush();

                        if (!ExpressionSettings.client.LOGGED.equals("none"))
                            System.out.println(
                                    ExpressionSettings.client.PREFIX +
                                            ExpressionSettings.client.LOGGED
                                                    .replaceAll("%connection%", host + ":" + port)
                                                    .replaceAll("%name%", name)
                            );

                        while (client.isConnected() && !client.isClosed()) {
                            // listen futures requests

                            String request = in.readLine();
                            if (request == null) // is deconnected
                                break;

                            try {
                                processRequest(new JSONObject(request));
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println("Pas de Json :/");
                            }
                        }

                        if (!ExpressionSettings.client.LOGOUT.equals("none"))
                            System.out.println(
                                    ExpressionSettings.client.PREFIX +
                                            ExpressionSettings.client.LOGOUT
                                                    .replaceAll("%connection%", host + ":" + port)
                                                    .replaceAll("%name%", name)
                            );
                    } catch (Exception e) {
                        if (!ExpressionSettings.client.RETRYING.equals("none"))
                            System.out.println(
                                    ExpressionSettings.client.PREFIX +
                                            ExpressionSettings.client.RETRYING
                                                    .replaceAll("%name%", name)
                            );

                        TimeUnit.SECONDS.sleep(2L);
                    } finally {
                        try {
                            client.close();
                            out.close();
                            in.close();
                        } catch (Exception ignored) { }
                    }
                }
            } catch (Exception ignored) {}
        }).start();
    }

    public void registerDefaultListener () {
        listen("isOnline", x -> {
            reply(x, x);
        });
    }


    public Pattern patternListeners = Pattern.compile("(.*)#[0-9]*@([A-Za-z])");
    public void processRequest (JSONObject request) {
        new Thread(() -> {
            String channel = request.getString("__channel");

            Iterator<String> it = listeners.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();

                Matcher matcher = patternListeners.matcher(key);
                if (matcher.find()) {
                    if (matcher.group(1).equals(channel)) {
                        listeners.get(key).accept(request);
                    if (matcher.group(2).equals("y"))
                        it.remove();
                    }
                }
            }
        }).start();
    }


    public void listen (String channel, Consumer<JSONObject> callback) {
        int randomNum = ThreadLocalRandom.current().nextInt(20, 5001);
        listeners.put(channel + "#" + randomNum + "@n", callback);
    }

    public void reply (JSONObject before, JSONObject reply) {
        if (!client.isClosed() && client.isConnected()) {
            reply.put("__recipient", before.getString("__sender"));
            reply.put("__channel", "reply_" + before.getString("__channel"));
            reply.put("__type", "transfer");

            out.println(reply.toString());
            out.flush();
        }
    }

    public void reply (JSONObject before, HashMap<String, Object> reply) {
        JSONObject jsonObject = new JSONObject("{}");
        reply.forEach(jsonObject::put);
        reply(before, jsonObject);
    }

    public Callback send (String server, String channel, JSONObject data) {
        if (!client.isClosed() && client.isConnected()) {
            data.put("__type", "transfer");
            data.put("__recipient", server);
            data.put("__channel", channel);

            out.println(data.toString());
            out.flush();
            return new Callback(data);
        }
        return new Callback();
    }

    public Callback send (String server, String channel, HashMap<String, Object> data) {
        JSONObject json = new JSONObject("{}");
        data.forEach(json::put);
        return send(server, channel, json);
    }

    public boolean isOnline (String server) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        send(server, "isOnline", new HashMap<>())
                .callback(x -> future.complete(true))
                .waiting(0.5)
                .timeout(x -> future.complete(false));

        return future.join();
    }

    public int getPing (String server) {
        Long date = new Date().getTime();
        CompletableFuture<Long> future = new CompletableFuture<>();
        send(server, "isOnline", new HashMap<>())
                .callback(x -> future.complete(new Date().getTime()))
                .waiting(2)
                .timeout(x -> future.complete(new Date().getTime()));

        return (int) (future.join() - date);
    }

    public static class Callback {
        private final JSONObject json;
        private double wait = 5.0D;
        private String id = "";

        public Callback () { this.json = null; }
        public Callback (JSONObject json) { this.json = json; }

        public Callback callback (Consumer<JSONObject> callback) {
            if (json != null) {
                int randomNum = ThreadLocalRandom.current().nextInt(20, 5001);
                this.id = "reply_" + json.getString("__channel") + "#" + randomNum + "@y";
                listeners.put(this.id, callback);
            }
            return this;
        }

        public Callback waiting (double seconds) { wait = seconds; return this; }

        public Callback timeout (Consumer<Void> fail) {
            if (json != null) {
                new Thread(() -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep((long) (this.wait * 1000.0D));
                        if (listeners.containsKey(this.id)) {
                            listeners.remove(this.id);
                            fail.accept(null);
                        }
                    } catch (Exception ignored) { }
                }).start();
            }
            return this;
        }
    }

}
