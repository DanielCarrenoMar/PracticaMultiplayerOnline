package src.pages;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import src.app.Main;

import com.badlogic.gdx.Screen;

public class MenuScreen implements Screen {
    private Main main;
    private Table table = new Table();
    private TextButton playButton;
    private TextButton playConnectButton;
    private TextField ipField;

    public MenuScreen(Main main) {
        this.main = main;
        table.setFillParent(true);

        playButton = new TextButton("Play", main.skin);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                main.changePage(2);
            }
        });

        ipField = new TextField("localhost:1234", main.skin);
        ipField.setMaxLength(45);

        playConnectButton = new TextButton("Conectarse a servidor", main.skin);
        playConnectButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                GameScreen page = (GameScreen) main.getPage("GameScreen");
                String[] connet = new String[]{"localhost", "1234"};
                if (ipField.getText().contains(":")) {
                    connet = ipField.getText().split(":");
                }
                page.connectToServer(connet[0], Integer.parseInt(connet[1]));
                main.changePage(2);
            }
        });


        table.add(playButton).width(300).height(75).expandX();
        table.row();
        table.add(playConnectButton).width(300).height(75).expandX();
        table.row();
        table.add(ipField).width(300).height(75).expandX();

        main.stage.addActor(table);
    }

    @Override
    public void show() {
        table.setVisible(true);
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
        table.setVisible(false);
    }

    @Override
    public void dispose() {
    }
}
