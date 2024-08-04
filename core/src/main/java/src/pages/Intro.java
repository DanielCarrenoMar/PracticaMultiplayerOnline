package src.pages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import src.app.Main;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import src.utils.Animation;

public class Intro implements Screen {
    Main main;
    Image logo;
    Float timer = 0f;

    public Intro(Main main) {
        this.main = main;
        Texture texture = new Texture("CubiTz.png");
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        logo = new Image(texture);
        logo.setHeight(520);
        logo.setWidth(520);
        logo.setPosition(280, 100);
        logo.setColor(1, 1, 1, 0.01f);
        main.stage.addActor(logo);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        AnyKeyDowm();

        ScreenUtils.clear(0f, 0f, 0f, 1f);
        main.stage.act();
        main.stage.draw();
        timer += delta;

        if (logo.getColor().a < 1) {
            float progress = Math.min(timer / 4, 1);
            float alpha = Animation.easeInOutCubic(progress);
            if (timer > 0.5f) {
                logo.setColor(1, 1, 1, alpha);
            }
        }else if (timer > 4.5f) {
            main.changePage(1);
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
    }

    public void AnyKeyDowm(){
        if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY) || Gdx.input.isTouched()) {
            main.changePage(1);
        }
    }
}
