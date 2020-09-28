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
public class FileRenameOnMouseClickSystem extends EntitySystem {
        
    private final ComponentMapper<FileRenameOnMouseClickComponent> renameMapper;
    
    private ImmutableArray<Entity> entities;
   
    public FileRenameOnMouseClickSystem() {
        this.renameMapper = ComponentMapper.getFor(FileRenameOnMouseClickComponent.class);
    }
    
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(FileRenameOnMouseClickComponent.class, MouseClickEventComponent.class).get()
        );
    }
    
    public void update(float delta) {
        for(Entity entity : entities) {
            FileRenameOnMouseClickComponent rename = renameMapper.get(entity);
            if(rename.entity != null ){
                rename.entity.add(new FocusedComponent());
            } 
        }
    }
    
    public void removedFromEngine(Engine engine) {
        
    }
}


