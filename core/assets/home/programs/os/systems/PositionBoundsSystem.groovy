package os.systems;

import os.components.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;

public class PositionBoundsSystem extends EntitySystem {
	
    private final ComponentMapper<PositionComponent> positionMapper;;
    private final ComponentMapper<PositionBoundsComponent> boundsMapper;;

    private ImmutableArray<Entity> entities;
    
    public PositionBoundsSystem() {
        this.positionMapper = ComponentMapper.getFor(PositionComponent.class);
        this.boundsMapper = ComponentMapper.getFor(PositionBoundsComponent.class);
    }
	
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(PositionComponent.class, PositionBoundsComponent.class).get()
        );
    }

    public void update(float delta) {
        for(Entity entity : entities) {
            processEntity(entity);
        } 
    }   
    
    public void processEntity(Entity entity) {
        PositionComponent position = positionMapper.get(entity);
        PositionBoundsComponent bounds = boundsMapper.get(entity);

        if(position.x > bounds.maxX) position.x = bounds.maxX;
        if(position.x < bounds.minX) position.x = bounds.minX;
        if(position.y > bounds.maxY) position.y = bounds.maxY;
        if(position.y < bounds.minY) position.y = bounds.minY;
        if(position.z > bounds.maxZ) position.z = bounds.maxZ;
        if(position.z < bounds.minZ) position.z = bounds.minZ;
    }
}
