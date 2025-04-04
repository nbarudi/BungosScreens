package ca.bungo.textmenus.utility;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class RaycastUtil {

    public static final float RAY_GAP_OFFSET = 0.001f;

    public record Ray3f(Vector3f origin, Vector3f direction) {
            public Ray3f(Vector3f origin, Vector3f direction) {
                this.origin = origin;
                this.direction = direction.normalize(); // Ensure it's a unit vector
            }

            public Vector3f getPoint(float t) {
                return new Vector3f(direction).mul(t).add(origin);
            }
        }

    public static Ray3f getPlayerLookRay(Player player) {
        Location eye = player.getEyeLocation();
        Vector3f origin = eye.toVector().toVector3f();
        Vector3f direction = eye.getDirection().normalize().toVector3f();
        return new Ray3f(origin, direction);
    }

    public static List<Vector3f> getRayPoints(Ray3f ray, float distance){
        List<Vector3f> points = new ArrayList<>();
        for(float t = 0; t <= distance; t += RAY_GAP_OFFSET){
            points.add(ray.getPoint(t));
        }
        return points;
    }



}


