package fr.ulity.core_v3.messaging;

import fr.ulity.core_v3.Core;

public class SocketServer {
    public static void start() {
        new fr.ulity.socketexchange.api.SocketServer(Core.config.getInt("bungeecord.socket_port"));
    }
}
