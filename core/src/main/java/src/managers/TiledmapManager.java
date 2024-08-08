package src.managers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import src.entities.EntityFactory;
import src.pages.GameScreen;
import src.struct.Struct;

public class TiledmapManager {
    private TiledMap tiledmap;
    private GameScreen game;

    public TiledmapManager(GameScreen game) {
        this.game = game;
    }

    public OrthogonalTiledMapRenderer setupMap() {
        tiledmap = new TmxMapLoader().load("maps/village.tmx");
        parsedEntityMap(tiledmap.getLayers().get("objects").getObjects());
        parsedColisionMap(tiledmap.getLayers().get("colision").getObjects());

        return new OrthogonalTiledMapRenderer(tiledmap);
    }

    public void parsedColisionMap(MapObjects objects) {
        for (MapObject object : objects) {
            Float X = (Float) object.getProperties().get("x");
            Float Y = (Float) object.getProperties().get("y");
            Float W = (Float) object.getProperties().get("width");
            Float H = (Float) object.getProperties().get("height");
            game.structManager.addStruct(new Struct(new Rectangle(X, Y, W, H)));
        }
    }

    public void parsedEntityMap(MapObjects objects) {

        for (MapObject object : objects) {
            String type = object.getName();
            if (type.equals("player")){
                createMainPlayer(object);
            }else{
                createEntity(type, object);
            }
        }

    }

    public void createMainPlayer(MapObject object) {
        Float X = (Float) object.getProperties().get("x");
        Float Y = (Float) object.getProperties().get("y");
        game.createMainPlayer( X, Y);
    }

    public void createEntity(String type, MapObject object) {
        Float X = (Float) object.getProperties().get("x");
        Float Y = (Float) object.getProperties().get("y");
        game.entityManager.addEntity(EntityFactory.createEntity(type, X, Y));
    }

    public void dispose() {
        tiledmap.dispose();
    }

    public void reMakeMap() {
        parsedEntityMap(tiledmap.getLayers().get("objects").getObjects());
        parsedColisionMap(tiledmap.getLayers().get("colision").getObjects());
    }
}
