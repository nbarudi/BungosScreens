package ca.bungo.textmenus.types.widgets.generic;

import ca.bungo.textmenus.types.Widget;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftTextDisplay;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

@Getter
@Setter
public class TextWidget implements Widget {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private TextDisplay textDisplay;

    @Setter(AccessLevel.NONE)
    private final Location baseLocation;

    private final String text;
    private Color backgroundColor;
    private Color textColor;
    private float textSize;
    private TextDisplay.TextAlignment textAlignment;
    private float textRotation;

    private Vector3f textOffset;

    private boolean isBold = false;
    private boolean isItalic = false;
    private boolean isUnderlined = false;
    private boolean isStrikethrough = false;
    private boolean isObfuscated = false;

    public TextWidget(Location baseLocation, String text) {
        this.baseLocation = baseLocation;

        this.text = text;
        this.backgroundColor = Color.fromARGB(0, 0, 0, 0);
        this.textColor = Color.WHITE;
        this.textSize = 1f;
        this.textAlignment = TextDisplay.TextAlignment.LEFT;
        this.textRotation = 0f;

        this.textOffset = new Vector3f();
    }

    @Override
    public void draw() {
        cleanup();

        float textAngle = (float) Math.toRadians(textRotation);

        textDisplay = createBasicDisplay(baseLocation);
        textDisplay.setBackgroundColor(backgroundColor);
        textDisplay.setAlignment(textAlignment);


        textDisplay.text(
                Component.text(text)
                        .color(TextColor.color(textColor.asRGB()))
                        .decoration(TextDecoration.ITALIC, isItalic)
                        .decoration(TextDecoration.UNDERLINED, isUnderlined)
                        .decoration(TextDecoration.STRIKETHROUGH, isStrikethrough)
                        .decoration(TextDecoration.OBFUSCATED, isObfuscated)
                        .decoration(TextDecoration.BOLD, isBold));

        Transformation transformation = new Transformation(
                textOffset,
                new AxisAngle4f(textAngle, 0f, 0, 1f),
                new Vector3f(textSize, textSize, textSize),
                new AxisAngle4f()
        );

        textDisplay.setTransformation(transformation);
    }

    @Override
    public void cleanup() {
        if(textDisplay != null) {
            textDisplay.remove();
            textDisplay = null;
        }
    }

}
