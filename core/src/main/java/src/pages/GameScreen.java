package src.pages;


import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import src.app.Main;
import src.entities.Npc;
import src.net.Client;
import src.net.Packet;
import src.net.Server;
import src.ui.PauseMenu;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import src.entities.Entity;
import src.entities.EntityFactory;
import src.entities.Player;
import src.managers.EntityManager;
import src.managers.StructManager;
import src.managers.TiledmapManager;
import src.utils.ConvertColor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameScreen implements Screen {
    private final Main main;
    private final OrthographicCamera camera;
    private final OrthogonalTiledMapRenderer tiledRenderer;
    private final TiledmapManager tiledmapManager;
    public StructManager structManager = new StructManager();
    public EntityManager entityManager = new EntityManager();

    private final TextButton serverButton;
    private final Label label;
    private final Label dialog;
    private final Dialog texBox;

    private Player mainPlayer;
    private String namePlayer = "Daniel";

    private Client client = null;
    private Boolean isServer = false;
    private Server server = null;
    private final ExecutorService pool = Executors.newFixedThreadPool(3);

    public GameScreen(Main main) {
        this.main = main;
        this.camera = new OrthographicCamera();
        camera.setToOrtho(false, 300, 300);
        this.tiledmapManager = new TiledmapManager(this);
        this.tiledRenderer = tiledmapManager.setupMap();

        label = new Label("Server", main.skin);
        label.setBounds(100, 500, 300, 100);
        label.setVisible(false);

        this.serverButton = new TextButton("Server", main.skin);
        serverButton.setBounds(100, 400, 300, 100);
        serverButton.addListener(new ClickListener(
        ) {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                startServer();
            }
        });
        serverButton.setVisible(false);

        dialog = new Label("Hola", main.skin);
        dialog.setBounds(50, 0, 1280, 200);
        dialog.setFontScale(3);
        dialog.setVisible(false);

        texBox = new Dialog("Dialogo", main.skin);
        texBox.setBounds(0, 0, 1280, 200);
        texBox.setVisible(false);

        main.stage.addActor(serverButton);
        main.stage.addActor(label);
        main.stage.addActor(texBox);
        main.stage.addActor(dialog);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        keyHandler();
        checkCollision();
        ScreenUtils.clear(ConvertColor.colorFromHexString("FF472d3c"));

        label.setText("X: " + Math.round(mainPlayer.X) + " Y: " + Math.round(mainPlayer.Y));

        cameraUpdate();
        tiledRenderer.setView(camera);
        main.batch.setProjectionMatrix(camera.combined);

        tiledRenderer.render();
        mainPlayer.draw(main.batch);
        entityManager.drawEntities(main.batch);
        main.stage.act();
        main.stage.draw();

        entityManager.updateEntities(delta, structManager);

        if (!isServer) return;
        ArrayList<Entity> entities = entityManager.getEntities();
        if (entities == null) return;
        for (Entity entity : entities) {
            if (entity.getTypeId().equals("player")) continue;
            client.send(Packet.setPosEntity(entity.getTypeId(), entity.id, entity.X, entity.Y));
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        tiledRenderer.dispose();
        tiledmapManager.dispose();
        mainPlayer.dispose();

        if (server != null) server.close();
        if (client != null) client.close();
        pool.close();
    }

    public void createMainPlayer( Float X, Float Y) {
        mainPlayer = (Player) EntityFactory.createEntity("player",X, Y);
        if (mainPlayer == null) return;
        mainPlayer.setName(namePlayer);
    }

    public void keyHandler() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            Entity entity = entityManager.closestToRadius(mainPlayer.X, mainPlayer.Y, 30);
            if (entity != null){
                if (entity.getTypeId().startsWith("npc")){
                    Npc npc = (Npc) entity;
                    npc.startDialog(mainPlayer, dialog, texBox);
                }
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            serverButton.setVisible(!serverButton.isVisible());
            label.setVisible(!label.isVisible());
        }

        if (mainPlayer.getLock()) return;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            mainPlayer.move(180.);
            playerMoveSend();
        } else
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            mainPlayer.move(0.);
            playerMoveSend();
        } else
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            mainPlayer.move(90.);
            playerMoveSend();
        } else
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            mainPlayer.move(270.);
            playerMoveSend();
        }

    }

    public void checkCollision(){
        if (structManager.checkCollision(mainPlayer.collisionShape)){
            mainPlayer.revertPosition();
        }
    }

    public void cameraUpdate(){
        Vector3 position = camera.position;
        position.x = Math.round(mainPlayer.X);
        position.y = Math.round(mainPlayer.Y);
        camera.position.set(position);
        camera.update();
    }

    public void connectToServer(String ip, Integer port) {
        client = new Client(this, ip, port);
        pool.execute(client);
        client.send(Packet.connect(mainPlayer.getName(), mainPlayer.X, mainPlayer.Y));
    }

    public void startServer() {
        if (client != null) return;
        if (server != null) return;

        server = new Server(1234);
        pool.execute(server);
        connectToServer("localhost", 1234);
        ArrayList<Entity> entities = entityManager.getEntities();
        if (entities != null) {
            for (Entity entity : entityManager.getEntities()) {
                client.send(Packet.newEntity(entity.getTypeId(), entity.id));
                client.send(Packet.setPosEntity(entity.getTypeId(), entity.id, entity.X, entity.Y));
            }
        }
        isServer = true;
    }

    public void playerMoveSend() {
        if (client != null && client.isRunning()) client.send(Packet.setPosEntity(mainPlayer.getTypeId(), mainPlayer.id, mainPlayer.X, mainPlayer.Y));
    }
}
