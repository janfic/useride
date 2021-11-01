package ui.systems;

import os.components.*;
import ui.components.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
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
        CompilerConfiguration compilerConfiguration = new CompilerConfiguration();
        ImportCustomizer importCustomizer = new ImportCustomizer();
        importCustomizer.addStarImports("com.badlogic.ashley.core", "com.badlogic.ashley.utils", "com.badlogic.gdx", "com.badlogic.gdx.graphics", "com.badlogic.gdx.graphics.g2d");
        GroovyClassLoader loader = classLoaderComponent.classLoader;
        while(loader != null && loader instanceof GroovyClassLoader) {
            for(Class c : loader.getLoadedClasses()) {
                importCustomizer.addImports(c.getName());
            }
            if(loader.getParent() instanceof GroovyClassLoader) {
                loader = loader.getParent();
            }
            else {
                break;
            }
        }
        compilerConfiguration.addCompilationCustomizers(importCustomizer);
        GroovyShell shell = new GroovyShell(classLoaderComponent.classLoader, compilerConfiguration);
        for( Entity entity : entities) {
            ECSAttributeComponent ecsAttribute = ecsMapper.get(entity);
            Object o = shell.evaluate(ecsAttribute.ecs);
            entity.remove(ECSAttributeComponent.class);
            List<? extends Component> components = (List<? extends Component>) o;
            for( Component c : components) { 
                entity.add(c);
            }
        }
    }
}

