package print.systems;

import com.janfic.useride.kernel.components.*;
import com.janfic.useride.kernel.systems.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import terminal.components.*;
import os.components.*;
import os.systems.*;

public class BootSystem extends EntitySystem {	

    ProgramOutputComponent outputComponent = null;
    ProgramInputComponent inputComponent = null;
    
    public void addedToEngine(Engine engine) {
        ImmutableArray<Entity> inject = engine.getEntitiesFor(Family.all(ProgramEntityInjectionComponent.class).get());
        ProgramEntityInjectionComponent injection = inject.first().getComponent(ProgramEntityInjectionComponent.class);
        
        for(Entity e : injection.entities) {
            engine.addEntity(e);  
            if(e.getComponent(ProgramOutputComponent.class) != null) {
                outputComponent = e.getComponent(ProgramOutputComponent.class);
            }
            if(e.getComponent(ProgramInputComponent.class) != null) {
                inputComponent = e.getComponent(ProgramInputComponent.class);
            }
        }
        
        if(inputComponent != null) inputComponent.input.poll();
    }
    
    public void update(float delta) {
        if(outputComponent != null && inputComponent != null && inputComponent.input.size() > 0) {
            outputComponent.output.add(inputComponent.input.poll());
        }
    }
    
    public void removedFromEngine(Engine engine) {
        
    }
    
}
