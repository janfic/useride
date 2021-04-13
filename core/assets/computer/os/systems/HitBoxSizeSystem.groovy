package os.systems;

import os.components.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;

public class HitBoxSizeSystem extends EntitySystem {
	
    private final ComponentMapper<SizeComponent> sizeMapper;
    private final ComponentMapper<HitBoxComponent> boxMapper;

    private ImmutableArray<Entity> entities;
    
    public HitBoxSizeSystem() {
        this.sizeMapper = ComponentMapper.getFor(SizeComponent.class);
        this.boxMapper = ComponentMapper.getFor(HitBoxComponent.class);
    }
	
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(SizeComponent.class, HitBoxComponent.class).get()
        );
    }

    public void update(float delta) {
        for(Entity entity : entities) {
            processEntity(entity);
        } 
    }   
    
    public void processEntity(Entity entity) {
        SizeComponent size = sizeMapper.get(entity);
        HitBoxComponent box = boxMapper.get(entity);

        box.rectangle.setHeight(size.height);
        box.rectangle.setWidth(size.width);
    }
}


