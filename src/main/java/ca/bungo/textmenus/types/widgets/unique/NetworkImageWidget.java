package ca.bungo.textmenus.types.widgets.unique;

import ca.bungo.textmenus.TextMenusPlugin;
import ca.bungo.textmenus.types.widgets.generic.ImageWidget;
import ca.bungo.textmenus.utility.NetworkUtility;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.URISyntaxException;

public class NetworkImageWidget extends ImageWidget {

    private final String imageURL;

    private boolean isLoaded = false;
    private boolean loadAttempted = false;

    public NetworkImageWidget(Location baseLocation, String imageURL) {
        super(baseLocation, new Color[0][0]);

        this.imageURL = imageURL;

        loadImage();
    }

    private void loadImage() {
        try {
            NetworkUtility.getNetworkImage(this.imageURL).thenAccept((colors) -> {
                if (colors == null || colors.length == 0) return;
                buildPixelMap(colors);
                isLoaded = true;
                this.ROW_SIZE = colors.length;
                if(loadAttempted) {
                    Bukkit.getScheduler().runTask(JavaPlugin.getProvidingPlugin(this.getClass()), this::draw);
                }
            });
        } catch (URISyntaxException e) {
            TextMenusPlugin.LOGGER.warn("Failed to load image from {}", this.imageURL, e);
        }
    }

    @Override
    public void draw() {
        if(!isLoaded) {
            loadAttempted = true;
            return;
        }
        super.draw();
    }



}
