package src.pages;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
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
import java.util.concurrent.CopyOnWriteArrayList;
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
    private final Label tutorial;
    private final Label dialog;
    private final Container<Label> textBox = new Container<>();
    private Float tickTime = 0.0f;

    public Player mainPlayer;
    private String namePlayer = "Jugador principal";

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

        tutorial = new Label("Presiona E para hablar con los NPC.\nESC para abrir el menu", main.skin);
        tutorial.setBounds(10, 640, 600, 100);
        tutorial.setVisible(false);

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
        dialog.setBounds(0, 0, 500, 200);
        dialog.setFontScale(2.5f);
        dialog.setVisible(false);
        textBox.setActor(dialog);

        textBox.setBounds(0, 0, 1280, 200);
        textBox.setColor(ConvertColor.colorFromHexString("FF16151D"));
        textBox.setBackground(main.skin.getDrawable("progress-bar-square-knob"));
        textBox.left();
        textBox.padLeft(20);
        textBox.setVisible(false);

        main.stage.addActor(serverButton);
        main.stage.addActor(tutorial);
        main.stage.addActor(label);
        main.stage.addActor(textBox);
    }

        @Override
    public void show() {
            tutorial.setVisible(true);
    }

    @Override
    public void render(float delta) {
        tickTime += delta;
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

        if (client != null) client.send(Packet.setPosPlayer(mainPlayer.X, mainPlayer.Y));

        if (client == null) entityManager.updateEntities(delta, structManager);
        if (!isServer || tickTime < 0.1f) return;
        server.sendAll(Packet.setPosEntity(mainPlayer.getTypeId(), mainPlayer.id, mainPlayer.X, mainPlayer.Y),-1);
        CopyOnWriteArrayList<Entity> entities = entityManager.getEntities();
        if (entities == null) return;
        for (Entity entity : entities) {
            server.sendAll(Packet.setPosEntity(entity.getTypeId(), entity.id, entity.X, entity.Y), -1);
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
        tutorial.setVisible(false);
    }

    @Override
    public void dispose() {
        tiledRenderer.dispose();
        tiledmapManager.dispose();
        mainPlayer.dispose();

        if (server != null) server.close();
        if (client != null && client.isRunning()) client.close();
        pool.shutdown();
    }

    public void backToMenu() {
        main.changePage(1);
    }

    public void resetGame() {
        entityManager.removeAll();
        tiledmapManager.reMakeMap();
    }

    public void createMainPlayer( Float X, Float Y) {
        mainPlayer = (Player) EntityFactory.createEntity("player",X, Y);
        if (mainPlayer == null) return;
        mainPlayer.setId(0);
        mainPlayer.setName(namePlayer);
    }

    public void keyHandler() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            Entity entity = entityManager.closestToRadius(mainPlayer.X, mainPlayer.Y, 30);
            if (entity != null){
                if (entity.getTypeId().startsWith("npc")){
                    Npc npc = (Npc) entity;
                    npc.startDialog(mainPlayer, dialog, textBox);
                    if (client != null) {
                        client.send(Packet.changeLockEntity(npc.getTypeId(), npc.id, npc.getLock()));}
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
        } else
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            mainPlayer.move(0.);
        } else
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            mainPlayer.move(90.);
        } else
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            mainPlayer.move(270.);
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
        if (client != null) return;
        client = new Client(this, ip, port);
        pool.execute(client);
        client.send(Packet.connect(mainPlayer.getName(), mainPlayer.X, mainPlayer.Y));
        entityManager.removeAll();
    }

    public void startServer() {
        if (client != null) return;
        if (server != null) return;
        isServer = true;
        server = new Server(1234, this);
        pool.execute(server);
        serverButton.setColor(Color.GREEN);
        tutorial.setText(tutorial.getText()+"\nSERVER ABIERTO EN PUERTO" + server.port);
        //entityManager.addEntityNoId(mainPlayer);
    }
}
