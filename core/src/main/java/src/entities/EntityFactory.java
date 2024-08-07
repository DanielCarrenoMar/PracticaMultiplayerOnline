package src.entities;

import java.util.Dictionary;

public class EntityFactory {

    public static Entity createEntity(String typeId, Float X, Float Y) {
        return switch (typeId) {
            case "npc_mage" -> new MageNpc(X, Y);
            case "player" -> new Player(X, Y);
            default -> null;
        };
    }
}
