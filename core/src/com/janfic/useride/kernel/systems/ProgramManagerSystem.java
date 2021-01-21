package com.janfic.useride.kernel.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.janfic.useride.kernel.components.*;

public class ProgramManagerSystem extends EntitySystem {

    private final ComponentMapper<EngineComponent> engineMapper;
    private final ComponentMapper<IDComponent> idMapper;
    private final ComponentMapper<NameComponent> nameMapper;
    private final ComponentMapper<ClassLoaderComponent> classLoaderMapper;

    private ImmutableArray<Entity> entities;

    public ProgramManagerSystem() {
        this.engineMapper = ComponentMapper.getFor(EngineComponent.class);
        this.idMapper = ComponentMapper.getFor(IDComponent.class);
        this.nameMapper = ComponentMapper.getFor(NameComponent.class);
        this.classLoaderMapper = ComponentMapper.getFor(ClassLoaderComponent.class);
    }

    @Override
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
                Family.all(EngineComponent.class, IDComponent.class, NameComponent.class).get()
        );
    }

    @Override
    public void update(float deltaTime) {
        for (Entity entity : entities) {
            EngineComponent engineComponent = engineMapper.get(entity);
            engineComponent.engine.update(deltaTime);
        }
    }

    @Override
    public void removedFromEngine(Engine engine) {
        this.entities = null;
    }
}
