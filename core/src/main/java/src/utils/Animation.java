package src.utils;

import com.badlogic.gdx.utils.FloatArray;
import org.w3c.dom.views.DocumentView;

public class Animation {

    public static Float easeInSine(Float progress) {
        return 1 - (float) Math.cos((progress * Math.PI) / 2);
    }
    public static Float easeOutSine(Float progress) {
        return (float) Math.sin((progress * Math.PI) / 2);
    }
    public static Float easeInOutSine(Float progress) {
        return -((float)Math.cos(Math.PI * progress) - 1) / 2;
    }
    public static Float easeInQuad(Float progress) {
        return progress * progress;
    }
    public static Float easeOutQuad(Float progress) {
        return 1 - (1 - progress) * (1 - progress);
    }
    public static Float easeInOutQuad(Float progress) {
        return progress < 0.5 ? 2 * progress * progress : 1 - (float) Math.pow(-2 * progress + 2, 2) / 2;
    }
    public static Float easeInCubic(Float progress) {
        return progress * progress * progress;
    }
    public static Float easeOutCubic(Float progress) {
        return 1 - (float) Math.pow(1 - progress, 3);
    }
    public static Float easeInOutCubic(Float progress) {
        return progress < 0.5 ? 4 * progress * progress * progress : 1 - (float) Math.pow(-2 * progress + 2, 3) / 2;
    }
}
