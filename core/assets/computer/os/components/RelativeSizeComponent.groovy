package os.components;

import com.badlogic.ashley.core.*;

public class RelativeSizeComponent implements Component {
    public float width, height;
    public String unit = "p";
    public Entity parent;
}
