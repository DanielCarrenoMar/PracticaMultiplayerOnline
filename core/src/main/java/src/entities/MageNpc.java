package src.entities;

import src.app.Main;

public class MageNpc extends Npc {
    public MageNpc(Float X, Float Y) {
        init(Main.mageTexture, "mage", "Mage", X, Y);
        setDialog("I am a mage");
    }
}
