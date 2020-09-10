package os.systems;

import os.components.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;

public class ChangeColorOnMouseHoverSystem extends EntitySystem {
	
	private final ComponentMapper<ChangeColorOnMouseHoverComponent> changeMapper;
	private final ComponentMapper<ColorComponent> colorMapper;
	private final ComponentMapper<MouseHoverEventComponent> hoverMapper;

	private ImmutableArray<Entity> entities, offEntities;

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
		this.offEntities = engine.getEntitiesFor(
			Family.all(
				ColorComponent.class,
				ChangeColorOnMouseHoverComponent.class,
			).exclude(
				MouseHoverEventComponent.class
			)
			.get()
		);

	}

	public void update(float delta) {
		for(Entity entity : entities) {
			ColorComponent color = colorMapper.get(entity);
			MouseHoverEventComponent hoverEvent = hoverMapper.get(entity);
			ChangeColorOnMouseHoverComponent change = changeMapper.get(entity);

			color.color = change.hoverColor.cpy();
		}
		for(Entity entity : offEntities) {
			ColorComponent color = colorMapper.get(entity);
			ChangeColorOnMouseHoverComponent change = changeMapper.get(entity);

			color.color = change.offColor.cpy();
		}
	}

	public void removedFromEngine(Engine engine) {
	
	}

}