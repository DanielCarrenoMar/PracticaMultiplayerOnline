package src.pages;

import com.badlogic.gdx.utils.ScreenUtils;
import src.app.Main;

import com.badlogic.gdx.Screen;

public class MainMenu implements Screen {
    Main main;

    public MainMenu(Main main) {
        this.main = main;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.4f, 1f);
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
    }
}
