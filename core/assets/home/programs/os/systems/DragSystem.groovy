package os.systems;

import groovy.transform.CompileStatic;

import os.components.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;

@CompileStatic
public class DragSystem extends EntitySystem {
	
	private final ComponentMapper<DraggingComponent> draggingMapper;;
	private final ComponentMapper<PositionComponent> positionMapper;;
	private final ComponentMapper<ViewportComponent> viewportMapper;;
	private final ComponentMapper<MousePressEventComponent> pressMapper;;

	private ImmutableArray<Entity> entities, renderEntity, dragging;

	public DragSystem() {
		this.draggingMapper = ComponentMapper.getFor(DraggingComponent.class);
		this.positionMapper = ComponentMapper.getFor(PositionComponent.class);
		this.viewportMapper = ComponentMapper.getFor(ViewportComponent.class);
		this.pressMapper = ComponentMapper.getFor(MousePressEventComponent.class);
	}
	
	public void addedToEngine(Engine engine) {
		this.renderEntity = engine.getEntitiesFor(
			Family.all(
				ViewportComponent.class
			).get()
		);
		this.entities = engine.getEntitiesFor(
			Family.all(
				MousePressEventComponent.class, 
				DragableComponent.class,
				PositionComponent.class
			)
			.get()
		);
		this.dragging = engine.getEntitiesFor(
			Family.all(
				DraggingComponent.class,
				PositionComponent.class,
				DragableComponent.class
			).get()
		);
	}

	public void update(float delta) {
		if(renderEntity.size() < 1) return;
		ViewportComponent viewport = viewportMapper.get(renderEntity.first());

		Vector2 mouseCoords = viewport.viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));


		for(Entity entity : entities) {
			MousePressEventComponent press = pressMapper.get(entity);
			if(press.timer >= 0.25f) {
				entity.add(new DraggingComponent(previousX: mouseCoords.x, previousY: mouseCoords.y));
			}
		}
		
		for(Entity entity : dragging) {
			PositionComponent position = positionMapper.get(entity);
			DraggingComponent draggingComponent = draggingMapper.get(entity);

			position.x += mouseCoords.x - draggingComponent.previousX;
			position.y += mouseCoords.y - draggingComponent.previousY;

			draggingComponent.previousX = mouseCoords.x;
			draggingComponent.previousY = mouseCoords.y;

			if(Gdx.input.isTouched() == false) {
				entity.remove(DraggingComponent.class);
			}
		}
	}
}