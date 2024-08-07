package src.net;

import src.entities.Entity;
import src.entities.EntityFactory;
import src.pages.GameScreen;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client implements Runnable{
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    public String ip;
    public Integer port;
    private Boolean running = true;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private final GameScreen game;

    public Client(GameScreen game,String ip, Integer port) {
        this.ip = ip;
        this.port = port;
        this.game = game;
        try {
            socket = new Socket(ip, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e){
            logger.log(Level.WARNING, "CLIENT Falla al conectar al servidor", e);
        }
    }

    @Override
    public void run() {
        System.out.println("CLIENT en " + ip + ":" + port);
        try {
            while (running) {
                Object[] data = (Object[]) in.readObject();
                String type = (String) data[0];
                switch (type) {
                    case "newEntity" -> {
                        String typeId = (String) data[1];
                        Integer id = (Integer) data[2];

                        Entity entity = EntityFactory.createEntity(typeId, -100.0f, -100.0f);
                        if (entity == null) return;
                        entity.setId(id);
                        game.entityManager.addEntity(entity);
                    }
                    case "setPosEntity" -> {
                        String typeID = (String) data[1];
                        Integer id = (Integer) data[2];
                        Float x = (Float) data[3];
                        Float y = (Float) data[4];

                        if (!typeID.equals("player")) System.out.println("CLIENT Moviendo entidad " + typeID + " " + id + " a " + x + " " + y);
                        game.entityManager.setPosEntity(typeID, id, x, y);
                    }
                    case "disconnect" -> {
                        System.out.println("CLIENT jugador desconectado");
                        Integer id = (Integer) data[1];
                        game.entityManager.removeEntity("player", id);
                    }
                    case "serverClose" -> {
                        System.out.println("CLIENT Servidor cerrado");
                        running = false;
                        socket.close();
                    }
                }

            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "CLIENT Falla al recibir paquetes", e);
        }
    }

    public void send(Object[] data) {
        try {
            out.writeObject(data);
        } catch (IOException e) {
            logger.log(Level.WARNING, "CLIENT Falla al enviar paquetes", e);
        }
    }

    public void close(){
        if (!running) return;
        try {
            send(Packet.disconnect(-1));
            running = false;
            socket.close();
        } catch (IOException e) {
            logger.log(Level.WARNING, "CLIENT Falla al cerrar conexion", e);
        }
    }

    public Boolean isRunning() {
        return running;
    }
}
