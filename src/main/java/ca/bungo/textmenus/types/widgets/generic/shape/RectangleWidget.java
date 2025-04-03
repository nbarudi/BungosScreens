package ca.bungo.textmenus.types.widgets.generic.shape;

import ca.bungo.textmenus.types.Widget;
import ca.bungo.textmenus.types.widgets.HorizontalPivot;
import ca.bungo.textmenus.types.widgets.VerticalPivot;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;


@Getter
@Setter
public class RectangleWidget implements Widget {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private TextDisplay textDisplay;

    @Setter(AccessLevel.NONE)
    private final Location baseLocation;

    private float width;
    private float height;
    private Color color;

    private Vector3f positionOffset;
    private HorizontalPivot horizontalPivot;
    private VerticalPivot verticalPivot;
    private float rectangleRotation;

    public RectangleWidget(Location baseLocation, float width, float height){
        this.baseLocation = baseLocation;
        this.width = width;
        this.height = height;

        this.color = Color.BLACK;
        this.positionOffset = new Vector3f();
        this.horizontalPivot = HorizontalPivot.LEFT;
        this.verticalPivot = VerticalPivot.BOTTOM;
    }

    @Override
    public void draw() {
        float rotation = (float)Math.toRadians(rectangleRotation);

        textDisplay = createBasicDisplay(baseLocation);

        textDisplay.setBackgroundColor(color);
        textDisplay.text(Component.text(" "));

        float posX = 0;
        float posY = 0;

        if(horizontalPivot == HorizontalPivot.RIGHT){
            posX += -0.075f * width;
        }
        else if(horizontalPivot == HorizontalPivot.LEFT){
            posX += 0.05f * width;
        }
        else {
            posX += -0.01f * width;
        }

        if(verticalPivot == VerticalPivot.BOTTOM){
            //posY += -0.075f * width;
            posY += 0;
        }
        else if(verticalPivot == VerticalPivot.TOP){
            posY += -0.125f*height;
        }
        else {
            posY += -0.065f*height;
        }

        posX += (float) (-0.05f + (0.05f * Math.cos(rotation)))*width;
        posY += (float) (0.05f * Math.sin(rotation)) * height;


        positionOffset.x += posX;
        positionOffset.y += posY;

        Transformation transformation = new Transformation(
                positionOffset,
                new AxisAngle4f(rotation, 0, 0, 1),
                new Vector3f(width, height/2f, 0),
                new AxisAngle4f()
        );

        textDisplay.setTransformation(transformation);
    }

    @Override
    public void cleanup() {
        if(textDisplay != null){
            textDisplay.remove();
            textDisplay = null;
        }
    }
}
