package os.components;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.graphics.g2d.*;

public class AnimationComponent implements Component {
	Animation<TextureRegion> animation;
	float stateTime, frameRate;
}