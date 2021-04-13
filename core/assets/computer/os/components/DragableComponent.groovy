package os.components;

import com.badlogic.ashley.core.*;

public class DragableComponent implements Component {
    boolean horizontal;
    boolean verticle;
    
    public DragableComponent() {
        this.horizontal = true;
        this.verticle = true;
    }
}