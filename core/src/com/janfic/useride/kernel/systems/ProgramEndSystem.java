package com.janfic.useride.kernel.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.janfic.useride.kernel.components.*;

public class ProgramEndSystem extends EntitySystem {

    private final ComponentMapper<EngineComponent> engineMapper;
    private final ComponentMapper<IDComponent> idMapper;
    private final ComponentMapper<NameComponent> nameMapper;
    private final ComponentMapper<ClassLoaderComponent> classLoaderMapper;
    private final ComponentMapper<ProgramEndRequestComponent> programEndRequestMapper;

    private ImmutableArray<Entity> entities, programs;

    public ProgramEndSystem() {
        this.engineMapper = ComponentMapper.getFor(EngineComponent.class);
        this.idMapper = ComponentMapper.getFor(IDComponent.class);
        this.nameMapper = ComponentMapper.getFor(NameComponent.class);
        this.classLoaderMapper = ComponentMapper.getFor(ClassLoaderComponent.class);
        this.programEndRequestMapper = ComponentMapper.getFor(ProgramEndRequestComponent.class);
    }

    @Override
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(Family.all(ProgramEndRequestComponent.class).get());
        this.programs = engine.getEntitiesFor(
                Family.all(
                        IDComponent.class,
                        NameComponent.class,
                        EngineComponent.class,
                        ClassLoaderComponent.class
                ).get()
        );
    }

    @Override
    public void update(float deltaTime) {
        for (Entity entity : entities) {
            ProgramEndRequestComponent endRequest = programEndRequestMapper.get(entity);
            for (Entity program : programs) {
                NameComponent nameComponent = nameMapper.get(program);
                IDComponent idComponent = idMapper.get(program);
                if (endRequest.programName.equals(nameComponent.name) && idComponent.id == endRequest.id) {
                    System.out.println("[ ProgramEndSystem ]: Ended Program: " + nameComponent.name);
                    program.remove(EngineComponent.class);
                    program.remove(IDComponent.class);
                    program.remove(ClassLoaderComponent.class);
                    program.remove(NameComponent.class);
                }
            }

            entity.remove(ProgramEndRequestComponent.class);
        }
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine); //To change body of generated methods, choose Tools | Templates.
    }

}
