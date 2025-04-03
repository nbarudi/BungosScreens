package ca.bungo.textmenus.utility;

import org.bukkit.Color;

import java.util.Random;

public class GlobalSettings {

    public static final float BASE_PIXEL_SIZE = 0.25f;

    public static Color[][] getDummyColors(int width, int height) {
        Color[] options = {Color.BLUE, Color.GREEN};
        Random rand = new Random(1000);
        Color[][] colors = new Color[width][height];
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                colors[x][y] = Color.fromRGB(
                        rand.nextInt(0, 255),
                        rand.nextInt(0, 255),
                        rand.nextInt(0, 255)
                );
                //colors[x][y] = Color.BLACK;
                //colors[x][y] = options[rand.nextInt(options.length)];
            }
        }
        return colors;
    }

}
