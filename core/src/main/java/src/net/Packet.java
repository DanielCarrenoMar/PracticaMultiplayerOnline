package src.net;

import com.badlogic.gdx.graphics.Color;

public class Packet{

    public Object[] connect(Integer X, Integer Y) {
        return new Object[] {"PacketConnect", X, Y};
    }

    public Object[] clientId(Integer id) {
        return new Object[] {"ClientId", id};
    }

    public Object[] playerConnect (Integer id, Color color) {
        return new Object[] {"PacketPlayerConnect", id, color.r, color.g, color.b};
    }

    public Object[] player (Integer id, Integer X, Integer Y) {
        return new Object[] {"PacketPlayer", id, X, Y};
    }

    public Object[] disconnect(Integer id) {
        return new Object[] {"PacketDisconnect", id};
    }
}
