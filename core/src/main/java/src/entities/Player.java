package src.entities;

import src.app.Main;

public class Player extends  Entity {
    private Float previousX;
    private Float previousY;

    public Player() {
        super(Main.playerTexture,"player", "None123", 100, 100, 16, 16, 1.0f);
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void move(Double degree) {
        previousX = X;
        previousY = Y;
        super.move(degree);
    }

    public void revertPosition() {
        setPos(previousX, previousY);
    }
}
