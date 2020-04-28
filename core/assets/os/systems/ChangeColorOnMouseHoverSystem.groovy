package os.systems;

import os.components.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;

public class ChangeColorOnMouseHoverSystem extends EntitySystem {
	
	private final ComponentMapper<ChangeColorOnMouseHoverComponent> changeMapper;
	private final ComponentMapper<ColorComponent> colorMapper;
	private final ComponentMapper<MouseHoverEventComponent> hoverMapper;

	private ImmutableArray<Entity> entities;

	public ChangeColorOnMouseHoverSystem() {
		this.changeMapper = ComponentMapper.getFor(ChangeColorOnMouseHoverComponent.class);
		this.colorMapper = ComponentMapper.getFor(ColorComponent.class);
		this.hoverMapper = ComponentMapper.getFor(MouseHoverEventComponent.class);
	}
	
	public void addedToEngine(Engine engine) {
		this.entities = engine.getEntitiesFor(
			Family.all(
				ColorComponent.class,
				ChangeColorOnMouseHoverComponent.class,
				MouseHoverEventComponent.class
			).get()
		);
	}

	public void update(float delta) {
		for(Entity entity : entities) {
			ColorComponent color = colorMapper.get(entity);
			MouseHoverEventComponent hoverEvent = hoverMapper.get(entity);
			ChangeColorOnMouseHoverComponent change = changeMapper.get(entity);

			color.color = change.color.cpy();
		}
	}

	public void removedFromEngine(Engine engine) {
	
	}

}