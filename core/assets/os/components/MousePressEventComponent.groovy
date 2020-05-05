package os.components;

import com.badlogic.ashley.core.*;

public class MousePressEventComponent implements Component {
	float x, y;
	int button;
	int count;
	float timer;
}