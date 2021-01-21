package ui.components;

import com.badlogic.ashley.core.*;
import java.util.Set;
import java.util.HashSet;

public class ClassComponent implements Component {
    String raw;
    Set<String> classes;
    public ClassComponent() {
        this.classes = new HashSet<String>();
    }
}
