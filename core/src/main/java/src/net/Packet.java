package src.net;

import com.badlogic.gdx.graphics.Color;
import src.entities.Entity;

public class Packet{

    public static Object[] connect( String name,Float X, Float Y) {
        return new Object[] {"connect", name, X, Y};
    }

    public static Object[] newEntity (String typeId, Integer id) {
        return new Object[] {"newEntity", typeId, id};
    }

    public static Object[] setPosEntity (String typeId,Integer id, Float X, Float Y) {
        return new Object[] {"setPosEntity", typeId, id, X, Y};
    }

    public static Object[] disconnect(Integer id) {
        return new Object[] {"disconnect", id};
    }

    public static  Object[] serverClose() {
        return new Object[] {"serverClose"};
    }
}
