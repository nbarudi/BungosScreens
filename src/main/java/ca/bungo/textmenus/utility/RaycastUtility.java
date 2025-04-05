package ca.bungo.textmenus.utility;

import ca.bungo.textmenus.TextMenusPlugin;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Transformation;
import org.joml.*;

import javax.annotation.Nullable;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RaycastUtility {

    public record Ray3f(Vector3f origin, Vector3f direction) {
            public Ray3f(Vector3f origin, Vector3f direction) {
                this.origin = origin;
                this.direction = direction.normalize(); // Ensure it's a unit vector
            }

            public Vector3f getPoint(float t) {
                return new Vector3f(direction).mul(t).add(origin);
            }
        }

    public record FloatRange(float start, float end) {
        public boolean contains(float t) {
                return t >= start && t <= end;
            }
        }

    public static Ray3f getPlayerLookRay(Player player) {
        Location eye = player.getEyeLocation();
        Vector3f origin = eye.toVector().toVector3f();
        Vector3f direction = eye.getDirection().normalize().toVector3f();
        return new Ray3f(origin, direction);
    }


    public static List<Vector3f> sampleRay(Ray3f ray, float distance, float step) {
        List<Vector3f> points = new ArrayList<>();
        for (float t = 0; t <= distance; t += step) {
            points.add(ray.getPoint(t));
        }
        return points;
    }



    /**
     * Raycast method for checking where against a text display screen a player is looking.
     * This is AGGRESSIVELY tailored for RectangleWidgets with a Bottom-Left pivot point alignment
     * As well, rotation is not supported because my sanity is already falling
     * @param player Player you are checking the raycast for
     * @param displayLocation Where is the root location of the display entity
     * @param plane Literally not used, but it's here because I 'wanted' to make rotations and pivots supported
     * @param xRange Float range for the x tolerance (basically a data type to say 'between 0 to 1f')
     * @param yRange Float range for the y tolerance
     * @return Vector2f of the localspace point the player clicked
     * */
    public static @Nullable Vector2f lookingAt(Player player, Location displayLocation, Matrix4f plane, FloatRange xRange, FloatRange yRange) {

        Location eye = player.getEyeLocation();
        Vector3f rayOrigin = eye.toVector().toVector3f();
        Vector3f rayDirection = eye.getDirection().normalize().toVector3f();

        Vector3f planePoint = displayLocation.toVector().toVector3f();
        Vector3f planeNormal = displayLocation.getDirection().normalize().toVector3f();

        float denom = rayDirection.dot(planeNormal);

        if(Math.abs(denom) < 1e-6) { return null; }

        Vector3f difference = new Vector3f(planePoint).sub(rayOrigin);
        float t = difference.dot(planeNormal)/denom;
        if(t < 0){
            return null;
        }

        Vector3f intersectionPoint = new Vector3f(rayOrigin).add(new Vector3f(rayDirection).mul(t));

        Vector3f hitVector = new Vector3f(
                (float)(intersectionPoint.x() - displayLocation.getX()),
                (float)(intersectionPoint.y() - displayLocation.getY()),
                (float)(intersectionPoint.z() - displayLocation.getZ())
        );

        float yawRadians = (float) Math.toRadians(-displayLocation.getYaw());
        float cos = (float) Math.cos(yawRadians);
        float sin = (float) Math.sin(yawRadians);

        float localPointX = hitVector.x * cos - hitVector.z * sin;
        float localPointY = hitVector.y;

        Vector2f localPoints = new Vector2f(localPointX, localPointY);

        TextMenusPlugin.LOGGER.info("localPoints: {}", localPoints);

        if(xRange.contains(localPoints.x) && yRange.contains(localPoints.y)) {
            return new Vector2f(localPoints.x(), localPoints.y());
        }

        return null;
    }

    public static void debugRay(World world, Vector3f origin, Vector3f direction, float maxDistance, float step) {
        for (float t = 0; t <= maxDistance; t += step) {
            Vector3f point = new Vector3f(direction.normalize()).mul(t).add(origin);
            world.spawnParticle(Particle.DUST,
                    point.x, point.y, point.z,
                    0,
                    new Particle.DustOptions(Color.RED, 1f));
        }
    }

}


