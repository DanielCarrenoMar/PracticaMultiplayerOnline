package src.managers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import src.entities.*;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class EntityManager {
    private final CopyOnWriteArrayList<Entity> entities = new CopyOnWriteArrayList<>();

    public EntityManager() {
    }

    public void removeAll() {
        entities.clear();
    }

    public void addEntity(Entity entity) {
        if (entity == null) {
            System.out.println("No se puede agregar entidad Null");
            return;
        }
        entity.id = entities.size()+1;
        entities.add(entity);
    }
    public void addEntityNoId(Entity entity) {
        if (entity == null) {
            System.out.println("No se puede agregar entidad Null");
            return;
        }
        entities.add(entity);
    }

    public void removeEntity(String typeId,Integer id) {
        entities.removeIf(entity -> entity.id.equals(id) && entity.getTypeId().equals(typeId));
    }

    public void updateEntities(float delta, StructManager colision) {
        for (Entity entity : entities) {
            entity.update(delta, colision);
        }
    }

    public void drawEntities(SpriteBatch batch) {
        synchronized (entities) {
            for (Entity entity : entities) {
                entity.draw(batch);
            }
        }
    }

    public void setPosEntity(String typeId, Integer id , Float X, Float Y) {
        for (Entity entity : entities) {
            if (entity.id.equals(id) && entity.getTypeId().equals(typeId)) {
                entity.setPos(X, Y);
            }
        }
    }

    public CopyOnWriteArrayList<Entity> getEntities() {
        if (entities.isEmpty()) {
            return null;
        }
        return entities;
    }

    public Entity closestToRadius(Float X, Float Y, Integer radius) {
        float minDistance = Float.MAX_VALUE;
        Entity closestEntity = null;
        for (Entity entity : entities) {
            float distance = (float) Math.sqrt(Math.pow(entity.X - X, 2) + Math.pow(entity.Y - Y, 2));
            if (distance < minDistance && distance < radius) {
                minDistance = distance;
                closestEntity = entity;
            }
        }
        return closestEntity;
    }
}
