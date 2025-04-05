package ca.bungo.textmenus.utility;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BoundingBox;

public class VisualsUtility {

    public static void asyncDrawBoundingBox(World world, BoundingBox box, double spacing, Particle particle, int particleCount) {
        for (double x = box.getMinX(); x <= box.getMaxX(); x += spacing) {
            for (double y = box.getMinY(); y <= box.getMaxY(); y += spacing) {
                for (double z = box.getMinZ(); z <= box.getMaxZ(); z += spacing) {
                    boolean onEdge =
                            ((Math.abs(x - box.getMinX()) < spacing || Math.abs(x - box.getMaxX()) < spacing) ? 1 : 0) +
                                    ((Math.abs(y - box.getMinY()) < spacing || Math.abs(y - box.getMaxY()) < spacing) ? 1 : 0) +
                                    ((Math.abs(z - box.getMinZ()) < spacing || Math.abs(z - box.getMaxZ()) < spacing) ? 1 : 0) >= 2;

                    if (onEdge) {
                        Location loc = new Location(world, x, y, z);
                        Bukkit.getScheduler().runTask(JavaPlugin.getProvidingPlugin(VisualsUtility.class), () -> world.spawnParticle(particle, loc, particleCount, 0, 0, 0, 0));
                    }
                }
            }
        }
    }

}
