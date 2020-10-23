package print.systems;

import com.janfic.useride.kernel.components.*;
import com.janfic.useride.kernel.systems.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import terminal.components.*;
import os.components.*;
import os.systems.*;

public class BootSystem extends EntitySystem {	

    public void addedToEngine(Engine engine) {
        ImmutableArray<Entity> inject = engine.getEntitiesFor(Family.all(ProgramEntityInjectionComponent.class).get());
        ProgramEntityInjectionComponent injection = inject.first().getComponent(ProgramEntityInjectionComponent.class);
        
        ProgramOutputComponent outputComponent = null;
        ProgramInputComponent inputComponent = null;
        
        for(Entity e : injection.entities) {
            engine.addEntity(e);  
            if(e.getComponent(ProgramOutputComponent.class) != null) {
                outputComponent = e.getComponent(ProgramOutputComponent.class);
            }
            if(e.getComponent(ProgramInputComponent.class) != null) {
                inputComponent = e.getComponent(ProgramInputComponent.class);
            }
        }
        
        if(outputComponent != null && inputComponent != null && inputComponent.lines.size() > 0) {
            outputComponent.lines.add(inputComponent.lines.get(0));
            outputComponent.lines.add(inputComponent.lines.get(0));
            inputComponent.lines.remove(0);
        }
    }
    
    public void update(float delta) {
        
    }
    
    public void removedFromEngine(Engine engine) {
        
    }
    
}
