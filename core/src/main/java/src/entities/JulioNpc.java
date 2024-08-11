package src.entities;

import com.badlogic.gdx.graphics.Color;
import src.app.Main;
import src.utils.ConvertColor;

public class JulioNpc extends Npc {
    public JulioNpc(Float X, Float Y) {
        init(Main.julioTexture, "julio", "Julio", X, Y);
        this.setColor(Color.BLACK);
        setDialog("Recuerda aventurero, decir Nah i'd\n win antes de una pelea es como declarar tu derrota.\n(Me persigen los militares)");
    }
}
