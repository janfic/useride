
package os.systems;

import os.components.*;
import com.janfic.useride.kernel.components.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.ashley.systems.*;

public class RemoveEntitiesOnMouseClickSystem extends EntitySystem {
    
    private final ComponentMapper<RemoveEntityOnMouseClickComponent> removeEntityMapper;
    private final ComponentMapper<RemoveEntitiesOnMouseClickComponent> removeEntitiesMapper;

    private ImmutableArray<Entity> entities;
    
    public RemoveEntitiesOnMouseClickSystem() {
        this.removeEntityMapper = ComponentMapper.getFor(RemoveEntityOnMouseClickComponent.class);
        this.removeEntitiesMapper = ComponentMapper.getFor(RemoveEntitiesOnMouseClickComponent.class);
    }
    
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(
                MouseClickEventComponent.class
            ).one(
                RemoveEntityOnMouseClickComponent.class, 
                RemoveEntitiesOnMouseClickComponent.class
            ).get()
        );
    }
    
    public void update(float delta) {
        for(Entity entity : entities) {
            
            RemoveEntityOnMouseClickComponent one = removeEntityMapper.get(entity);
            RemoveEntitiesOnMouseClickComponent many = removeEntitiesMapper.get(entity);
           
            
            if(one != null && one.entity != null) {
                System.out.println("removed")
                this.getEngine().removeEntity(one.entity);
            }
            if(many != null && many.entities != null) {
                System.out.println("removed")
                for(Entity e : many.entities) {
                    this.getEngine().removeEntity(e);
                }
            } 
        }
    }
    
    public void removedFromEngine(Engine engine) {
        
    }
}