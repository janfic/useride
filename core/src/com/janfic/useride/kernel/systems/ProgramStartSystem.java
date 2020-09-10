package com.janfic.useride.kernel.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.files.FileHandle;
import com.janfic.useride.kernel.components.*;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import java.util.ArrayList;
import java.util.*;
import org.codehaus.groovy.control.CompilerConfiguration;

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
            
            System.out.println("[ ProgramStartSystem ]: Starting Program");
            
            FileComponent fileComponent = fileMapper.get(entity);
            ProgramStartRequestComponent startRequest = startRequestMapper.get(entity);
            ProgramEntityInjectionComponent entityInjection = injectionMapper.get(entity);
            
            System.out.println("[ ProgramStartSystem ]: Program Name = " + startRequest.name);
            
            FileHandle rootProgramDirectory = fileComponent.file;
            
            FileHandle components = rootProgramDirectory.child("components");
            FileHandle systems = rootProgramDirectory.child("systems");
            
            System.out.println("[ ProgramStartSystem ]: Analyzing System Structure.. ");
            
            if (!rootProgramDirectory.exists() || !components.exists() || !systems.exists()) {
                System.out.println(
                        "[ ProgramStartSystem ] [ERROR] : A folder is missing - ABORTING Program Startup"
                );
                continue;
            }
            System.out.println("[ ProgramStartSystem ]: Correct Structure Found ");
            
            System.out.println("[ ProgramStartSystem ]: Creating Program Entity Components.. ");
            ClassLoaderComponent classLoaderComponent = new ClassLoaderComponent();
            classLoaderComponent.classLoader = new GroovyClassLoader(parent);
            
            classLoaderComponent.classLoader.setShouldRecompile(Boolean.TRUE);
            classLoaderComponent.classLoader.addClasspath(rootProgramDirectory.parent().path());
            
            IDComponent idComponent = new IDComponent();
            idComponent.id = idCount++;
            
            NameComponent nameComponent = new NameComponent();
            nameComponent.name = startRequest.name;
            
            EngineComponent engineComponent = new EngineComponent();
            engineComponent.engine = new Engine();
            System.out.println("[ ProgramStartSystem ]: Created Successfully ");
            
            if (entityInjection != null) {
                System.out.println("[ ProgramStartSystem ]: Entity Injection Component found - injecting into program");
                Entity e = new Entity();
                e.add(entityInjection);
                engineComponent.engine.addEntity(e);
            }
            
            ProgramArgumentsComponent arguments = argumentMapper.get(entity);
            if (arguments != null) {
                System.out.println("[ ProgramStartSystem ]: ProgramArgumentsComponent found - adding to program");
                Entity e = new Entity();
                e.add(arguments);
            }
            
            try {
                System.out.println("[ ProgramStartSystem ]: Compiling Components: " + components.list(".groovy").length + " Component files found");
                for (FileHandle component : components.list(".groovy")) {
                    GroovyCodeSource source = new GroovyCodeSource(component.file());
                    source.setCachable(true);
                    
                    Class c = classLoaderComponent.classLoader.parseClass(source, true);
                }
                
                System.out.println("[ ProgramStartSystem ]: Compiling Systems: " + systems.list(".groovy").length + " Systems files found");
                for (FileHandle system : systems.list(".groovy")) {
                    GroovyCodeSource source = new GroovyCodeSource(system.file());
                    
                    Class c = classLoaderComponent.classLoader.parseClass(source, true);
                    //rootProgramDirectory.nameWithoutExtension() + ".systems." + system.nameWithoutExtension(), true, true);
                    if (system.nameWithoutExtension().equals("BootSystem")) {
                        EntitySystem bootSystem = (EntitySystem) c.getConstructors()[0].newInstance();
                        engineComponent.engine.addSystem(bootSystem);
                    }
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            System.out.println("[ ProgramStartSystem ]: Program Successfully Started!");
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
    
    public static class USERIDEClassLoader extends GroovyClassLoader {
        
        public USERIDEClassLoader(CompilerConfiguration config) {
            super(new GroovyClassLoader(), config, true);
        }
        
        public String[] classPaths() {
            System.out.println(this.classCache);
            return this.getClassPath();
        }
        
        public long time(Class c) {
            return this.getTimeStamp(c);
        }
    }
}
