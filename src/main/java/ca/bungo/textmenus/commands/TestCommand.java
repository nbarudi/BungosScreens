package ca.bungo.textmenus.commands;

import ca.bungo.textmenus.types.Widget;
import ca.bungo.textmenus.types.widgets.HorizontalPivot;
import ca.bungo.textmenus.types.widgets.VerticalPivot;
import ca.bungo.textmenus.types.widgets.generic.ImageWidget;
import ca.bungo.textmenus.types.widgets.generic.TextWidget;
import ca.bungo.textmenus.types.widgets.generic.shape.RectangleWidget;
import ca.bungo.textmenus.types.widgets.unique.NetworkImageWidget;
import ca.bungo.textmenus.types.widgets.unique.SkinWidget;
import ca.bungo.textmenus.utility.GlobalSettings;
import ca.bungo.textmenus.utility.NetworkUtility;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class TestCommand extends Command {

    List<Widget> widgets;

    public TestCommand() {
        super("test");
        widgets = new ArrayList<>();
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String @NotNull [] args) {
        if(!(sender instanceof Player player)) return false;
        if(args.length == 0) return false;

        Location baseLocation = player.getEyeLocation().add(player.getLocation().getDirection().multiply(2));
        baseLocation.setYaw(baseLocation.getYaw() - 180);

        if(args[0].equalsIgnoreCase("spawn")) {
            Color[][] colors = GlobalSettings.getDummyColors(64, 64);
            ImageWidget widget = new ImageWidget(baseLocation, colors);
            widgets.add(widget);
            widget.draw();
        }
        else if(args[0].equalsIgnoreCase("clean")) {
            for(Widget widget : widgets) {
                widget.cleanup();
            }
            widgets.clear();
        }
        else if(args[0].equalsIgnoreCase("skin")) {
            SkinWidget widget = new SkinWidget(baseLocation, player.getUniqueId().toString(), true);
            widgets.add(widget);
            widget.draw();
        }
        else if(args[0].equalsIgnoreCase("image")){
            if(args.length != 2) return false;
            String url = args[1];
            if(!url.startsWith("http")) return false;
            NetworkImageWidget widget = new NetworkImageWidget(baseLocation, url);
            widgets.add(widget);
            widget.draw();
        }
        else if(args[0].equalsIgnoreCase("text")){
            if(args.length < 2) return false;
            StringBuilder text = new StringBuilder();
            for(int i = 1; i < args.length; i++) {
                text.append(args[i]).append(" ");
            }

            String message = text.substring(0, text.length() - 1);

            TextWidget widget = new TextWidget(baseLocation, message);
            widgets.add(widget);

            widget.setBackgroundColor(Color.BLACK);
            widget.setTextColor(Color.YELLOW);
            widget.setTextRotation(45);
            widget.setBold(true);
            widget.setStrikethrough(true);
            widget.setTextSize(1);

            widget.draw();
        }
        else if(args[0].equalsIgnoreCase("rect")){
            RectangleWidget widget = new RectangleWidget(baseLocation, 10, 10);
            widgets.add(widget);
            widget.setHorizontalPivot(HorizontalPivot.LEFT);
            widget.setRectangleRotation(45);
            widget.draw();
        }


        return false;
    }
}
