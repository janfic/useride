package delete.systems;

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
        
        this.engine.addSystem(new FileDeleteSystem());
    }
    
    public void update(float delta) {
        if(inputComponent.input.size() > 0) {
            String input = inputComponent.input.poll();
            FileHandle file = dir.file.child(input);
            if(file.exists()) {
                Entity e = new Entity();
                e.add(new FileDeleteRequestComponent(fileName: file.path()));
                this.getEngine().addEntity(e);
            }
            else {
                outputComponent.output.add("File [ " + input + " ] does not exist. ");
            }
        }
    }
    
    public void removedFromEngine(Engine engine) {
        
    }
}

