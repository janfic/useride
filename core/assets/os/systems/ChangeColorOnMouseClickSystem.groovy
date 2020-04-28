package os.systems;

import os.components.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;

public class ChangeColorOnMouseClickSystem extends EntitySystem {
	
	private final ComponentMapper<ChangeColorOnMouseClickComponent> changeMapper;
	private final ComponentMapper<ColorComponent> colorMapper;
	private final ComponentMapper<MouseClickEventComponent> clickMapper;

	private ImmutableArray<Entity> entities;

	public ChangeColorOnMouseClickSystem() {
		this.changeMapper = ComponentMapper.getFor(ChangeColorOnMouseClickComponent.class);
		this.colorMapper = ComponentMapper.getFor(ColorComponent.class);
		this.clickMapper = ComponentMapper.getFor(MouseClickEventComponent.class);
	}
	
	public void addedToEngine(Engine engine) {
		this.entities = engine.getEntitiesFor(
			Family.all(
				ColorComponent.class,
				ChangeColorOnMouseClickComponent.class,
				MouseClickEventComponent.class
			).get()
		);
	}

	public void update(float delta) {
		for(Entity entity : entities) {
			ColorComponent color = colorMapper.get(entity);
			MouseClickEventComponent clickEvent = clickMapper.get(entity);
			ChangeColorOnMouseClickComponent change = changeMapper.get(entity);

			color.color = change.color.cpy();
		}
	}

	public void removedFromEngine(Engine engine) {
	
	}

}