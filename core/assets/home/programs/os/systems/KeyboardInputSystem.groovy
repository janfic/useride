package os.systems;

import os.components.*;
import com.badlogic.gdx.input.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.utils.IntSet;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import java.util.*;

public class KeyboardInputSystem extends EntitySystem {

    private final ComponentMapper<KeyInputComponent> keyListenerMapper;

    private ImmutableArray<Entity> entities;

    private InputProcessor inputProcessor; 

    int keyDown;
    int keyUp;
    int keyTyped;
    Set<Integer> pressed;

    public KeyboardInputSystem() {
        this.keyListenerMapper = ComponentMapper.getFor(KeyInputComponent.class);
        this.inputProcessor = new InputProcessor() {
            @Override
            public boolean keyDown(int keyCode) {
                pressed.add(keyCode);
                keyDown = keyCode;
                return false;
            }

            @Override
            public boolean keyUp(int keyCode) {
                pressed.remove(keyCode);
                keyUp = keyCode;
                return false;
            }

            public boolean keyTyped(char key) {
                keyTyped = key;
                return false;
            }
            public boolean mouseMoved(int x, int y) {return false;}
            public boolean touchDown(int x, int y, int pointer, int button) {return false;}
            public boolean touchUp(int x, int y, int pointer, int button) {return false;}
            public boolean scrolled(int scroll){return false;}
            public boolean touchDragged(int dx, int dy, int button){return false;}
        };
        ((InputMultiplexer)(Gdx.input.getInputProcessor())).addProcessor(this.inputProcessor);
        pressed = new HashSet<Integer>();
    }
	
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(KeyInputComponent.class).get()
        );
    }

    public void update(float delta) {
        for(Entity entity : entities) {
            KeyInputComponent keyInput = keyListenerMapper.get(entity);
            keyInput.pressed = pressed;
            keyInput.keyDown = keyDown;
            keyInput.keyUp = keyUp;
            keyInput.keyTyped = keyTyped;
        }
        keyDown = 0;
        keyUp = 0;
        keyTyped = 0;
    }

    public void removedFromEngine(Engine engine) {
        ((InputMultiplexer)(Gdx.input.getInputProcessor())).removeProcessor(this.inputProcessor);
    }
}