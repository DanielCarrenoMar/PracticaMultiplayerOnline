package src.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import src.app.Main;

public class PauseMenu{
    private Main main;
    private TextButton resumeButton;
    private TextButton ServerButton;

    public PauseMenu(Main main) {
        this.main = main;
        this.resumeButton = new TextButton("Resume", main.skin);
        resumeButton.setBounds(100, 400, 300, 100);
        resumeButton.addAction(new RunnableAction(){
            @Override
            public void run(){
                hide();
            }
        });

        this.ServerButton = new TextButton("Server", main.skin);
        ServerButton.setBounds(100, 200, 300, 100);
        /*ServerButton.addAction(new RunnableAction(){
            @Override
            public void run(){
                main.startServer();
            }
        });*/

        main.stage.addActor(resumeButton);
        main.stage.addActor(ServerButton);
    }

    public void show() {
        resumeButton.setVisible(true);
        ServerButton.setVisible(true);
    }

    public void hide() {
        resumeButton.setVisible(false);
        ServerButton.setVisible(false);
    }
}
