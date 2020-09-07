package os.components;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.Input.Keys;
import java.util.Set;

public class KeyInputComponent implements Component {
    Set<Integer> pressed;
    int keyDown;
    int keyUp;
    int keyTyped;
}