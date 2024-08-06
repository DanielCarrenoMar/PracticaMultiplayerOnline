package src.utils;

import com.badlogic.gdx.graphics.Color;

public class ConvertColor {
    public static Color colorFromHex(long hex)
    {
        float a = (hex & 0xFF000000L) >> 24;
        float r = (hex & 0xFF0000L) >> 16;
        float g = (hex & 0xFF00L) >> 8;
        float b = (hex & 0xFFL);

        return new Color(r/255f, g/255f, b/255f, a/255f);
    }

    /**
    * Convierte un texto hexadecimal a Color
    * Formato AARRGGBB
    */
    public static Color colorFromHexString(String s)
    {
        if (s.length() == 6) {
            s = "FF" + s;
        }

        if(s.startsWith("0x"))
            s = s.substring(2);

        if(s.length() != 8)
            throw new IllegalArgumentException("String must have the form AARRGGBB");

        return colorFromHex(Long.parseLong(s, 16));
    }
}
