package src.net;

import src.entities.Entity;
import src.entities.EntityFactory;
import src.entities.Player;
import src.managers.EntityManager;
import src.pages.GameScreen;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server implements Runnable{
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    public Integer port;
    public String ip;
    private ServerSocket serverSocket;
    private Boolean running = false;
    private final ExecutorService pool = Executors.newFixedThreadPool(10);
    private final GameScreen game;

    private static final CopyOnWriteArrayList<User> users = new CopyOnWriteArrayList<User>();

    public Server(Integer port, GameScreen game) {
        this.game = game;
        this.port = port;
        try {
            this.serverSocket = new ServerSocket(port);
            this.ip = serverSocket.getInetAddress().getHostAddress();
            running = true;
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
                User newUser = new User(clientSocket, users.size()+1);
                pool.execute(newUser);
                users.add(newUser);
            }
        }catch (IOException e){
            logger.log(Level.WARNING, "Falla al aceptar la conexion", e);
        }
    }

    public void sendAll(Object[] data, Integer id){
        synchronized (users) {
            for (User user : users) {
                if (!user.running || user.id.equals(id)) continue;
                user.send(data);
            }
        }
    }

    public void close(){
        running = false;
        try {
            synchronized (users) {
                for (User user : users) {
                    user.running = false;
                    user.send(Packet.serverClose());
                }
            }
            serverSocket.close();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Falla al cerrar el servidor", e);
        }
        pool.shutdown();
    }

    private class User implements Runnable{
        private final Socket socket;
        private final Integer id;
        private Boolean running = false;
        private ObjectOutputStream out;

        private User(Socket socket, Integer id) {
            this.socket = socket;
            this.id = id;
        }

        public void close(){
            if (!running) return;
            System.out.println("SERVER Usuario desconectado " + id);
            sendAll(Packet.disconnect(id), id);
            game.entityManager.removeEntity("player", id);
            running = false;
            users.remove(this);
            try {
                socket.close();
            } catch (IOException e) {
                logger.log(Level.WARNING, "Falla al cerrar el socket", e);
            }
        }

        @Override
        public void run() {
            System.out.println("SERVER Nuevo usuario conectado " + id);
            try {
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                out = new ObjectOutputStream(socket.getOutputStream());
                running = true;
                while (running) {
                    Object[] data = (Object[]) in.readObject();
                    String type = (String) data[0];
                    //System.out.println("SERVER Recibido " + type);
                    switch (type) {
                        case "connect" -> {
                            String name = (String) data[1];
                            Float X = (Float) data[2];
                            Float Y = (Float) data[3];
                            Player newPlayer = (Player) EntityFactory.createEntity("player", X, Y);
                            if (newPlayer == null) return;
                            newPlayer.setName(name);
                            newPlayer.setId(id);

                            CopyOnWriteArrayList<Entity> entities = game.entityManager.getEntities();
                            if (entities != null) {
                                for (Entity entity : entities) {
                                    System.out.println("SERVER Enviando entidad + " + entity.getTypeId() + " " + entity.id);
                                    send(Packet.newEntity(entity.getTypeId(), entity.id, entity.getName()));
                                    send(Packet.setPosEntity(entity.getTypeId(), entity.id, entity.X, entity.Y));
                                }
                            }
                            send(Packet.newEntity(game.mainPlayer.getTypeId(), game.mainPlayer.id, game.mainPlayer.getName()));

                            sendAll(Packet.newEntity(newPlayer.getTypeId(), newPlayer.id, newPlayer.getName()), id);
                            sendAll(Packet.setPosEntity(newPlayer.getTypeId(), newPlayer.id, newPlayer.X, newPlayer.Y), id);
                            game.entityManager.addEntityNoId(newPlayer);
                        }
                        case "disconnect" -> {
                            close();
                        }
                        case "setPosPlayer" -> {
                            Float X = (Float) data[1];
                            Float Y = (Float) data[2];
                            game.entityManager.setPosEntity("player", id, X, Y);

                            sendAll(Packet.setPosEntity("player", id, X, Y), id);
                        }
                        case "changeLockEntity" ->{
                            String typeID = (String) data[1];
                            Integer id = (Integer) data[2];
                            Boolean lock = (Boolean) data[3];
                            CopyOnWriteArrayList<Entity> entities = game.entityManager.getEntities();
                            if (entities != null) {
                                for (Entity entity : entities) {
                                    if (entity.id.equals(id) && entity.getTypeId().equals(typeID)) {
                                        entity.setLock(lock);
                                    }
                                }
                            }
                        }
                    }

                }
            } catch (IOException e) {
                logger.log(Level.WARNING, "SERVER Falla al leer/escribir mensaje", e);
            } catch (ClassNotFoundException e) {
                logger.log(Level.WARNING, "SERVER Falla al leer/escribir mensaje (clase no encontrada)", e);
            } finally {
                close();
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
