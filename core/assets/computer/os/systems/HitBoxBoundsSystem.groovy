package os.systems;

import os.components.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;

public class HitBoxBoundsSystem extends EntitySystem {
	
    private final ComponentMapper<PositionComponent> positionMapper;
    private final ComponentMapper<HitBoxBoundsComponent> boundsMapper;
    private final ComponentMapper<HitBoxComponent> boxMapper;

    private ImmutableArray<Entity> entities;
    
    public HitBoxBoundsSystem() {
        this.positionMapper = ComponentMapper.getFor(PositionComponent.class);
        this.boundsMapper = ComponentMapper.getFor(HitBoxBoundsComponent.class);
        this.boxMapper = ComponentMapper.getFor(HitBoxComponent.class);
    }
	
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(PositionComponent.class, HitBoxBoundsComponent.class, HitBoxComponent.class).get()
        );
    }

    public void update(float delta) {
        for(Entity entity : entities) {
            processEntity(entity);
        } 
    }   
    
    public void processEntity(Entity entity) {
        PositionComponent position = positionMapper.get(entity);
        HitBoxBoundsComponent bounds = boundsMapper.get(entity);
        HitBoxComponent box = boxMapper.get(entity);

        if(position.x + box.rectangle.getWidth() > bounds.maxX) position.x = bounds.maxX - box.rectangle.getWidth();
        if(position.x < bounds.minX) position.x = bounds.minX;
        if(position.y + box.rectangle.getHeight() > bounds.maxY) position.y = bounds.maxY - box.rectangle.getHeight();
        if(position.y < bounds.minY) position.y = bounds.minY;
    }
}

