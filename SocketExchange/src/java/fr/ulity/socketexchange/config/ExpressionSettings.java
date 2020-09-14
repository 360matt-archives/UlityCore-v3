package fr.ulity.socketexchange.config;

public class ExpressionSettings {

    public static Server server = new Server();
    public static Client client = new Client();

    public static class Server {
        public String PREFIX = "[SocketExchange] ";

        public String STARTED = "Socket server started";
        public String LOGGED = "Client connected: %name%";
        public String LOGOUT = "Client disconnected: %name%";
    }

    public static class Client {
        public String PREFIX = "[SocketExchange] ";

        public String LOGGED = "%name% : have started connection to the server %connection%";
        public String LOGOUT = "%name% : has lost connection to the server %connection%";
        public String RETRYING = "%name% : attempt to reconnect in 5 seconds ...";
    }

}
