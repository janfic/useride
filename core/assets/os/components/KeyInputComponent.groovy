package os.components;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.IntSet;

public class KeyInputComponent implements Component {
    IntSet pressed;
    int keyDown;
}