package useragar.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import useragar.components.*


public class HungerSystem extends EntitySystem {

    public final ComponentMapper<MassComponent> massMapper;
	
    private ImmutableArray<Entity> entities;
    
    public HungerSystem() {
        this.massMapper = ComponentMapper.getFor(MassComponent.class);
    }
    
    @Override
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(
                MassComponent.class
            ).get()
        );
    }
    
    @Override
    public void update(float delta) {
        for(Entity entity : entities) {
            MassComponent mass = massMapper.get(entity);
            
            mass.mass *= (mass.mass > 100 ? 0.99 : 1);
            if(mass.mass < 10) {
                mass.mass = 10;
            }
        }
    }
    
    @Override
    public void removedFromEngine(Engine engine) {
        
    }
    
}