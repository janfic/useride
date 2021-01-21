package os.components;

import com.badlogic.ashley.core.*;
import java.util.List;
import java.util.ArrayList;

public class SiblingsComponent implements Component {
    List<Entity> siblings;
    public SiblingsComponent() {
        this.siblings = new ArrayList<Entity>();
    }
}


