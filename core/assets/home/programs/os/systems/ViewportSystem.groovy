package os.systems;

import groovy.transform.CompileStatic;

import os.components.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.viewport.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.*;
import com.badlogic.ashley.utils.*;

@CompileStatic
public class ViewportSystem extends EntitySystem {
	
    private final ComponentMapper<ViewportComponent> viewportMapper;
    private final ComponentMapper<SizeComponent> sizeMapper;
    private final ComponentMapper<PositionComponent> positionMapper;

    private ImmutableArray<Entity> entities;

    private Vector2 mouseCoords;

    public ViewportSystem() {
        this.sizeMapper = ComponentMapper.getFor(SizeComponent.class);
        this.positionMapper = ComponentMapper.getFor(PositionComponent.class);
        this.viewportMapper = ComponentMapper.getFor(ViewportComponent.class);
    }
	
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(
                ViewportComponent.class, 
                PositionComponent.class, 
                SizeComponent.class
            ).get()
        );
    }

    public void update(float delta) {
        for(Entity entity : entities) {
            PositionComponent position = positionMapper.get(entity);
            SizeComponent size = sizeMapper.get(entity);
            ViewportComponent viewportComponent = viewportMapper.get(entity);

            viewportComponent.viewport.setScreenBounds((int) position.x, (int) position.y, (int) size.width,(int) size.height);
        }
    }
}