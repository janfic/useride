package os.systems;

import os.components.*;
import com.janfic.useride.kernel.components.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;

public class ProgramStartOnMouseClickSystem extends EntitySystem {
	
	private final ComponentMapper<ProgramStartOnMouseClickComponent> programStartMapper;
	private final ComponentMapper<MouseClickEventComponent> clickMapper;

	private ImmutableArray<Entity> entities;

	public ProgramStartOnMouseClickSystem() {
		this.programStartMapper = ComponentMapper.getFor(ProgramStartOnMouseClickComponent.class);
		this.clickMapper = ComponentMapper.getFor(MouseClickEventComponent.class);
	}
	
	public void addedToEngine(Engine engine) {
		this.entities = engine.getEntitiesFor(
			Family.all(
				ProgramStartOnMouseClickComponent.class,
				ClickableComponent.class,
				MouseClickEventComponent.class
			).get()
		);
	}

	public void update(float delta) {
		for(Entity entity : entities) {
			ProgramStartOnMouseClickComponent start = programStartMapper.get(entity);
			MouseClickEventComponent click = clickMapper.get(entity);

			if(click.count >= 2 && start.started == false) {
				ProgramStartRequestComponent startRequest = new ProgramStartRequestComponent(name: start.name);
				FileLoadRequestComponent fileRequest = new FileLoadRequestComponent(fileName: start.path);

				Entity program = new Entity();
				program.add(startRequest);
				program.add(fileRequest);
			
				start.started = true;
				this.getEngine().addEntity(program);
			}
		}
	}

	public void removedFromEngine(Engine engine) {
	
	}

}