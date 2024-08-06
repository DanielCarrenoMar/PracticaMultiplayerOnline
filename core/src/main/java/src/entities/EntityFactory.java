package src.entities;

import java.util.Dictionary;

public class EntityFactory {

    public static Entity createEntity(String typeId) {
        if (typeId.equals("player")) {
            return new Player();
        }
        return null;
    }
}
