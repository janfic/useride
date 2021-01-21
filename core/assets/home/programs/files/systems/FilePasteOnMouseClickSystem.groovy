package files.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.assets.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.files.*;

import com.janfic.useride.kernel.components.*;
import com.janfic.useride.kernel.systems.*;
import os.components.*;
import files.components.*;

import groovy.transform.CompileStatic;


@CompileStatic
public class FilePasteOnMouseClickSystem extends EntitySystem {
        
    private final ComponentMapper<FilePasteOnMouseClickComponent> pasteMapper;
    
    private ImmutableArray<Entity> entities;
   
    public FilePasteOnMouseClickSystem() {
        this.pasteMapper = ComponentMapper.getFor(FilePasteOnMouseClickComponent.class);
    }
    
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(FilePasteOnMouseClickComponent.class, MouseClickEventComponent.class).get()
        );
    }
    
    public void update(float delta) {
        for(Entity entity : entities) {
            FilePasteOnMouseClickComponent paste = pasteMapper.get(entity);
            if(paste.entity != null) paste.entity.add(new FilePasteComponent());
        }
    }
    
    public void removedFromEngine(Engine engine) {
        
    }
}