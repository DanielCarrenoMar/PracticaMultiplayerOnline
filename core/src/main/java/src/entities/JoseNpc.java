package src.entities;

import src.app.Main;
import src.utils.ConvertColor;

public class JoseNpc extends Npc {
    public JoseNpc(Float X, Float Y) {
        init(Main.joseTexture, "jose", "Jose", X, Y);
        setColor(ConvertColor.colorFromHexString("FF26B537"));
        setDialog("Era un aventurero como tu, hasta\n que un dia recibi un flechazo en la rodilla");
    }
}
