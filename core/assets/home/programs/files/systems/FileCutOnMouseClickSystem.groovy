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
public class FileCutOnMouseClickSystem extends EntitySystem {
        
    private final ComponentMapper<FileCutOnMouseClickComponent> cutMapper;
    
    private ImmutableArray<Entity> entities;
   
    public FileCutOnMouseClickSystem() {
        this.cutMapper = ComponentMapper.getFor(FileCutOnMouseClickComponent.class);
    }
    
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(FileCutOnMouseClickComponent.class, MouseClickEventComponent.class).get()
        );
    }
    
    public void update(float delta) {
        for(Entity entity : entities) {
            FileCutOnMouseClickComponent cut = cutMapper.get(entity);
            if(cut.entity != null) cut.entity.add(new FileCutComponent());
        }
    }
    
    public void removedFromEngine(Engine engine) {
        
    }
}

