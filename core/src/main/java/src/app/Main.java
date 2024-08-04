package src.app;

import src.net.Server;
import src.pages.*;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    public SpriteBatch batch;
    public ShapeRenderer shapeRenderer;
    public Skin skin;
    public Stage stage;
    private Screen[] pages;

    private final ExecutorService serverThread = Executors.newSingleThreadExecutor();;
    private Server server = null;

    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("./assets/ui/uiskin.json"));

        pages = new Screen[]{
            new Intro(),
            new MainMenu()
        };
        this.setScreen(pages[0]);
    }

    @Override
    public void render() {
        super.render();
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
