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
    private final ComponentMapper<TextSelectionComponent> selectMapper;
    
    private ImmutableArray<Entity> entities;
    
    float keyCursorTimer;
    boolean cursorShown, lastframeCursorStatus;
    float wait = 0;
    boolean shift, capsLock;
    
    public TextInputSystem() {
        this.textMapper = ComponentMapper.getFor(TextComponent.class);
        this.selectMapper = ComponentMapper.getFor(TextSelectionComponent.class);
        this.keyboardMapper = ComponentMapper.getFor(KeyInputComponent.class);
    }
    
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(TextComponent.class, KeyInputComponent.class, FocusedComponent.class, TextSelectionComponent.class).get()
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
            TextSelectionComponent selectComponent = selectMapper.get(entity);
            KeyInputComponent keyInputComponent = keyboardMapper.get(entity);
            if(selectComponent.textCursorIndex == -1) selectComponent.textCursorIndex = textComponent.text.length();
            
            if(keyInputComponent.keyTyped != 0 && keyInputComponent.keyTyped != 8 && keyInputComponent.keyTyped != keyInputComponent.keyUp && !keyInputComponent.pressed.isEmpty()) {
                String input = "" + (char)keyInputComponent.keyTyped;
                if((char)keyInputComponent.keyTyped == 13) input = "\n";
                textComponent.text = textComponent.text.substring(0, selectComponent.textCursorIndex) + input + textComponent.text.substring(selectComponent.textCursorIndex);
                selectComponent.textCursorIndex = selectComponent.textCursorIndex + 1 ;
            }
            
            if(keyInputComponent.keyTyped == 8 && textComponent.text.length() >= 1 && selectComponent.textCursorIndex > 0) {
                textComponent.text = textComponent.text.substring(0, selectComponent.textCursorIndex - 1) + textComponent.text.substring(selectComponent.textCursorIndex);
                selectComponent.textCursorIndex = selectComponent.textCursorIndex - 1 ;
            }
            
            if(keyInputComponent.keyDown == Input.Keys.LEFT) {
                wait = 0;
                if(selectComponent.textCursorIndex > 0) selectComponent.textCursorIndex = selectComponent.textCursorIndex - 1 ;
            }
            if(keyInputComponent.pressed.contains(Input.Keys.LEFT)) {
                wait += delta;
                if(wait >= 0.75f) {
                    wait -= 0.05f
                    if(selectComponent.textCursorIndex > 0) selectComponent.textCursorIndex = selectComponent.textCursorIndex - 1 ;
                }
            }
            
            if(keyInputComponent.keyDown == Input.Keys.RIGHT) {
                wait = 0;
                if(selectComponent.textCursorIndex < textComponent.text.length()) selectComponent.textCursorIndex = selectComponent.textCursorIndex + 1 ;
            }
            if(keyInputComponent.pressed.contains(Input.Keys.RIGHT)) {
                wait += delta;
                if(wait >= 0.75f) {
                    wait -= 0.05f
                    if(selectComponent.textCursorIndex < textComponent.text.length()) selectComponent.textCursorIndex = selectComponent.textCursorIndex + 1 ;
                }
            }
        }
        lastframeCursorStatus = cursorShown;
    }
    
    public void removedFromEngine(Engine engine) {
        
    }
}
