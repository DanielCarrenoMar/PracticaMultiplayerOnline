package src.app;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import src.pages.*;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    public SpriteBatch batch;
    public ShapeRenderer shapeRenderer;
    public Skin skin;
    public Stage stage;
    private Viewport viewport;
    private Screen[] pages;

    public static Texture playerTexture;
    public static Texture mageTexture;

    @Override
    public void create() {
        Gdx.graphics.setWindowedMode(1080, 720);
        Gdx.graphics.setTitle("CubiTzCrossing");

        Main.playerTexture = new Texture("sprites/player/playerState.png");
        Main.mageTexture = new Texture("sprites/mage.png");

        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("./assets/ui/uiskin.json"));
        viewport = new FitViewport(1080, 720);
        stage = new Stage(viewport);

        Gdx.input.setInputProcessor(stage);

        pages = new Screen[]{
            new IntroScreen(this),
            new MenuScreen(this),
            new GameScreen(this)
        };
        this.setScreen(pages[1]);
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

    public Screen getPage(String page) {
        for (Screen p : pages) {
            if (p.getClass().getSimpleName().equals(page)) {
                return p;
            }
        }
        return null;
    }

    public void changePage(Integer page) {
        this.setScreen(pages[page]);
    }
}
