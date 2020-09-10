package com.janfic.useride.kernel.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.janfic.useride.kernel.components.*;

/**
 * This System starts the program by adding all the required systems and
 * entities to the engine.
 *
 * @author janfic
 */
public class BootSystem extends EntitySystem {

    @Override
    public void addedToEngine(Engine engine) {
        engine.addSystem(new FileLoadSystem());
        engine.addSystem(new FileSaveSystem());
        engine.addSystem(new ProgramStartSystem());
        engine.addSystem(new ProgramEndSystem());
        engine.addSystem(new ProgramSaveSystem());
        engine.addSystem(new ProgramLoadSystem());
        engine.addSystem(new ProgramManagerSystem());

        Entity entity = new Entity();

        FileLoadRequestComponent loadRequest = new FileLoadRequestComponent();
        loadRequest.fileName = "os";

        ProgramStartRequestComponent startRequest = new ProgramStartRequestComponent();
        startRequest.name = "OS";

        entity.add(loadRequest);
        entity.add(startRequest);

        engine.addEntity(entity);
        
        engine.removeSystem(this);
    }
}
