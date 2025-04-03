package ca.bungo.textmenus.types.widgets.generic;

import ca.bungo.textmenus.TextMenusPlugin;
import ca.bungo.textmenus.types.Widget;
import ca.bungo.textmenus.utility.GlobalSettings;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.TextDisplay;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class ImageWidget implements Widget {

    public class Pixel {
        private final int x,y,width;
        private final Color color;

        private TextDisplay textDisplay;
        private boolean spawned = false;

        public Pixel(int x, int y, int width, Color color) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.color = color;
        }

        public void spawnPixel(Location baseLocation) {
            TextDisplay display = ImageWidget.this.createBasicDisplay(baseLocation);
            display.setBackgroundColor(color);

            float pixelSize = GlobalSettings.BASE_PIXEL_SIZE;

            float posX = x*pixelSize/(8) + (width*pixelSize/20f);
            float posY = y*pixelSize/(8); //I honestly dont know why these magic numbers work. But they made everything aligned

            Transformation transformation = new Transformation(
                    new Vector3f(posX, posY, 0),
                    new AxisAngle4f(),
                    new Vector3f(width*pixelSize, pixelSize/2, pixelSize),
                    new AxisAngle4f()
            );

            display.setTransformation(transformation);
            textDisplay = display;
            spawned = true;
        }

        public void cleanPixel() {
            if(!spawned) return;
            textDisplay.remove();
            textDisplay = null;
        }
    }

    private final List<Pixel> pixels;
    private final Location baseLocation;
    protected int ROW_SIZE;

    public ImageWidget(Location baseLocation, Color[][] colors) {
        pixels = new ArrayList<>();
        this.baseLocation = baseLocation;
        this.ROW_SIZE = colors.length;
        buildPixelMap(colors);
    }

    public void buildPixelMap(Color[][] colors) {
        pixels.clear();
        int width = colors.length;
        if(width == 0) return;
        int height = colors[0].length;

        for(int y = 0; y < height; y++) {
            int x = 0;
            while(x < width) {
                Color color = colors[x][y];
                int startX = x;
                while(x < width && colors[x][y].equals(color)) {
                    x++;
                }
                Pixel pixel = new Pixel(startX, y, x - startX, color);
                pixels.add(pixel);
            }
        }
    }

    @Override
    public void draw() {
        cleanup();
        new BukkitRunnable() {
            int index = 0;
            @Override
            public void run() {
                int count = 0;
                while(count++ < ROW_SIZE && index < pixels.size()){
                    Pixel pixel = pixels.get(index);
                    if(pixel.color.getAlpha() == 0) {
                        index++;
                        continue;
                    }
                    Bukkit.getScheduler().runTask(JavaPlugin.getProvidingPlugin(TextMenusPlugin.class), () -> pixel.spawnPixel(baseLocation));
                    index++;
                }

                if(index >= pixels.size()) this.cancel();
            }
        }.runTaskTimerAsynchronously(JavaPlugin.getProvidingPlugin(this.getClass()), 0, 2);
    }

    @Override
    public void cleanup() {
        for(Pixel pixel : pixels) {
            pixel.cleanPixel();
        }
    }
}
