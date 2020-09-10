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
public class FileSearchOnMouseClickSystem extends EntitySystem {
        
    private final ComponentMapper<FileSearchOnMouseClickComponent> searchMapper;
    
    private ImmutableArray<Entity> entities;
   
    public FileSearchOnMouseClickSystem() {
        this.searchMapper = ComponentMapper.getFor(FileSearchOnMouseClickComponent.class);
    }
    
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(FileSearchOnMouseClickComponent.class, MouseClickEventComponent.class).get()
        );
    }
    
    public void update(float delta) {
        for(Entity entity : entities) {
            FileSearchOnMouseClickComponent click = searchMapper.get(entity);
            if(click.entity != null) click.entity.add(new FileSearchComponent());
            entity.remove(MouseClickEventComponent.class);
        }
    }
    
    public void removedFromEngine(Engine engine) {
        
    }
}
