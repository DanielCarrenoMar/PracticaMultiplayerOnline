package src.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server implements Runnable{
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    public Integer port;
    public String ip;
    private ServerSocket serverSocket;

    public Server(Integer port) {
        this.port = port;
        try {
            this.serverSocket = new ServerSocket(port);
            this.ip = serverSocket.getInetAddress().getHostAddress();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Falla al crear el servidor en el puerto " + port, e);
        }
    }

    @Override
    public void run() {
    }
}
