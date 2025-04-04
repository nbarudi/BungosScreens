package ca.bungo.textmenus.utility;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.joml.*;

import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

public class RaycastUtil {

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

    private static boolean lineIntersectsPlane(Vector3f point1, Vector3f point2, Matrix4f planeTransform, FloatRange xRange, FloatRange yRange){
        Matrix4f inverted = new Matrix4f(planeTransform).invert();

        Vector4f _point1 = inverted.transform(new Vector4f(point1, 1));
        Vector3f point1Transformed = new Vector3f(_point1.x, _point1.y, _point1.z);

        Vector4f _point2 = inverted.transform(new Vector4f(point2, 1));
        Vector3f point2Transformed = new Vector3f(_point2.x, _point2.y, _point2.z);

        Vector3f point = lineAtZ(point1Transformed, point2Transformed, 0f);
        if (point == null) return false;

        return yRange.contains(point.y) && xRange.contains(point.x);
    }

    private static Vector3f lineAtZ(Vector3f point1, Vector3f point2, float z){
        float dz = point1.z - point2.z;
        if (Math.abs(dz) < 1e-6f) return null;
        float t = (z - point1.z) / (point2.z - point1.z);
        return new Vector3f(point1).lerp(point2, t);
    }

    public static boolean lookingAt(Player player, Vector displayLocation, Matrix4f plane){
        var playerEyeLocation = player.getEyeLocation();
        var point1 = playerEyeLocation.toVector().subtract(displayLocation).toVector3f();
        var point2 = playerEyeLocation.toVector().add(player.getLocation().getDirection()).subtract(displayLocation).toVector3f();

        debugRay(
                player.getWorld(),
                playerEyeLocation.toVector().toVector3f(),
                player.getLocation().getDirection().toVector3f(),
                10,
                0.01f
        );

        return lineIntersectsPlane(point1, point2, plane, new FloatRange(0f, 1f), new FloatRange(0f, 1f));
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


