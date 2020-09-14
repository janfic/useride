package com.janfic.useride.kernel.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.files.FileHandle;
import com.janfic.useride.kernel.components.*;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import java.util.ArrayList;
import java.util.*;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.classgen.GeneratorContext;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.control.customizers.CompilationCustomizer;

/**
 *
 * @author janfc
 */
public class ProgramStartSystem extends EntitySystem {

    private final ComponentMapper<ProgramStartRequestComponent> startRequestMapper;
    private final ComponentMapper<ProgramEntityInjectionComponent> injectionMapper;
    private final ComponentMapper<ProgramArgumentsComponent> argumentMapper;
    private final ComponentMapper<ClassLoaderComponent> loaderMapper;
    private final ComponentMapper<IDComponent> idMapper;
    private final ComponentMapper<FileComponent> fileMapper;

    private static GroovyClassLoader parent = ProgramStartSystem.parent == null ? new GroovyClassLoader() : ProgramStartSystem.parent;

    private ImmutableArray<Entity> entities, loader;

    private int idCount;

    public ProgramStartSystem() {
        this.startRequestMapper = ComponentMapper.getFor(ProgramStartRequestComponent.class);
        this.idMapper = ComponentMapper.getFor(IDComponent.class);
        this.fileMapper = ComponentMapper.getFor(FileComponent.class);
        this.injectionMapper = ComponentMapper.getFor(ProgramEntityInjectionComponent.class);
        this.argumentMapper = ComponentMapper.getFor(ProgramArgumentsComponent.class);
        this.loaderMapper = ComponentMapper.getFor(ClassLoaderComponent.class);
        this.idCount = 0;
    }

    @Override
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
                Family.all(ProgramStartRequestComponent.class, FileComponent.class).get()
        );
        this.loader = engine.getEntitiesFor(Family.all(ClassLoaderComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        if (loader.size() < 1) {
            return;
        }
        ClassLoaderComponent loaderComponent = loaderMapper.get(loader.first());
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
            classLoaderComponent.classLoader = new GroovyClassLoader(loaderComponent.classLoader);
            classLoaderComponent.classLoader.addClasspath(rootProgramDirectory.parent().path());

            IDComponent idComponent = new IDComponent();
            idComponent.id = idCount++;

            NameComponent nameComponent = new NameComponent();
            nameComponent.name = startRequest.name;

            EngineComponent engineComponent = new EngineComponent();
            engineComponent.engine = new Engine();

            Entity en = new Entity();
            en.add(classLoaderComponent);
            engineComponent.engine.addEntity(en);

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
                    Class c = loaderComponent.classLoader.loadClass(rootProgramDirectory.name() + ".components." + component.nameWithoutExtension());
                    System.out.println(c == loaderComponent.classLoader.parseClass(component.file()));
                }

                System.out.println("[ ProgramStartSystem ]: Compiling Systems: " + systems.list(".groovy").length + " Systems files found");
                for (FileHandle system : systems.list(".groovy")) {
                    Class c = loaderComponent.classLoader.loadClass(rootProgramDirectory.name() + ".systems." + system.nameWithoutExtension());
                    System.out.println("Class: " + c + "\t" + (c == loaderComponent.classLoader.parseClass(system.file())));
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
