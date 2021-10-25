package ui.systems;

import os.components.*;
import ui.components.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import com.janfic.useride.kernel.components.ClassLoaderComponent;

public class ECSAttributeSystem extends EntitySystem {
	
    private final ComponentMapper<ECSAttributeComponent> ecsMapper;
    private final ComponentMapper<ClassLoaderComponent> classLoaderMapper;

    private ImmutableArray<Entity> entities, loader;

    public ECSAttributeSystem() {
        this.ecsMapper = ComponentMapper.getFor(ECSAttributeComponent.class);
        this.classLoaderMapper = ComponentMapper.getFor(ClassLoaderComponent.class);
    }
	
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(
                ECSAttributeComponent.class
            ).get()
        );
        this.loader = engine.getEntitiesFor(
            Family.all(
                ClassLoaderComponent.class
            ).get()
        );
    }

    public void update(float delta) {
        if(loader.size() < 1) return;
        ClassLoaderComponent classLoaderComponent = classLoaderMapper.get(loader.first());
        System.out.println(classLoaderComponent.classLoader.getLoadedClasses());
        GroovyShell shell = new GroovyShell(classLoaderComponent.classLoader);
        for( Entity entity : entities) {
            ECSAttributeComponent ecsAttribute = ecsMapper.get(entity);
            Object o = shell.evaluate(ecsAttribute.ecs);
            entity.remove(ECSAttributeComponent.class);
            System.out.println(o);
        }
    }
}

