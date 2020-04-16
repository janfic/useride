package com.janfic.useride.kernel.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.janfic.useride.kernel.components.*;
import groovy.lang.GroovyClassLoader;

public class ProgramLoadSystem extends EntitySystem {

    private final ComponentMapper<EngineComponent> engineMapper;
    private final ComponentMapper<ProgramLoadRequestComponent> requestMapper;
    private final ComponentMapper<FileComponent> fileMapper;
    private final ComponentMapper<ClassLoaderComponent> classLoaderMapper;
    private final ComponentMapper<IDComponent> idMapper;

    private ImmutableArray<Entity> requests, programs;

    private final Json json;

    public ProgramLoadSystem() {
        this.engineMapper = ComponentMapper.getFor(EngineComponent.class);
        this.requestMapper = ComponentMapper.getFor(ProgramLoadRequestComponent.class);
        this.fileMapper = ComponentMapper.getFor(FileComponent.class);
        this.classLoaderMapper = ComponentMapper.getFor(ClassLoaderComponent.class);
        this.idMapper = ComponentMapper.getFor(IDComponent.class);
        this.json = new Json(JsonWriter.OutputType.json);
    }

    @Override
    public void addedToEngine(Engine engine) {
        requests = engine.getEntitiesFor(Family.all(FileComponent.class, ProgramLoadRequestComponent.class).get());
        programs = engine.getEntitiesFor(
                Family.all(
                        ClassLoaderComponent.class,
                        EngineComponent.class,
                        IDComponent.class,
                        FileComponent.class
                ).get()
        );
    }

    @Override
    public void update(float deltaTime) {
        for (Entity requestEntity : requests) {
            ProgramLoadRequestComponent request = requestMapper.get(requestEntity);
            FileComponent fileComponent = fileMapper.get(requestEntity);

            for (Entity programEntity : programs) {
                IDComponent idComponent = idMapper.get(programEntity);
                EngineComponent engineComponent = engineMapper.get(programEntity);
                ClassLoaderComponent loaderComponent = classLoaderMapper.get(programEntity);

                if (request.id == idComponent.id) {
                    try {
                        load(fileComponent.file, engineComponent.engine = new Engine(), loaderComponent.classLoader);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    requestEntity.remove(ProgramLoadRequestComponent.class);
                }
            }
        }
    }

    private void load(FileHandle saveRoot, Engine engine, GroovyClassLoader loader) throws ClassNotFoundException {

        FileHandle entitiesFolder = saveRoot.child("entities");
        FileHandle systemsFolder = saveRoot.child("systems");

        for (FileHandle entityFile : entitiesFolder.list()) {
            Entity entity = new Entity();
            for (FileHandle componentFile : entityFile.list(".json")) {
                Class componentClass = loader.loadClass(componentFile.nameWithoutExtension());
                Component c = (Component) json.fromJson(componentClass, componentFile);
                entity.add(c);
            }
            engine.addEntity(entity);
        }
        for (FileHandle system : systemsFolder.list(".json")) {
            try {
                Class systemClass = loader.loadClass(system.nameWithoutExtension());
                EntitySystem s = (EntitySystem) json.fromJson(systemClass, system);
                engine.addSystem(s);
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine); //To change body of generated methods, choose Tools | Templates.
    }

}
