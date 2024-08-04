package src.pages;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import src.app.Main;

import com.badlogic.gdx.Screen;

public class MenuMain implements Screen {
    private Main main;
    TextButton playButton;

    public MenuMain(Main main) {
        this.main = main;
        playButton = new TextButton("Play", main.skin);
        playButton.setBounds(100, 400, 300, 100);
        main.stage.addActor(playButton);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.4f, 1f);
        main.stage.act();
        main.stage.draw();
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
