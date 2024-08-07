package src.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import src.app.Main;
import src.managers.StructManager;

import java.io.Serializable;

public class Entity implements Serializable {
    private String typeId, name;
    public Integer id = -1;
    private Integer health, maxHealth;
    public Float X = 0.0f;
    public Float Y = 0.0f;
    public Integer Z = 0;
    private Float speed = 1.0f;
    private Integer height = 32;
    private Integer width = 32;
    public Rectangle collisionShape;
    private Boolean lock = false;

    private Sprite sprite;

    public Float time = 0.0f;

    public Entity(String typeId, String name, Integer health, Integer maxHealth, Integer height, Integer width, Float speed) {
        this.sprite = new Sprite(Main.playerTexture);
        sprite.setBounds(X, Y, width, height);
        this.typeId = typeId;
        this.name = name;
        this.health = health;
        this.maxHealth = maxHealth;
        this.height = height;
        this.width = width;
        this.speed = speed;
        this.collisionShape = new Rectangle(X+5, Y+5, width-10, height-10);
    }

    public void setLock(Boolean lock) {
        this.lock = lock;
    }
    public Boolean getLock() {
        return lock;
    }

    public String getTypeId() {
        return typeId;
    }
    public String getName() {
        return name;
    }
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Integer getWidth() {
        return width;
    }
    public Integer getHeight() {
        return height;
    }

    public void setSprite(Texture texture) {
        this.sprite = new Sprite(texture);
        sprite.setBounds(X, Y, width, height);
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPos(Float X, Float Y, Integer Z) {
        this.X = X;
        this.Y = Y;
        this.Z = Z;
        collisionShape.setPosition(X+5, Y+5);
    }
    public void setPos(Float X, Float Y) {
        this.X = X;
        this.Y = Y;
        collisionShape.setPosition(X+5, Y+5);
    }

    public void setHealth(Integer health) {
        this.health = health;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }

    public void move(Double degree) {
        Double valueX = Math.cos(Math.toRadians(degree)) * speed;
        Double valueY = Math.sin(Math.toRadians(degree)) * speed;
        X += valueX.floatValue();
        Y += valueY.floatValue();
        collisionShape.setPosition(X+5, Y+5);
    }

    public void draw(SpriteBatch batch) {
        if (sprite == null) return;
        batch.begin();
        batch.draw(sprite, X, Y, width, height);
        batch.end();
    }

    public void update(float delta, StructManager colision){}

    public void dispose() {
        sprite.getTexture().dispose();
    }
}
