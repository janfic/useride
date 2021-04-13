package os.systems;

import groovy.transform.CompileStatic;

import os.components.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;

public class WidthBetweenSystem extends EntitySystem {
	
    private final ComponentMapper<WidthBetweenComponent> betweenMapper;;
    private final ComponentMapper<SizeComponent> sizeMapper;;
    private final ComponentMapper<PositionComponent> positionMapper;;

    private ImmutableArray<Entity> entities;

    private final Family hasPosition;
    
    public WidthBetweenSystem() {
        this.betweenMapper = ComponentMapper.getFor(WidthBetweenComponent.class);
        this.sizeMapper = ComponentMapper.getFor(SizeComponent.class);
        this.positionMapper = ComponentMapper.getFor(PositionComponent.class);
        this.hasPosition = Family.all(PositionComponent.class).get();
    }
	
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(
                WidthBetweenComponent.class, 
                SizeComponent.class
            )
            .get()
        );
    }

    public void update(float delta) {		
        for(Entity entity : entities) {
            WidthBetweenComponent between = betweenMapper.get(entity);
            SizeComponent size = sizeMapper.get(entity);
            if(hasPosition.matches(between.a) && hasPosition.matches(between.b)) {
                PositionComponent aPosition = positionMapper.get(between.a);
                PositionComponent bPosition = positionMapper.get(between.b);
                
                size.width = Math.abs(aPosition.x - bPosition.x);
            }
        }
    }
}