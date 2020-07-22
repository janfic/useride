package com.janfic.useride.kernel.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.janfic.useride.kernel.components.IDComponent;
import com.janfic.useride.kernel.components.RequestProgramIDComponent;

public class GetProgramIDSystem extends EntitySystem {

    private final ComponentMapper<RequestProgramIDComponent> requestMapper;
    private final ComponentMapper<IDComponent> idMapper;

    private ImmutableArray<Entity> entities;

    public GetProgramIDSystem() {
        this.requestMapper = ComponentMapper.getFor(RequestProgramIDComponent.class);
        this.idMapper = ComponentMapper.getFor(IDComponent.class);
    }

    @Override
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
                Family.all(RequestProgramIDComponent.class).get()
        );
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(float deltaTime) {
        for (Entity entity : entities) {
            RequestProgramIDComponent request = requestMapper.get(entity);
            if (request.program != null) {
                IDComponent id = idMapper.get(request.program);
                if (id != null) {
                    IDComponent i = new IDComponent();
                    i.id = id.id;
                    entity.add(i);
                }
            }
        }
    }

}
