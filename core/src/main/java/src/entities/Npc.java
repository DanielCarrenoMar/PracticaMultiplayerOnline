package src.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import src.managers.StructManager;

import java.util.Random;

public class Npc extends Entity{
    private final Random random = new Random();
    private final Vector2 goTo = new Vector2(0,0);
    private String dialogText = "Hola123";

    public Npc() {
        super("npc_", "None123", 100, 100, 16, 16, 0.4f);
    }

    public void init(Texture texture, String typeId, String name, Float X, Float Y) {
        setSprite(texture);
        setTypeId(getTypeId() + typeId);
        setName(name);
        setPos(X, Y);
    }

    public void setDialog(String dialog) {
        this.dialogText = dialog;
    }

    public void startDialog(Player player, Label dialog, Container<Label> textBox) {
        if (player.getLock()) {
            player.setLock(false);
            setLock(false);
            dialog.setVisible(false);
            textBox.setVisible(false);
            return;
        }

        player.setLock(true);
        setLock(true);
        dialog.setText(getName() + ": " + dialogText);
        dialog.setVisible(true);
        textBox.setVisible(true);
    }

    @Override
    public void update(float delta, StructManager structManager) {
        if (getLock()) return;

        if (!(X - 5 < goTo.x && goTo.x < X + 5 && Y - 5 < goTo.y && goTo.y < Y + 5) && !goTo.isZero()) {
            move(Math.toDegrees(Math.atan2(goTo.y - Y, goTo.x - X)));
        }

        time += delta;
        if (time < 5.0f) return;
        time = 0.0f;

        boolean isNotCollision = false;
        while (!(isNotCollision && goTo.x > 0 && goTo.y > 0)) {
            goTo.set(X + random.nextInt(-50,50), Y + random.nextInt(-50,50));
            isNotCollision = !structManager.checkCollision(new Rectangle(goTo.x, goTo.y, getWidth(), getHeight()));
        }

    }
}
