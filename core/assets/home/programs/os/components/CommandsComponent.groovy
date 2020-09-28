package os.components;

import java.util.*;
import com.badlogic.ashley.core.*;

public class CommandsComponent implements Component {
	Family family;
	Entity entity;
	List<Component> add;
	List<Class<? extends Component>> remove;
}