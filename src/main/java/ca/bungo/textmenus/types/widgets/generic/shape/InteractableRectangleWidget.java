package ca.bungo.textmenus.types.widgets.generic.shape;

import ca.bungo.textmenus.TextMenusPlugin;
import ca.bungo.textmenus.types.Interactable;
import ca.bungo.textmenus.types.widgets.HorizontalPivot;
import ca.bungo.textmenus.types.widgets.VerticalPivot;
import ca.bungo.textmenus.utility.RaycastUtility;
import ca.bungo.textmenus.utility.VisualsUtility;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Transformation;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class InteractableRectangleWidget extends RectangleWidget implements Interactable {

    private BoundingBox boundingBox;
    BukkitTask BBDisplay;
    public InteractableRectangleWidget(Location baseLocation, float width, float height) {
        super(baseLocation, width, height);
        this.setHorizontalPivot(HorizontalPivot.RIGHT);
        this.setVerticalPivot(VerticalPivot.BOTTOM);

        boundingBox = new BoundingBox();
    }

    @Override
    public Vector2f isInside(Player player) {
        Transformation transformation = textDisplay.getTransformation();

        Matrix4f matrix = new Matrix4f()
            .translate(transformation.getTranslation())
            .rotate(transformation.getLeftRotation())
            .scale(transformation.getScale())
            .rotate(transformation.getRightRotation());

        return RaycastUtility.lookingAt(player, baseLocation, matrix, new RaycastUtility.FloatRange(0, 0.125f*getWidth()), new RaycastUtility.FloatRange(0, 0.125f*getHeight()));
    }

    @Override
    public void draw() {
        super.draw();
    }

    @Override
    public void onInteract(Player player, Vector3f hitPoint) {}

    @Override
    public void cleanup() {
        super.cleanup();
        if(BBDisplay != null) BBDisplay.cancel();
    }
}
