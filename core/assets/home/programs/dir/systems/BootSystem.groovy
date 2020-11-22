package dir.systems;

import com.janfic.useride.kernel.components.*;
import com.janfic.useride.kernel.systems.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.gdx.*;
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
        
    }
    
    public void update(float delta) {
        if(inputComponent.input.size() > 0) {
            
            FileComponent copy = new FileComponent();
            copy.file = Gdx.files.local(dir.file.path());
            String inputPath = inputComponent.input.poll();
        
            String[] path = inputPath.split("/");
            for(int i = 0; i < path.length; i++) {
                path[i] = path[i].trim();
                if(path[i].equals("..")) {
                    copy.file = copy.file.parent();
                }
                else if(path[i].equals(".")) {
                    copy.file = copy.file;
                }
                else {
                    copy.file = copy.file.child(path[i]);
                }
            }
        
            if(copy.file.exists()) {
                if(copy.file.isDirectory()) {
                    dir.file = copy.file;
                    outputComponent.output.add(dir.file);
                }
                else {
                    outputComponent.output.add("[ " + copy.file.path() + " ] is not a directory.");
                }
            }
            else {
                outputComponent.output.add("Directory [ " + copy.file.path() + " ] does not exist.");
            }
        }
    }
    
    public void removedFromEngine(Engine engine) {
        
    }
}
