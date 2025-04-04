package ca.bungo.textmenus.types;

import ca.bungo.textmenus.utility.RaycastUtil;
import org.bukkit.entity.Player;
import org.joml.Vector3f;

public interface Interactable {

    float HITBOX_TOLERANCE = 0.003f;

    boolean isInside(Player player);
    void onInteract(Player player, Vector3f hitPoint);

}
