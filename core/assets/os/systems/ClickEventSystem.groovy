package os.systems;

import os.components.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.ashley.systems.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.viewport.*;
import com.badlogic.gdx.*;

public class ClickEventSystem extends SortedIteratingSystem {

	private final ComponentMapper<OnMousePressedEventComponent> onPressedMapper;
	private final ComponentMapper<OnMouseReleasedEventComponent> onReleasedMapper;
	
	private final ComponentMapper<PositionComponent> positionMapper;
	private final ComponentMapper<SizeComponent> sizeMapper;
	
	private final ComponentMapper<ViewportComponent> viewportMapper;
	
	private ImmutableArray<Entity> pastEvents, renderEntities;

	private Viewport viewport;
	
	private boolean sentClick = false;

	public ClickEventSystem() {
		super(Family.all(PositionComponent.class, SizeComponent.class).get(), new ZComparator());
		this.onPressedMapper = ComponentMapper.getFor(OnMousePressedEventComponent.class);
		this.onReleasedMapper = ComponentMapper.getFor(OnMouseReleasedEventComponent.class);
		this.positionMapper = ComponentMapper.getFor(PositionComponent.class);
		this.sizeMapper = ComponentMapper.getFor(SizeComponent.class);
	}

	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		this.pastEvents = engine.getEntitiesFor(Family.one(OnMousePressedEventComponent.class, OnMouseReleasedEventComponent.class).get());
		this.renderEntities = engine.getEntitiesFor(Family.all(SpriteBatchComponent.class, ViewportComponent.class).get());
	}
	
	public void update(float delta) {
		for(Entity entity : pastEvents) {
			entity.remove(OnMousePressedEventComponent.class);
			entity.remove(OnMouseReleasedEventComponent.class);
		}
		
		this.viewport = renderEntities.first().getComponent(ViewportComponent.class).viewport;
		
		sentClick = false;
		super.update(delta);
	}
	
	public void processEntity(Entity entity, float delta) {
		if(sentClick) return;
		PositionComponent position = positionMapper.get(entity);
		SizeComponent size = sizeMapper.get(entity);
		
		if(isInside(Gdx.input.getX(), Gdx.input.getY(), position.x, position.y, size.width, size.height)) {
			if(Gdx.input.justTouched()) {
				OnMousePressedEventComponent pressed = new OnMousePressedEventComponent();
				pressed.x = Gdx.input.getX();
				pressed.y = Gdx.input.getY();
				pressed.button = 1;
				entity.add(pressed);
				sentClick = true;
				return;
			}
		}
	}
	
	public void removedFromEngine(Engine engine) {
	
	}
	
	public boolean isInside(float mx, float my, float px, float py, float width, float height) {
		Vector2 v = this.viewport.unproject(new Vector2(mx, my));
		mx = v.x;
		my = v.y;
		System.out.println("(" + mx + "," + my + ") [" + px + "," + py + " | " + width + "," + height + "]");
		if( mx < px || mx > px + width || my < py || my > py + height) return false;
		return true;
	}
	
	private static class ZComparator implements Comparator<Entity> {
		private final ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
		
		@Override
		public int compare(Entity e1, Entity e2) {
			return (int)Math.signum( -pm.get(e1).z + pm.get(e2).z);
		}
	}
}