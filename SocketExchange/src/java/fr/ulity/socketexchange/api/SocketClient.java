package fr.ulity.socketexchange.api;


import fr.ulity.socketexchange.config.ExpressionSettings;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SocketClient {
    private static final ConcurrentHashMap<String, Consumer<JSONObject>> listeners = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Callback> callbacks = new ConcurrentHashMap<>();

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
                                System.out.println("Malformed JSON: " + request);
                                System.out.println("Pas de Json :/   --   WTF the JSON ??");
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

                        TimeUnit.SECONDS.sleep(5L);
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
        listen("isOnline", x -> reply(x, new JSONObject("{}")));
    }


    public Pattern patternListeners = Pattern.compile("(.*)#([0-9]*)");
    public void processRequest (JSONObject request) {
        String channel = request.getString("__channel");
        String id = String.valueOf(request.getInt("__id_reply"));

        if (!channel.startsWith("reply_")) {
            /* permanent listeners */
            listeners.forEach((k, v) -> {
                Matcher matcher = patternListeners.matcher(k);

                if (matcher.find() && matcher.group(1).equals(channel))
                    listeners.get(k).accept(request);
            });
        } else {
            /* one time listeners */
            Iterator<String> it = callbacks.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();

                Matcher matcher = patternListeners.matcher(key);

                if (matcher.find() && matcher.group(1).equals(channel) && matcher.group(2).equals(id)) {
                    Callback callback = callbacks.get(key);
                    callback.consumer.accept(request);
                    if (callback.threadTimeout != null)
                        callback.threadTimeout.interrupt();
                    it.remove();
                    break;
                }
            }
        }
    }


    public void listen (String channel, Consumer<JSONObject> json) {
        int randomNum = ThreadLocalRandom.current().nextInt(20, 5001);
        listeners.put(channel + "#" + randomNum, json);
    }

    public void reply (JSONObject before, JSONObject reply) {
        if (!client.isClosed() && client.isConnected()) {
            reply.put("__type", "transfer");
            reply.put("__id_reply", before.getInt("__id_reply"));
            reply.put("__recipient", before.getString("__sender"));
            reply.put("__channel", "reply_" + before.getString("__channel"));

            out.println(reply.toString());
            out.flush();
        }
    }

    public void reply (JSONObject before, HashMap<String, Object> reply) {
        final JSONObject jsonObject = new JSONObject("{}");
        reply.forEach(jsonObject::put);
        reply(before, jsonObject);
    }

    public Callback send (String server, String channel, JSONObject data) {
        if (!client.isClosed() && client.isConnected()) {
            int id = ThreadLocalRandom.current().nextInt(20, 10001);

            data.put("__type", "transfer");
            data.put("__id_reply", id);
            data.put("__recipient", server);
            data.put("__channel", channel);

            out.println(data.toString());
            out.flush();
            return new Callback(data, id);
        }
        return new Callback();
    }

    public Callback send (String server, String channel, HashMap<String, Object> data) {
        JSONObject jsonObject = new JSONObject("{}");
        data.forEach(jsonObject::put);
        return send(server, channel, jsonObject);
    }

    public boolean isOnline (String server) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        send(server, "isOnline", new HashMap<>())
                .callback(x -> future.complete(true))
                .timeout(x -> future.complete(false));

        return future.join();
    }

    public int getPing (String server) {
        Long date = new Date().getTime();
        CompletableFuture<Long> future = new CompletableFuture<>();
        send(server, "isOnline", new HashMap<>())
                .callback(x -> future.complete(new Date().getTime()))
                .waiting(1)
                .timeout(x -> future.complete(new Date().getTime()));

        return (int) (future.join() - date);
    }

    public static class Callback {
        private final JSONObject json;
        private String id;
        private int rdmID;

        private Consumer<JSONObject> consumer;

        private Thread threadTimeout;
        private float wait = 0.05F;


        public Callback () { this.json = null; }
        public Callback (JSONObject json, int rdmID) { this.json = json; this.rdmID = rdmID; }

        public Callback callback (Consumer<JSONObject> callback) {
            this.consumer = callback;
            if (json != null) {
                this.id = "reply_" + json.getString("__channel") + "#" + rdmID;
                callbacks.put(this.id, this);
            }
            return this;
        }

        public Callback waiting (float seconds) { wait = seconds; return this; }

        public void timeout (Consumer<Void> fail) {
            if (json != null) {
                threadTimeout = new Thread(() -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep((long) (this.wait * 1000));
                        if (callbacks.containsKey(id)) {
                            callbacks.remove(id);
                            fail.accept(null);
                        }
                    } catch (Exception ignored) { }
                });
                threadTimeout.start();
            } else
                fail.accept(null);
        }
    }

}
