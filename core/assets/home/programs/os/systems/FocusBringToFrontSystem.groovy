package os.systems;

import os.components.*;
import groovy.transform.CompileStatic;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.viewport.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.ashley.systems.*;

public class FocusBringToFrontSystem extends EntitySystem {
    
    private final ComponentMapper<PositionComponent> positionMapper;
    private final ComponentMapper<ParentComponent> parentMapper;

    private ImmutableArray<Entity> entities, otherEntities;

    public FocusBringToFrontSystem() {
        this.positionMapper = ComponentMapper.getFor(PositionComponent.class);
        this.parentMapper = ComponentMapper.getFor(ParentComponent.class);
    }
	
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(FocusedComponent.class, FocusBringToFrontComponent.class, PositionComponent.class).get()
        );
        this.otherEntities = engine.getEntitiesFor(
            Family.all(PositionComponent.class).get()
        );
    }

    public void update(float delta) {
        if(entities.size() < 1) return;
        int max = 0;
        Entity top = entities.first();
        for(Entity entity : otherEntities) {
            if(!isChild(top, entity) && entity != top) {
                PositionComponent position = positionMapper.get(entity);
                if(position.z > max) {
                    max = position.z;
                }
            }
        }
        
        PositionComponent position = positionMapper.get(top);
        position.z = max + 1;
    }
    
    private boolean isChild(Entity parent, Entity entity) {
        ParentComponent parentComponent = parentMapper.get(entity);
        if(parentComponent == null || parentComponent.parent == null) return false;
        if(parentComponent.parent == parent) return true;
        else return isChild(parent, parentComponent.parent);
    }
}
