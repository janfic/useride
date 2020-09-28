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
public class FileDeleteOnMouseClickSystem extends EntitySystem {
        
    private final ComponentMapper<FileDeleteOnMouseClickComponent> deleteMapper;
    private final ComponentMapper<FileComponent> fileMapper;
    
    private ImmutableArray<Entity> entities;
   
    public FileDeleteOnMouseClickSystem() {
        this.deleteMapper = ComponentMapper.getFor(FileDeleteOnMouseClickComponent.class);
        this.fileMapper = ComponentMapper.getFor(FileComponent.class);
    }
    
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(FileDeleteOnMouseClickComponent.class, MouseClickEventComponent.class).get()
        );
    }
    
    public void update(float delta) {
        for(Entity entity : entities) {
            FileDeleteOnMouseClickComponent delete = deleteMapper.get(entity);
            if(delete.entity != null ){
                FileComponent fileComponent = fileMapper.get(delete.entity);
                if(fileComponent != null) delete.entity.add(new FileDeleteRequestComponent(fileName: fileComponent.file.path()));
            } 
        }
    }
    
    public void removedFromEngine(Engine engine) {
        
    }
}

