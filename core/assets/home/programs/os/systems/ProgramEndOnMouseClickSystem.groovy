package os.systems;

import os.components.*;
import com.janfic.useride.kernel.components.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.ashley.systems.*;

public class ProgramEndOnMouseClickSystem extends EntitySystem {
    
    private final ComponentMapper<ProgramEndOnMouseClickComponent> endOnClickMapper;
    private final ComponentMapper<IDComponent> idMapper;

    private ImmutableArray<Entity> entities;
    
    public ProgramEndOnMouseClickSystem() {
        this.endOnClickMapper = ComponentMapper.getFor(ProgramEndOnMouseClickComponent.class);
        this.idMapper = ComponentMapper.getFor(IDComponent.class);
    }
    
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(
                ProgramEndOnMouseClickComponent.class,
                IDComponent.class,
                MouseClickEventComponent.class
            ).get()
        );
    }
    
    public void update(float delta) {
        for(Entity entity : entities) {
            ProgramEndOnMouseClickComponent end = endOnClickMapper.get(entity);
            IDComponent id = idMapper.get(entity);
            
            ProgramEndRequestComponent endRequest = new ProgramEndRequestComponent(programName: end.name, id: id.id);
            Entity endEntity = new Entity();
            endEntity.add(endRequest);
            
            this.getEngine().addEntity(endEntity);
        }
    }
    
    public void removedFromEngine(Engine engine) {
        
    }
}