package src.entities;

import src.app.Main;
import src.utils.ConvertColor;

public class MageNpc extends Npc {
    public MageNpc(Float X, Float Y) {
        init(Main.mageTexture, "mage", "El Magito", X, Y);
        setColor(ConvertColor.colorFromHexString("FF8440E6"));
        setDialog("Solia ser el hechicero, hasta que\n el pato lucas me robo el titulo");
    }
}
