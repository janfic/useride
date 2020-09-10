package os.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.gdx.*;
import os.components.*;

public class StageSystem extends EntitySystem {
	
	public final ComponentMapper<StageComponent> stageMapper;
	
	private ImmutableArray<Entity> entities;
	
	public StageSystem() {
		this.stageMapper = ComponentMapper.getFor(StageComponent.class);
	}
	
	public void addedToEngine(Engine engine) {
		this.entities = engine.getEntitiesFor(
			Family.all(
				StageComponent.class
			).get()
		);
	}
	
	public void update(float delta) {
		for( Entity entity : entities) {
			StageComponent stageComponent = stageMapper.get(entity);
			stageComponent.stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
			stageComponent.stage.getViewport().apply();
			stageComponent.stage.act(delta);
			stageComponent.stage.draw();
		}
	}
	
	public void removedFromEngine(Engine engine) {
	
	}
}