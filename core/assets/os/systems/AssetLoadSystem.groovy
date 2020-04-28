package os.systems;

import os.components;
import com.badlogic.gdx.assets.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;

public class AssetLoadSystem extends EntitySystem {
	
	private final ComponentMapper<LoadAssetsComponent> loadMapper;
	private final ComponentMapper<LoadAllAssetsComponent> loadAllMapper;
	private final ComponentMapper<AssetManagerComponent> assetManagerMapper;

	private ImmutableArray<Entity> assetManagers, loadEntities, loadAllEntities;

	public AssetLoadSystem() {
		this.loadMapper = ComponentMapper.getFor(LoadAssetsComponent.class);
		this.loadAllMapper = ComponentMapper.getFor(LoadAllAssetsComponent.class);
		this.assetManagerMapper = ComponentMapper.getFor(AssetManagerComponent.class);
	}
	
	public void addedToEngine(Engine engine) {
		this.loadEntities = engine.getEntitiesFor(
			Family.all(LoadAssetsComponent.class).get()
		);
		this.loadAllEntities = engine.getEntitiesFor(
			Family.all(LoadAllAssetsComponent.class).get()
		);
		this.assetManagers = engine.getEntitiesFor(
			Family.all(AssetManagerComponent.class).get()
		);
	}

	public void update(float delta) {
		if(assetManagers.size() < 1) return;
		AssetManagerComponent assetManager = assetManagerMapper.get(assetManagers.first());

		if(loadEntities.size() > 0) assetManager.manager.update();

		for(Entity entity : loadEntities) {
			LoadAssetsComponent load = loadMapper.get(entity);
			load.progress = assetManager.getProgress();
		}

		if(loadAllEntities.size() > 0) assetManager.manager.finishLoading();

		for(Entity entity : loadAllEntities) {
			entity.remove(LoadAllAssetsComponent.class);
		}
	}

	public void removedFromEngine(Engine engine) {
	
	}

}