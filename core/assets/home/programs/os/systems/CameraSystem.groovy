package os.systems;

import os.components.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;

import java.time.*;

public class CameraSystem extends EntitySystem {
	
    private final ComponentMapper<CameraComponent> cameraMapper;;

    private ImmutableArray<Entity> entities;

    public CameraSystem() {
        this.cameraMapper = ComponentMapper.getFor(CameraComponent.class);
    }
	
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(CameraComponent.class).get()
        );
    }

    public void update(float delta) {
        for(Entity entity : entities) {
            CameraComponent camera = cameraMapper.get(entity);
            
            camera.angle += delta;
        }
    }
}

