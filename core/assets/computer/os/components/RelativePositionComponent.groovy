package os.components;

import com.badlogic.ashley.core.*;

public class RelativePositionComponent implements Component {
    public float x, y, z;
    public String unit = "p";
    public float parentMultiplier = 1.0;
    public Entity parent;
}