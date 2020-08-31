package os.systems;

import os.components.*;
import com.badlogic.gdx.input.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.utils.IntSet;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;

public class KeyboardInputSystem extends EntitySystem {

    private final ComponentMapper<KeyInputComponent> keyListenerMapper;

    private ImmutableArray<Entity> entities;

    private InputProcessor inputProcessor; 

    int keyDown;
    int keyUp;
    IntSet pressed;

    public KeyboardInputSystem() {
        this.keyListenerMapper = ComponentMapper.getFor(KeyInputComponent.class);
        this.inputProcessor = new InputProcessor() {
            @Override
            public boolean keyDown(int keyCode) {
                pressed.add(keyCode);
                keyDown = keyCode;
            }

            @Override
            public boolean keyUp(int keyCode) {
                pressed.remove(keyCode);
            }

            public boolean keyTyped(char key) {}
            public boolean mouseMoved(int x, int y) {}
            public boolean touchDown(int x, int y, int pointer, int button) {}
            public boolean touchUp(int x, int y, int pointer, int button) {}
            public boolean scrolled(int scroll){}
            public boolean touchDragged(int dx, int dy, int button){}
        };
        ((InputMultiplexer)(Gdx.input.getInputProcessor())).addProcessor(this.inputProcessor);
        pressed = new IntSet();
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
        }
    }

    public void removedFromEngine(Engine engine) {
        ((InputMultiplexer)(Gdx.input.getInputProcessor())).removeProcessor(this.inputProcessor);
    }
}