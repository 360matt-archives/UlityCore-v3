package fr.ulity.core_v3.messaging.defaults;

import fr.ulity.core_v3.modules.language.Lang;
import fr.ulity.socketexchange.config.ExpressionSettings;

public class SocketExpressions {
    public static void change () {
        ExpressionSettings.client.PREFIX = Lang.get("socket.prefix");
        ExpressionSettings.client.LOGGED = Lang.get("socket.client.logged");
        ExpressionSettings.client.LOGOUT = Lang.get("socket.client.logout");
        ExpressionSettings.client.RETRYING = Lang.get("socket.client.retrying");

        ExpressionSettings.server.PREFIX = Lang.get("socket.prefix");
        ExpressionSettings.server.STARTED = Lang.get("socket.server.started");
        ExpressionSettings.server.LOGGED = Lang.get("socket.server.logged");
        ExpressionSettings.server.LOGOUT = Lang.get("socket.server.logout");
    }
}
