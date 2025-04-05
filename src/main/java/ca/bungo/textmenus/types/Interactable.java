package ca.bungo.textmenus.types;

import org.bukkit.entity.Player;
import org.joml.Vector2f;
import org.joml.Vector3f;

import javax.annotation.Nullable;

public interface Interactable {

    float HITBOX_TOLERANCE = 0.003f;

    @Nullable Vector2f isInside(Player player);
    void onInteract(Player player, Vector3f hitPoint);

}
