package src.managers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import src.entities.*;

import java.util.ArrayList;

public class EntityManager {
    private final ArrayList<Entity> entities = new ArrayList<>();

    public EntityManager() {
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void removeEntity(String typeId,Integer id) {
        entities.removeIf(entity -> entity.id.equals(id) && entity.typeId.equals(typeId));
    }

    public void drawEntities(SpriteBatch batch) {
        for (Entity entity : entities) {
            entity.draw(batch);
        }
    }

    public void setPosEntity(String typeId, Integer id , Float X, Float Y) {
        for (Entity entity : entities) {
            if (entity.id.equals(id) && entity.typeId.equals(typeId)) {
                entity.setPos(X, Y);
            }
        }
    }

    public ArrayList<Entity> getEntities() {
        if (entities.isEmpty()) {
            return null;
        }
        return entities;
    }
}
