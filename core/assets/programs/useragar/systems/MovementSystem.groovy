package useragar.systems;

import useragar.components.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;

public class MovementSystem extends EntitySystem {
	
    public final ComponentMapper<PositionComponent> positionMapper;
    public final ComponentMapper<VelocityComponent> velocityMapper;
	
    private ImmutableArray<Entity> entities;
	
    public MovementSystem() {
        this.positionMapper = ComponentMapper.getFor(PositionComponent.class);
        this.velocityMapper = ComponentMapper.getFor(VelocityComponent.class);
    }
	
    @Override
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(PositionComponent.class, VelocityComponent.class).get()
        );
    }
	
    @Override
    public void update(float delta) {
        for(Entity entity : entities) {
            PositionComponent pos = positionMapper.get(entity);
            VelocityComponent vel = velocityMapper.get(entity);
			
            pos.x += vel.dx;
            pos.y += vel.dy;
        }
    }
	
    @Override
    public void removedFromEngine(Engine engine) {
        this.entities = null;
    }
}