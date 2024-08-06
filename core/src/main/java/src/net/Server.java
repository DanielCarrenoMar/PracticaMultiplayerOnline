package src.net;

import src.entities.Entity;
import src.entities.EntityFactory;
import src.entities.Player;
import src.managers.EntityManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server implements Runnable{
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    public Integer port;
    public String ip;
    private ServerSocket serverSocket;
    private Boolean running = true;
    private final ExecutorService pool = Executors.newFixedThreadPool(10);

    public static EntityManager entityManager = new EntityManager();
    private static final ArrayList<User> users = new ArrayList<User>();

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
        System.out.println("Server abierto en " + ip + ":" + port);
        try{
            while (running) {
                Socket clientSocket = serverSocket.accept();
                User newUser = new User(clientSocket, users.size());
                pool.execute(newUser);
                users.add(newUser);
            }
        }catch (IOException e){
            logger.log(Level.WARNING, "Falla al aceptar la conexion", e);
        }
    }

    public void sendAll(Object[] data, Integer id){
        for (User user : users) {
            if (user.id.equals(id)) continue;
            user.send(data);
        }
    }

    public void close(){
        running = false;
        try {
            for (User user : users) {
                user.running = false;
                user.send(Packet.serverClose());
            }
            serverSocket.close();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Falla al cerrar el servidor", e);
        }
        pool.close();
    }

    private class User implements Runnable{
        private final Socket socket;
        private final Integer id;
        private Boolean running = true;
        private ObjectOutputStream out;

        private User(Socket socket, Integer id) {
            this.socket = socket;
            this.id = id;
        }

        @Override
        public void run() {
            System.out.println("SERVER Nuevo usuario conectado " + id);
            try {
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                out = new ObjectOutputStream(socket.getOutputStream());
                while (running) {
                    Object[] data = (Object[]) in.readObject();
                    String type = (String) data[0];
                    switch (type) {
                        case "connect" -> {
                            String name = (String) data[1];
                            Float X = (Float) data[2];
                            Float Y = (Float) data[3];
                            Player newPlayer = (Player) EntityFactory.createEntity("player");
                            if (newPlayer == null) return;
                            newPlayer.setName(name);
                            newPlayer.setId(id);
                            newPlayer.setPos(X, Y);

                            ArrayList<Entity> entities = entityManager.getEntities();
                            if (entities != null) {
                                for (Entity entity : entityManager.getEntities()) {
                                    System.out.println("SERVER Enviando entidad + " + entity.typeId + " " + entity.id);
                                    send(Packet.newEntity(entity.typeId, entity.id));
                                    send(Packet.setPosEntity(entity.typeId, entity.id, entity.X, entity.Y));
                                }
                            }

                            sendAll(Packet.newEntity(newPlayer.typeId, newPlayer.id), id);
                            sendAll(Packet.setPosEntity(newPlayer.typeId, newPlayer.id, newPlayer.X, newPlayer.Y), id);
                            entityManager.addEntity(newPlayer);
                        }
                        case "disconnect" -> {
                            System.out.println("SERVER Usuario desconectado " + id);
                            sendAll(Packet.disconnect(id), id);
                            entityManager.removeEntity("player", id);
                            running = false;
                            users.remove(this);
                        }
                        case "setPosEntity" -> {
                            String typeId = (String) data[1];
                            Float X = (Float) data[3];
                            Float Y = (Float) data[4];
                            entityManager.setPosEntity(typeId, id, X, Y);

                            sendAll(Packet.setPosEntity(typeId, id, X, Y), id);
                        }
                    }

                }
            } catch (IOException e) {
                logger.log(Level.WARNING, "SERVER Falla al leer/escribir mensaje", e);
            } catch (ClassNotFoundException e) {
                logger.log(Level.WARNING, "SERVER Falla al leer/escribir mensaje (clase no encontrada)", e);
            }
        }

        public void send(Object[] data){
            try {
                out.writeObject(data);
            } catch (IOException e) {
                logger.log(Level.WARNING, "SERVER Falla al enviar mensaje", e);
            }
        }
    }
}
