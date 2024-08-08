package src.entities;

import src.app.Main;

public class DanielNpc extends Npc {
    public DanielNpc(Float X, Float Y) {
        init(Main.danielTexture, "daniel", "Daniel", X, Y);
        setDialog("Asi es soy yo, el CEO de CubiTz. ¿Que te parece el juego?");
    }
}
