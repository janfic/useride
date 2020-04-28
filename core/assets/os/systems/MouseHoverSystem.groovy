package os.systems;

import os.components.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.viewport.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.ashley.systems.*;

public class MouseHoverSystem extends SortedIteratingSystem {
	
	private final ComponentMapper<HoverableComponent> hoverableMapper;
	private final ComponentMapper<HitBoxComponent> hitboxMapper;
	private final ComponentMapper<PositionComponent> positionMapper;
	private final ComponentMapper<ViewportComponent> viewportMapper;

	private ImmutableArray<Entity> renderEntity, oldEvents;

	private Vector2 mouseCoords;

	public MouseHoverSystem() {
		super(Family.all(PositionComponent.class, HoverableComponent.class, HitBoxComponent.class).get(), new ZComparator());
		this.hoverableMapper = ComponentMapper.getFor(HoverableComponent.class);
		this.hitboxMapper = ComponentMapper.getFor(HitBoxComponent.class);
		this.positionMapper = ComponentMapper.getFor(PositionComponent.class);
		this.viewportMapper = ComponentMapper.getFor(ViewportComponent.class);
	}
	
	public void addedToEngine(Engine engine) {
		this.oldEvents = engine.getEntitiesFor(
			Family.all(MouseHoverEventComponent.class).get()
		);
		this.renderEntity = engine.getEntitiesFor(
			Family.all(ViewportComponent.class, SpriteBatchComponent.class).get()
		);
	}

	public void update(float delta) {

		for(Entity entity : oldEvents) {
			entity.remove(MouseHoverEventComponent.class);
		}

		if(renderEntity.size() < 1) return;
		ViewportComponent viewportComponent = viewportMapper.get(renderEntity.first());
		
		this.mouseCoords = getViewportCoords(viewportComponent.viewport, Gdx.input.getX(), Gdx.input.getY());

		super.update(delta);
	}

	
	public void processEntity(Entity entity, float delta) {
		PositionComponent position = positionMapper.get(entity);
		HitBoxComponent hitBox = hitboxMapper.get(entity);

		Vector2 temp = new Vector2(hitBox.rectangle.getX(), hitBox.rectangle.getY());

		hitBox.setPosition(temp.x + position.x, temp.y + position.y);

		if(hitBox.contains(this.mouseCoords)) {
			MouseClickEventComponent clickEvent = new MouseClickEventComponent();
			clickEvent.x = this.mouseCoords.x;
			clickEvent.y = this.mouseCoords.y;

			entity.add(clickEvent);
		}

		hitBox.setPosition(temp);
	}

	public Vector2 getViewportCoords(Viewport viewport, float mx, float my) {
		return viewport.unproject(new Vector2(mx, my));
	}

	private static class ZComparator implements Comparator<Entity> {
		private final ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
		
		@Override
		public int compare(Entity e1, Entity e2) {
			return (int)Math.signum( -pm.get(e1).z + pm.get(e2).z);
		}
	}
}