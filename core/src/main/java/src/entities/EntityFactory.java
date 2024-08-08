package src.entities;

import java.util.Dictionary;

public class EntityFactory {

    public static Entity createEntity(String typeId, Float X, Float Y) {
        return switch (typeId) {
            case "npc_mage" -> new MageNpc(X, Y);
            case "npc_jose" -> new JoseNpc(X, Y);
            case "npc_juan" -> new JuanNpc(X, Y);
            case "npc_julio" -> new JulioNpc(X, Y);
            case "npc_daniel" -> new DanielNpc(X, Y);
            case "player" -> new Player(X, Y);
            default -> null;
        };
    }
}
