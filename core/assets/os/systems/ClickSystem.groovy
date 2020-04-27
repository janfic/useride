package os.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.*;
import os.components.*;

public class ClickSystem extends SortedIteratingSystem {
	
	private final ComponentMapper<ClickableComponent> clickableMapper;
	private final ComponentMapper<PositionComponent> positionMapper;
	
	private final ComponentMapper<ViewportComponent> viewportMapper;
	
	private ImmutableArray<Entity> renderEntities;
	
	private Viewport viewport;
	private Vector2 viewportCoords;
	
	public ClickSystem() {
		super(Family.all(ClickableComponent.class, PositionComponent.class).get(), new ZComparator());
		this.clickableMapper = ComponentMapper.getFor(ClickableComponent.class);
		this.positionMapper = ComponentMapper.getFor(PositionComponent.class);
		this.viewportMapper = ComponentMapper.getFor(ViewportComponent.class);
	}
	
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		this.renderEntities = engine.getEntitiesFor(
			Family.all(SpriteBatchComponent.class, ViewportComponent.class).get()
		);
	}
	
	public void update(float delta) {
		if( renderEntities.size() > 0) {
			this.viewport = viewportMapper.get(renderEntities.get(0)).viewport;
		}
		
		mouseViewportCoords = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
		super.update(delta);
	}
	
	public void processEntity(Entity entity, float delta) {
		PositionComponent position = positionMapper.get(entity);
		ClickableComponent clickable = clickableMapper.get(entity);
		
		if(clickable.shape.contains(mouseViewportCoords.getX(), mouseViewportCoords.getY())) {
			ClickEnventComponent clickEvent = new ClickEnventComponent();
			clickEvent.x = mouseViewportCoords.getX();
			clickEvent.y = mouseViewportCoords.getY();
			
			entity.add(clickEvent);
		}
	}
	
	private static class ZComparator implements Comparator<Entity> {
		private final ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
		
		@Override
		public int compare(Entity e1, Entity e2) {
			return (int)Math.signum( -pm.get(e1).z + pm.get(e2).z);
		}
	}
}