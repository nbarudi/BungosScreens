package ca.bungo.textmenus.types.widgets.generic.shape;

import ca.bungo.textmenus.TextMenusPlugin;
import ca.bungo.textmenus.types.Interactable;
import ca.bungo.textmenus.types.widgets.HorizontalPivot;
import ca.bungo.textmenus.types.widgets.VerticalPivot;
import ca.bungo.textmenus.utility.RaycastUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Transformation;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class InteractableRectangleWidget extends RectangleWidget implements Interactable {


    public InteractableRectangleWidget(Location baseLocation, float width, float height) {
        super(baseLocation, width, height);
        this.setHorizontalPivot(HorizontalPivot.CENTER);
        this.setVerticalPivot(VerticalPivot.CENTER);
    }

    @Override
    public boolean isInside(Player player) {
        Transformation transformation = textDisplay.getTransformation();

        Matrix4f matrix = new Matrix4f()
            .translate(transformation.getTranslation())
            .rotate(transformation.getLeftRotation())
            .scale(transformation.getScale())
            .rotate(transformation.getRightRotation());

        return RaycastUtil.lookingAt(player, baseLocation.toVector(), matrix);
    }

    @Override
    public void onInteract(Player player, Vector3f hitPoint) {}
}
