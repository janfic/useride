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
public class FileCopyOnMouseClickSystem extends EntitySystem {
        
    private final ComponentMapper<FileCopyOnMouseClickComponent> copyMapper;
    
    private ImmutableArray<Entity> entities;
   
    public FileCopyOnMouseClickSystem() {
        this.copyMapper = ComponentMapper.getFor(FileCopyOnMouseClickComponent.class);
    }
    
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(FileCopyOnMouseClickComponent.class, MouseClickEventComponent.class).get()
        );
    }
    
    public void update(float delta) {
        for(Entity entity : entities) {
            FileCopyOnMouseClickComponent copy = copyMapper.get(entity);
            if(copy.entity != null) copy.entity.add(new FileCopyComponent());
        }
    }
    
    public void removedFromEngine(Engine engine) {
        
    }
}
