package os.systems;

import os.components.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;

@groovy.transform.CompileStatic
public class SpinSystem extends EntitySystem {
	
    private final ComponentMapper<RotationComponent> rotationMapper;;
    private final ComponentMapper<SpinComponent> spinMapper;;

    private ImmutableArray<Entity> entities;

    public SpinSystem() {
        this.spinMapper = ComponentMapper.getFor(SpinComponent.class);
        this.rotationMapper = ComponentMapper.getFor(RotationComponent.class);
    }
    
    @Override
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(SpinComponent.class, RotationComponent.class).get()
        );
    }

    @Override
    public void update(float delta) {
        for(Entity entity : entities) {
            RotationComponent rotation = rotationMapper.get(entity);
            SpinComponent spin = spinMapper.get(entity);
            
            rotation.rotation += spin.spin;
        }
    }
}
