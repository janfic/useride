package os.systems;

import groovy.transform.CompileStatic;

import os.components.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;

@CompileStatic
public class MoveOnDragSystem extends EntitySystem {
	
    private final ComponentMapper<DraggingComponent> draggingMapper;;
    private final ComponentMapper<PositionComponent> positionMapper;;
    private final ComponentMapper<ViewportComponent> viewportMapper;;
    private final ComponentMapper<DragableComponent> dragableMapper;;
    private final ComponentMapper<MoveOnDragComponent> moveOnDragMapper;;

    private ImmutableArray<Entity> entities, renderEntity, dragging;

    public MoveOnDragSystem() {
        this.draggingMapper = ComponentMapper.getFor(DraggingComponent.class);
        this.positionMapper = ComponentMapper.getFor(PositionComponent.class);
        this.viewportMapper = ComponentMapper.getFor(ViewportComponent.class);
        this.dragableMapper = ComponentMapper.getFor(DragableComponent.class);
    }
	
    public void addedToEngine(Engine engine) {
        this.renderEntity = engine.getEntitiesFor(
            Family.all(
                ViewportComponent.class
            ).get()
        );
        this.dragging = engine.getEntitiesFor(
            Family.all(
                DraggingComponent.class,
                PositionComponent.class,
                DragableComponent.class,
                MoveOnDragComponent.class
            ).get()
        );
    }

    public void update(float delta) {
        if(renderEntity.size() < 1) return;
        ViewportComponent viewport = viewportMapper.get(renderEntity.first());

        Vector2 mouseCoords = viewport.viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        
        for(Entity entity : dragging) {
            DragableComponent dragable = dragableMapper.get(entity)
            PositionComponent position = positionMapper.get(entity);
            DraggingComponent draggingComponent = draggingMapper.get(entity);
            
            position.x += (dragable.horizontal) ? mouseCoords.x - draggingComponent.previousX : 0;
            position.y += (dragable.verticle  ) ? mouseCoords.y - draggingComponent.previousY : 0;

            if(Gdx.input.isTouched() == false) {
                entity.remove(DraggingComponent.class);
            }
        }
    }
}
