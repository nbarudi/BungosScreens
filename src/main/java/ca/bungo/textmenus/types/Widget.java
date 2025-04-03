package ca.bungo.textmenus.types;

import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;

public interface Widget {

    void draw();

    void cleanup();

    default TextDisplay createBasicDisplay(Location baseLocation) {
        TextDisplay display = baseLocation.getWorld().spawn(baseLocation, TextDisplay.class);
        display.text(Component.text(" "));
        display.setBillboard(Display.Billboard.FIXED);
        display.setPersistent(false);
        display.setAlignment(TextDisplay.TextAlignment.LEFT);
        display.setBackgroundColor(Color.BLACK);
        return display;
    }

    default TextDisplay createPersistentDisplay(Location baseLocation) {
        TextDisplay display = baseLocation.getWorld().spawn(baseLocation, TextDisplay.class);
        display.text(Component.text(" "));
        display.setBillboard(Display.Billboard.FIXED);
        display.setPersistent(true);
        display.setAlignment(TextDisplay.TextAlignment.LEFT);
        display.setBackgroundColor(Color.BLACK);
        return display;
    }

}
