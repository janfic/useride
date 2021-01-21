package os.components;

import com.badlogic.ashley.core.*;
import java.util.List;
import java.util.ArrayList;

public class ChildrenComponent implements Component {
    List<Entity> children;
    public ChildrenComponent() {
        this.children = new ArrayList<Entity>();
    }
}

