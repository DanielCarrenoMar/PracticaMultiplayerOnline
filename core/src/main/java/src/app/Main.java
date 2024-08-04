package src.app;

import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import src.net.Server;
import src.pages.*;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    public SpriteBatch batch;
    public ShapeRenderer shapeRenderer;
    public Skin skin;
    public Stage stage;
    private Viewport viewport;
    private Screen[] pages;

    private final ExecutorService serverThread = Executors.newSingleThreadExecutor();;
    private Server server = null;

    @Override
    public void create() {
        Gdx.graphics.setWindowedMode(1080, 720);
        Gdx.graphics.setTitle("CubiTz");

        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("./assets/ui/uiskin.json"));
        viewport = new FitViewport(1080, 720);
        stage = new Stage(viewport);

        Gdx.input.setInputProcessor(stage);

        pages = new Screen[]{
            new Intro(this),
            new MainMenu(this)
        };
        this.setScreen(pages[0]);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        stage.dispose();
        skin.dispose();
        diaposeAllPages();
    }

    public void diaposeAllPages() {
        for (Screen page : pages) {
            page.dispose();
        }
    }

    public void changePage(Integer page) {
        this.setScreen(pages[page]);
    }

    public void startServer() {
        if (server == null) {
            server = new Server(1234);
            serverThread.execute(server);
        }
    }
}
