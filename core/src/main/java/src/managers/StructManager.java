package src.managers;

import com.badlogic.gdx.math.Rectangle;
import src.struct.Struct;

import java.util.ArrayList;

public class StructManager {
    private ArrayList<Struct> structs = new ArrayList<>();

    public StructManager() {
    }

    public void addStruct(Struct struct) {
        structs.add(struct);
    }

    public Boolean checkCollision(Rectangle rect) {
        for (Struct struct : structs) {
            if (struct.colisionShape.overlaps(rect)) {
                return true;
            }
        }
        return false;
    }

}
