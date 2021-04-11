package ui.components;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.graphics.Color;

public class BorderComponent implements Component {
    Color color;
    float radius;
    float thickness;
    String radiusType, thicknessType;
}
