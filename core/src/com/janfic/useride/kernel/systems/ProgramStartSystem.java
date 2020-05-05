package com.janfic.useride.kernel.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.files.FileHandle;
import com.janfic.useride.kernel.components.*;
import groovy.lang.GroovyClassLoader;
import java.util.Arrays;

/**
 *
 * @author janfc
 */
public class ProgramStartSystem extends EntitySystem {

    private final ComponentMapper<ProgramStartRequestComponent> startRequestMapper;
    private final ComponentMapper<ProgramEntityInjectionComponent> injectionMapper;
    private final ComponentMapper<ProgramArgumentsComponent> argumentMapper;
    private final ComponentMapper<IDComponent> idMapper;
    private final ComponentMapper<FileComponent> fileMapper;

    private static GroovyClassLoader parent = ProgramStartSystem.parent == null ? new GroovyClassLoader() : ProgramStartSystem.parent;

    private ImmutableArray<Entity> entities;

    private int idCount;

    public ProgramStartSystem() {
        this.startRequestMapper = ComponentMapper.getFor(ProgramStartRequestComponent.class);
        this.idMapper = ComponentMapper.getFor(IDComponent.class);
        this.fileMapper = ComponentMapper.getFor(FileComponent.class);
        this.injectionMapper = ComponentMapper.getFor(ProgramEntityInjectionComponent.class);
        this.argumentMapper = ComponentMapper.getFor(ProgramArgumentsComponent.class);
        this.idCount = 0;
    }

    @Override
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
                Family.all(ProgramStartRequestComponent.class, FileComponent.class).get()
        );
    }

    @Override
    public void update(float deltaTime) {
        for (Entity entity : entities) {

            FileComponent fileComponent = fileMapper.get(entity);
            ProgramStartRequestComponent startRequest = startRequestMapper.get(entity);
            ProgramEntityInjectionComponent entityInjection = injectionMapper.get(entity);

            FileHandle rootProgramDirectory = fileComponent.file;

            FileHandle components = rootProgramDirectory.child("components");
            FileHandle systems = rootProgramDirectory.child("systems");

            if (!rootProgramDirectory.exists() || !components.exists() || !systems.exists()) {
                System.out.println(rootProgramDirectory.exists());
                System.out.println(components.exists());
                System.out.println(systems.exists());
                System.out.println(
                        "A folder is missing"
                );
            }

            ClassLoaderComponent classLoaderComponent = new ClassLoaderComponent();
            classLoaderComponent.classLoader = parent;

            classLoaderComponent.classLoader.setShouldRecompile(Boolean.FALSE);
            System.out.println("Recompile: " + Arrays.toString(classLoaderComponent.classLoader.getLoadedClasses()));
            classLoaderComponent.classLoader.addClasspath(rootProgramDirectory.parent().path());

            IDComponent idComponent = new IDComponent();
            idComponent.id = idCount++;

            NameComponent nameComponent = new NameComponent();
            nameComponent.name = startRequest.name;

            EngineComponent engineComponent = new EngineComponent();
            engineComponent.engine = new Engine();

            if (entityInjection != null) {
                Entity e = new Entity();
                e.add(entityInjection);
                engineComponent.engine.addEntity(e);
            }

            ProgramArgumentsComponent arguments = argumentMapper.get(entity);
            if (arguments != null) {
                Entity e = new Entity();
                e.add(arguments);
            }

            try {
                for (FileHandle component : components.list(".groovy")) {
                    Class c = classLoaderComponent.classLoader.loadClass(
                            rootProgramDirectory.nameWithoutExtension() + ".components." + component.nameWithoutExtension());
                }

                for (FileHandle system : systems.list(".groovy")) {
                    Class c = classLoaderComponent.classLoader.loadClass(
                            rootProgramDirectory.nameWithoutExtension() + ".systems." + system.nameWithoutExtension());
                    if (system.nameWithoutExtension().equals("BootSystem")) {
                        EntitySystem bootSystem = (EntitySystem) c.newInstance();
                        engineComponent.engine.addSystem(bootSystem);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("Recompile: " + Arrays.toString(classLoaderComponent.classLoader.getLoadedClasses()));

            entity.add(engineComponent);
            entity.add(idComponent);
            entity.add(classLoaderComponent);
            entity.add(nameComponent);

            entity.remove(ProgramStartRequestComponent.class);
        }
    }

    @Override
    public void removedFromEngine(Engine engine
    ) {
        super.removedFromEngine(engine); //To change body of generated methods, choose Tools | Templates.
    }

}
