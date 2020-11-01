package copy.systems;

import com.janfic.useride.kernel.components.*;
import com.janfic.useride.kernel.systems.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.files.*;
import terminal.components.*;
import files.systems.*;
import files.components.*;
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
        if(inputComponent.input.size > 0) {
            String input = inputComponent.input.poll();
            String[] files = input.trim().split("\\s");
            if(files.length < 2) {
                outputComponent.output.add("missing argument: copy [fromFile] [toFile or Directory]")
            }
            else {
                Entity copy = new Entity();
                copy.add(new FileCopyComponent());
                copy.add(new FileLoadRequestComponent(fileName: dir.file.child(files[0])));
                
                Entity paste = new Entity();
                paste.add(new FilePasteComponent());
                paste.add(new FileLoadRequestComponent(fileName: dir.file.child(files[1])));
            
                this.getEngine().addEntity(copy);
                this.getEngine().addEntity(paste);
            }
        }
        
        this.getEngine().addSystem(new FileLoadSystem());
        this.getEngine().addSystem(new FilePasteSystem());
    }
    
    public void update(float delta) {
        
    }
    
    public void removedFromEngine(Engine engine) {
        
    }
}



