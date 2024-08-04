package src.pages;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import src.app.Main;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

public class Intro implements Screen {
    Main main;
    Image logo;

    public Intro(Main main) {
        this.main = main;
        Texture texture = new Texture("CubiTz.png");
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        logo = new Image(texture);
        logo.setHeight(400);
        logo.setWidth(400);
        main.stage.addActor(logo);
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
