package src.entities;

import com.badlogic.gdx.graphics.Color;
import src.app.Main;
import src.utils.ConvertColor;

public class JulioNpc extends Npc {
    public JulioNpc(Float X, Float Y) {
        init(Main.julioTexture, "julio", "Julio", X, Y);
        this.setColor(ConvertColor.colorFromHexString("FFCFCFCF"));
        setDialog("Me persigen los militares");
    }
}
