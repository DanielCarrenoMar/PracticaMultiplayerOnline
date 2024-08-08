package src.net;

import com.badlogic.gdx.graphics.Color;
import src.entities.Entity;

public class Packet{

    public static Object[] connect( String name,Float X, Float Y) {
        return new Object[] {"connect", name, X, Y};
    }

    public static Object[] setPosPlayer ( Float X, Float Y) {
        return new Object[] {"setPosPlayer", X, Y};
    }

    public static Object[] newEntity (String typeId, Integer id, String name) {
        return new Object[] {"newEntity", typeId, id, name};
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

    public static Object[] changeLockEntity(String typeId,Integer id, Boolean lock) {
        return new Object[] {"changeLockEntity", typeId, id, lock};
    }
}
