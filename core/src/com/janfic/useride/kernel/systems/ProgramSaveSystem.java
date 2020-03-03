package com.janfic.useride.kernel.systems;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.janfic.useride.kernel.components.EngineComponent;
import com.janfic.useride.kernel.components.FileComponent;
import com.janfic.useride.kernel.components.IDComponent;
import com.janfic.useride.kernel.components.ProgramSaveRequestComponent;

public class ProgramSaveSystem extends EntitySystem {

    private final ComponentMapper<EngineComponent> engineMapper;
    private final ComponentMapper<IDComponent> idMapper;
    private final ComponentMapper<FileComponent> fileMapper;
    private final ComponentMapper<ProgramSaveRequestComponent> requestMapper;

    private ImmutableArray<Entity> programs, requests;

    private final Json json;

    public ProgramSaveSystem() {
        this.engineMapper = ComponentMapper.getFor(EngineComponent.class);
        this.idMapper = ComponentMapper.getFor(IDComponent.class);
        this.fileMapper = ComponentMapper.getFor(FileComponent.class);
        this.requestMapper = ComponentMapper.getFor(ProgramSaveRequestComponent.class);
        this.json = new Json(JsonWriter.OutputType.json);
    }

    @Override
    public void addedToEngine(Engine engine) {
        programs = engine.getEntitiesFor(Family.all(EngineComponent.class, IDComponent.class, FileComponent.class).get());
        requests = engine.getEntitiesFor(Family.all(ProgramSaveRequestComponent.class, FileComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (Entity requestEntity : requests) {
            ProgramSaveRequestComponent saveRequest = requestMapper.get(requestEntity);
            FileComponent fileComponent = fileMapper.get(requestEntity);

            int id = saveRequest.id;

            for (Entity programEntity : programs) {
                IDComponent idComponent = idMapper.get(programEntity);
                if (id == idComponent.id) {
                    EngineComponent engineComponent = engineMapper.get(programEntity);
                    save(engineComponent.engine, fileComponent.file);
                    requestEntity.remove(ProgramSaveRequestComponent.class);
                }
            }
        }
    }

    private void save(Engine engine, FileHandle file) {
        for (int i = 0; i < engine.getEntities().size(); i++) {
            Entity entity = engine.getEntities().get(i);
            FileHandle entityFolder = file.child("entity" + i);
            for (int j = 0; j < entity.getComponents().size(); j++) {
                Component component = entity.getComponents().get(j);
                FileHandle componentFile = entityFolder.child(component.getClass().getName() + ".json");
                componentFile.writeString(json.toJson(component, component.getClass()), false);
            }
        }
    }

    @Override
    public void removedFromEngine(Engine engine) {
        programs = null;
        requests = null;
    }
}
