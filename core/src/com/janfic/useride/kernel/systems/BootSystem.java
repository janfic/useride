package com.janfic.useride.kernel.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.janfic.useride.kernel.components.FileLoadRequestComponent;


/**
 * This System starts the program by adding all the required systems and
 * entities to the engine.
 *
 * @author janfc
 */
public class BootSystem extends EntitySystem {
    @Override
    public void addedToEngine(Engine engine) {
        FileLoadSystem fileLoadSystem = new FileLoadSystem();
        FileSaveSystem fileSaveSystem = new FileSaveSystem();
        engine.addSystem(fileLoadSystem);
        engine.addSystem(fileSaveSystem);
        
        Entity entity = new Entity();
        
        FileLoadRequestComponent loadRequest = new FileLoadRequestComponent();
        loadRequest.fileName = "test.txt";
        
        
        entity.add(loadRequest);
        engine.addEntity(entity);
        
        engine.removeSystem(this);
    }
}
