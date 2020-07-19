package os.systems;

import os.components.*;
import com.badlogic.gdx.input.*;
import com.badlogic.gdx.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;

public class KeyboardInputSystem extends EntitySystem {

    private final ComponentMapper<KeyInputComponent> keyListenerMapper;

    private ImmutableArray<Entity> entities;

    private InputProcessor inputProcessor; 

    int keyDown;
    int keyUp;

    public KeyboardInputSystem() {
        this.keyListenerMapper = ComponentMapper.getFor(KeyInputComponent.class);
        this.inputProcessor = new InputProcessor() {
            @Override
            public boolean keyDown(int keyCode) {
                keyDown = keyCode;
            }

            @Override
            public boolean keyUp(int keyCode) {
                keyUp = keyCode;
            }

            public boolean keyTyped(char key) {}
            public boolean mouseMoved(int x, int y) {}
            public boolean touchDown(int x, int y, int pointer, int button) {}
            public boolean touchUp(int x, int y, int pointer, int button) {}
            public boolean scrolled(int scroll){}
            public boolean touchDragged(int dx, int dy, int button){}
        };
        ((InputMultiplexer)(Gdx.input.getInputProcessor())).addProcessor(this.inputProcessor);
    }
	
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(KeyInputComponent.class).get()
        );
    }

    public void update(float delta) {
        for(Entity entity : entities) {
            KeyInputComponent keyInput = keyListenerMapper.get(entity);

            keyInput.keyUp = this.keyUp;
            keyInput.keyDown = this.keyDown;
        }
    }

    public void removedFromEngine(Engine engine) {
        ((InputMultiplexer)(Gdx.input.getInputProcessor())).removeProcessor(this.inputProcessor);
    }
}