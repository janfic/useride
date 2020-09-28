package os.systems;

import os.components.*;
import groovy.transform.CompileStatic;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.viewport.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.ashley.systems.*;

public class FocusOnMouseClickSystem extends EntitySystem {
    
    private final ComponentMapper<FocusableComponent> focusableMapper;
    private final ComponentMapper<MousePressEventComponent> clickMapper;

    private ImmutableArray<Entity> entities, focused;

    public FocusOnMouseClickSystem() {
        this.focusableMapper = ComponentMapper.getFor(FocusableComponent.class);
        this.clickMapper = ComponentMapper.getFor(MousePressEventComponent.class);
    }
	
    public void addedToEngine(Engine engine) {
        this.focused = engine.getEntitiesFor(
            Family.all(FocusedComponent.class).get()
        );
        this.entities = engine.getEntitiesFor(
            Family.all(FocusableComponent.class, MousePressEventComponent.class, FocusOnMouseClickComponent.class).get()
        );
    }

    public void update(float delta) {
        
        if(Gdx.input.justTouched()) {
            for(Entity entity : focused) {
                entity.remove(FocusedComponent.class);
            }
        }

        for(Entity entity : entities) {
            entity.add(new FocusedComponent());
        }
    }
}