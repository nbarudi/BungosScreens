package ca.bungo.textmenus.utility;

import ca.bungo.textmenus.TextMenusPlugin;
import com.mojang.math.Transformation;
import io.papermc.paper.configuration.transformation.Transformations;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
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
        //Building player raycast
        Location eye = player.getEyeLocation();
        Vector3f rayOrigin = eye.toVector().toVector3f();
        Vector3f rayDirection = eye.getDirection().normalize().toVector3f();

        //Defining the plane - plane point being the 0,0 location, plane normal being the vector pointing perpendicular to the plane
        Vector3f planePoint = displayLocation.toVector().toVector3f();
        Vector3f planeNormal = displayLocation.getDirection().normalize().toVector3f();

        //Offsetting everything to make the planes pivot point to be 0,0
        rayOrigin = new Vector3f(rayOrigin).sub(planePoint);
        planePoint = new Vector3f(planePoint).sub(planePoint);

        //Is the dot product of the normal very small? It's likely the player is looking perpendicular to the screen
        float denom = rayDirection.dot(planeNormal);
        if(Math.abs(denom) < 1e-6) { return null; }


        //Where along the ray does the intersection occur?
        Vector3f difference = new Vector3f(planePoint).sub(rayOrigin);
        float t = difference.dot(planeNormal)/denom;
        if(t < 0){
            return null;
        }

        //Intersection point between the player ray and the plane
        Vector3f intersectionPoint = new Vector3f(rayOrigin).add(new Vector3f(rayDirection).mul(t));

        //Adjusting for the Yaw of the Text Display
        float yawRadians = (float) Math.toRadians(-displayLocation.getYaw());
        float cos = (float) Math.cos(yawRadians);
        float sin = (float) Math.sin(yawRadians);

        float localPointX = intersectionPoint.x * cos - intersectionPoint.z * sin;
        float localPointY = intersectionPoint.y;

        Vector2f localPoints = new Vector2f(localPointX, localPointY);

        //Checking if the intersection point is within our defined bounds
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


