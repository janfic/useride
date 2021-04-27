package os.systems;

import os.components.*;
import com.badlogic.gdx.input.*;
import com.badlogic.gdx.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import java.util.*;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import com.badlogic.gdx.Input;

public class TextInputSystem extends EntitySystem {
    
    private final ComponentMapper<TextComponent> textMapper;
    private final ComponentMapper<KeyInputComponent> keyboardMapper;
    
    private ImmutableArray<Entity> entities;
    
    float keyCursorTimer;
    boolean cursorShown, lastframeCursorStatus;
    float wait = 1;
    boolean shift, capsLock;
    
    public TextInputSystem() {
        this.textMapper = ComponentMapper.getFor(TextComponent.class);
        this.keyboardMapper = ComponentMapper.getFor(KeyInputComponent.class);
    }
    
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(TextComponent.class, KeyInputComponent.class, FocusedComponent.class).get()
        );
        this.keyCursorTimer = 0;
        this.cursorShown = true;
    }
    
    public void update(float delta) {
        this.keyCursorTimer += delta;
        
        if(this.keyCursorTimer > 0.5f) {
            cursorShown = !cursorShown
            keyCursorTimer = 0;
        }
        
        for(Entity entity : entities) {
            TextComponent textComponent = textMapper.get(entity);
            KeyInputComponent keyInputComponent = keyboardMapper.get(entity);

            if(cursorShown != lastframeCursorStatus) {
                if(cursorShown) {
                    textComponent.text = textComponent.text + "|"
                }
                else {
                    textComponent.text = textComponent.text.substring(0, textComponent.text.length() - 1);
                }
            }
            
            if(keyInputComponent.keyTyped != 0 && keyInputComponent.keyTyped != 8 && keyInputComponent.keyTyped != keyInputComponent.keyUp && !keyInputComponent.pressed.isEmpty()) {
                String input = "" + (char)keyInputComponent.keyTyped;
                if((char)keyInputComponent.keyTyped == 13) input = "\n";
                if(cursorShown) textComponent.text = textComponent.text.substring(0, textComponent.text.length() - 1)
                textComponent.text = textComponent.text + input + (cursorShown ? "|" : "");
            }
            
            if(keyInputComponent.keyTyped == 8 && textComponent.text.length() >= 1) {
                textComponent.text = textComponent.text.substring(0, textComponent.text.length() - (cursorShown ? 2 : 1) );
                cursorShown = false;
            }
        }
        lastframeCursorStatus = cursorShown;
    }
    
    public void removedFromEngine(Engine engine) {
        
    }
}
