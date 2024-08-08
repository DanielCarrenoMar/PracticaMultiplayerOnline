package src.entities;

import src.app.Main;
import src.utils.ConvertColor;

public class JuanNpc extends Npc {
    public JuanNpc(Float X, Float Y) {
        init(Main.juanTexture, "juan", "Juan David", X, Y);
        setColor(ConvertColor.colorFromHexString("FF8AD609"));
        setDialog("Te voy a robar el oro");
    }
}
