package os.systems;

import os.components.*;
import com.badlogic.gdx.input.*;
import com.badlogic.gdx.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;

public class TextInputSystem extends EntitySystem {
    
    private final ComponentMapper<TextComponent> textMapper;
    private final ComponentMapper<KeyInputComponent> keyboardMapper;
    
    private ImmutableArray<Entity> entities;
    
    float keyCursorTimer;
    
    public TextInputSystem() {
        this.textMapper = ComponentMapper.getFor(TextComponent.class);
        this.keyboardMapper = ComponentMapper.getFor(TextComponent.class);
    }
    
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(TextComponent.class, KeyInputComponent.class, FocusedComponent.class).get()
        );
        this.keyCursorTimer = 0;
    }
    
    public void update(float delta) {
        this.keyCursorTimer += delta;
        
        for(Entity entity : entities) {
            TextComponent textComponent = textMapper.get(entity);
            KeyInputComponent keyInputComponent = keyboardMapper.get(entity);
            
            
        }
    }
    
    public void removedFromEngine(Engine engine) {
        
    }
}
