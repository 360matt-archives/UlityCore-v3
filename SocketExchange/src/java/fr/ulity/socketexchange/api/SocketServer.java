package fr.ulity.socketexchange.api;

import fr.ulity.socketexchange.config.ExpressionSettings;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class SocketServer {

    public boolean online = true;
    public HashMap<String, ClientProcess> clients = new HashMap<>();

    public SocketServer (int port) {
        new Thread(() -> {
            try (ServerSocket server = new ServerSocket(port)) {
                System.out.println(
                        ExpressionSettings.server.PREFIX +
                                ExpressionSettings.server.STARTED
                );

                while (online)
                    new ClientProcess(server.accept());
            } catch (Exception ignored) {}

        }).start();
    }


    public class ClientProcess {
        private Socket client;
        private String name = null;
        private PrintWriter out;
        private BufferedReader in;

        protected ClientProcess (Socket client) {
            new Thread(() -> {
                try {
                    this.client = client;
                    this.client.setTcpNoDelay(true);
                    this.out = new PrintWriter(new BufferedOutputStream(client.getOutputStream()), true);
                    this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                    while (client.isConnected() && !client.isClosed()) {
                        try {
                            String request = this.in.readLine();
                            if (request == null)
                                break;
                            executeCmd(new JSONObject(request));
                        } catch (IOException e) {
                            if (!e.getMessage().equals("JSONObject"))
                                break;
                        }
                    }

                    out.close();
                    in.close();

                    System.out.println(
                            ExpressionSettings.server.PREFIX +
                                    ExpressionSettings.server.LOGOUT.replaceAll("%name%", name)
                    );
                } catch (Exception ignored) { }
            }).start();
        }

        protected void executeCmd (JSONObject request) {
            new Thread(() -> {
                if (request.keySet().contains("__type")) {
                    String type = request.getString("__type");
                    if (type.equals("auth")) {

                        if (request.keySet().contains("__auth")) {
                            this.name = request.getString("__auth");
                            clients.put(this.name, this);

                            System.out.println(
                                    ExpressionSettings.server.PREFIX +
                                            ExpressionSettings.server.LOGGED.replaceAll("%name%", this.name)
                            );
                        }
                    } else {
                        if (type.equals("transfer") && name != null)
                            transferData(request);
                    }
                }
            }).start();
        }

        protected void transferData (JSONObject request) {
            List<String> requires = Arrays.asList("__recipient", "__channel");
            if (request.keySet().containsAll(requires)) {
                if (clients.containsKey(request.getString("__recipient"))) {
                    request.put("__sender", name);

                    String recipient = request.getString("__recipient");
                    if (recipient.equals("all")) {
                        clients.forEach((k, v) -> {
                            if (!v.client.isClosed()) {
                                v.out.println(request);
                                v.out.flush();
                            }
                        });
                    } else {
                        ClientProcess destination = clients.get(recipient);
                        if (!destination.client.isClosed()) {
                            PrintWriter output = destination.out;
                            output.println(request);
                            output.flush();
                        }
                    }
                }
            }
        }
    }
}
