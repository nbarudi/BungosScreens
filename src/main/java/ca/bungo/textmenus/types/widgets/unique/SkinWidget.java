package ca.bungo.textmenus.types.widgets.unique;

import ca.bungo.textmenus.TextMenusPlugin;
import ca.bungo.textmenus.types.widgets.generic.ImageWidget;
import ca.bungo.textmenus.utility.NetworkUtility;
import ca.bungo.textmenus.utility.PixelUtility;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.URISyntaxException;
import java.util.Arrays;


public class SkinWidget extends ImageWidget {

    private final String playerUUID;
    private final boolean fullBody;
    private boolean isLoaded = false;
    private boolean loadAttempted = false;

    private static final Color[][] dummyColors = new Color[64][64];

    static {
        Color transparent = Color.fromARGB(0,0,0,0);
        for(int i = 0; i < 64; i++) {
            for(int j = 0; j < 64; j++) {
                dummyColors[i][j] = transparent;
            }
        }
    }

    public SkinWidget(Location baseLocation, String playerUUID) {
        super(baseLocation, dummyColors);
        this.playerUUID = playerUUID;
        this.fullBody = false;
        loadSkin();
    }

    public SkinWidget(Location baseLocation, String playerUUID, boolean fullBody) {
        super(baseLocation, dummyColors);
        this.playerUUID = playerUUID;
        this.fullBody = fullBody;
        loadSkin();
    }

    private void loadSkin() {
        try {
            NetworkUtility.getPlayerSkin(playerUUID).thenAccept((colors) -> {

                boolean isSlim = colors[55][54].getAlpha() == 0;

                if(!fullBody){
                    Color[][] head = PixelUtility.extractRegion(colors, 8, 48, 8, 8);
                    buildPixelMap(head);
                    if(loadAttempted) Bukkit.getScheduler().runTask(JavaPlugin.getProvidingPlugin(this.getClass()), this::draw);
                }
                else {
                    Color[][] fullBody = new Color[16][32];
                    Color transparent = Color.fromARGB(0,0,0,0);
                    for(int i = 0; i < 16; i++) {
                        for(int j = 0; j < 32; j++) {
                            fullBody[i][j] = transparent;
                        }
                    }

                    Color[][] head = PixelUtility.extractRegion(colors, 8, 48, 8, 8);
                    Color[][] body = PixelUtility.extractRegion(colors, 20, 32, 8, 12);

                    placeAt(fullBody, head, 4, 24, 8, 8);
                    placeAt(fullBody, body, 4, 12, 8, 12);

                    Color[][] lLeg = PixelUtility.extractRegion(colors, 20, 0, 4, 12);
                    Color[][] rLeg = PixelUtility.extractRegion(colors, 4, 32, 4, 12);

                    placeAt(fullBody, rLeg, 4, 0, 4, 12);
                    placeAt(fullBody, lLeg, 8, 0, 4, 12);

                    if(isSlim){
                        Color[][] lArm = PixelUtility.extractRegion(colors, 36, 0, 3, 12);
                        Color[][] rArm = PixelUtility.extractRegion(colors, 44, 32, 3, 12);

                        placeAt(fullBody, rArm, 1, 12, 3, 12);
                        placeAt(fullBody, lArm, 12, 12, 3, 12);
                    }

                    else {
                        Color[][] lArm = PixelUtility.extractRegion(colors, 36, 0, 4, 12);
                        Color[][] rArm = PixelUtility.extractRegion(colors, 44, 32, 4, 12);

                        placeAt(fullBody, rArm, 0, 12, 4, 12);
                        placeAt(fullBody, lArm, 12, 12, 4, 12);
                    }



                    buildPixelMap(fullBody);

                    if(loadAttempted) Bukkit.getScheduler().runTask(JavaPlugin.getProvidingPlugin(this.getClass()), this::draw);
                }

                isLoaded = true;
            });
        } catch (URISyntaxException e) {
            TextMenusPlugin.LOGGER.warn("Skin widget could not be loaded.");
        }
    }

    private void placeAt(Color[][] dest, Color[][] src, int x, int y, int srcWidth, int srcHeight) {
        for(int xPos = 0; xPos < srcWidth; xPos++) {
            for(int yPos = 0; yPos < srcHeight; yPos++) {
                dest[x + xPos][y + yPos] = src[xPos][yPos];
            }
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
