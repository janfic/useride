package list.systems;

import com.janfic.useride.kernel.components.*;
import com.janfic.useride.kernel.systems.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.files.*;
import terminal.components.*;
import os.components.*;
import os.systems.*;

public class BootSystem extends EntitySystem {	

    ProgramOutputComponent outputComponent = null;
    ProgramInputComponent inputComponent = null;
    FileComponent dir;
    
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
        dir = inputComponent.input.poll();
        FileHandle[] list = dir.file.list();
        for(FileHandle file : list) {
            outputComponent.output.add(file.name());
        }
    }
    
    public void update(float delta) {

    }
    
    public void removedFromEngine(Engine engine) {
        
    }
}


