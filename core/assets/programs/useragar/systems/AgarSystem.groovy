package useragar.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import useragar.components.*;

public class AgarSystem extends EntitySystem {

    public final ComponentMapper<MassComponent> massMapper;
    public final ComponentMapper<CircleComponent> circleMapper;
	
    private ImmutableArray<Entity> entities;
    
    public AgarSystem() {
        this.massMapper = ComponentMapper.getFor(MassComponent.class);
        this.circleMapper = ComponentMapper.getFor(CircleComponent.class);
    }
    
    @Override
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(
                MassComponent.class,
                CircleComponent.class
            ).get()
        );
    }
    
    @Override
    public void update(float delta) {
        for(Entity entity : entities) {
            MassComponent mass = massMapper.get(entity);
            CircleComponent circle = circleMapper.get(entity);
            
            circle.radius = (float) Math.sqrt(mass.mass / Math.PI);
        }
    }
    
    @Override
    public void removedFromEngine(Engine engine) {
        
    }
    
}