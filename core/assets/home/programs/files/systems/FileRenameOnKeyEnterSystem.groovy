package files.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.assets.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.files.FileHandle;

import com.janfic.useride.kernel.components.*;
import com.janfic.useride.kernel.systems.*;
import os.components.*;
import os.systems.*;
import files.components.*;

public class FileRenameOnKeyEnterSystem extends EntitySystem {
    
    private final ComponentMapper<KeyInputComponent> keyMapper;
    private final ComponentMapper<TextComponent> textMapper;
    
    private ImmutableArray<Entity> entities;
    
    public FileRenameOnKeyEnterSystem() {
        this.keyMapper = ComponentMapper.getFor(KeyInputComponent.class);
        this.textMapper = ComponentMapper.getFor(TextComponent.class);
    }
    
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(KeyInputComponent.class, TextComponent.class, FileRenameOnKeyEnterComponent.class, FocusedComponent.class).get()
        );
    }
    
    public void update(float delta) {
        for(Entity entity : entities) {
            KeyInputComponent keyInput = keyMapper.get(entity);
            TextComponent text = textMapper.get(entity);
            if(keyInput.keyTyped == 13) {
                entity.add(new FileRenameComponent(rename: text.text));
                entity.remove(FocusedComponent.class);
            }
        }
    }
    
    public void removedFromEngine(Engine engine) {
        
    }
}
