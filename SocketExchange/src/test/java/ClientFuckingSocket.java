import fr.ulity.socketexchange.config.ExpressionSettings;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientFuckingSocket {
    private String name;

    private static final HashMap<String, Consumer<JSONObject>> listeners = new HashMap<>();

    private Socket client;
    private PrintWriter out;
    private BufferedReader in;



    public static void main (String[] args) {
        new ClientFuckingSocket("weeesh", "127.0.0.1", 9000);
        ClientFuckingSocket cli = new ClientFuckingSocket("koukou", "127.0.0.1", 9000);

        new Thread(() -> {
            System.out.println("-");
            System.out.println("Debut");
            System.out.println(cli.isOnline("coucou"));
        });
    }

    public ClientFuckingSocket (String name, String host, int port) {
        new Thread(() -> {
            try {
                this.name = name;
                while (true) {
                    // auto-reconnect
                    try {
                        client = new Socket(host, port);
                        out = new PrintWriter(client.getOutputStream(), true);
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

    public boolean isOnline (String server) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        send(server, "isOnline", new HashMap<>())
                .callback(x -> future.complete(true))
                .waiting(1)
                .timeout(x -> future.complete(false));

        return future.join();
    }


    public Pattern patternListeners = Pattern.compile("(.*)#[0-9]*@([A-Za-z])");
    public void processRequest (JSONObject request) {
        String channel = request.getString("__channel");

        Iterator<Map.Entry<String, Consumer<JSONObject>>> it = listeners.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Consumer<JSONObject>> x = it.next();

            Matcher matcher = patternListeners.matcher(x.getKey());
            if (matcher.find()) {
                if (matcher.group(1).equals(channel)) {
                    x.getValue().accept(request);

                    if (matcher.group(2).equals("y"))
                        it.remove();
                }
            }
        }
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

    public void listen (String channel, Consumer<JSONObject> callback) {
        int randomNum = ThreadLocalRandom.current().nextInt(20, 5001);
        listeners.put(channel + "#" + randomNum + "@n", callback);
    }

    public void reply (JSONObject before, JSONObject reply) {
        if (!client.isClosed() && client.isConnected()) {
            reply.put("__recipient", before.getString("__sender"));
            reply.put("__channel", before.getString("__channel"));
            reply.put("__type", "transfer");

            out.println(reply.toString());
            out.flush();
        }
    }

    public class Callback {
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
