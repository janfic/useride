package os.systems;

import os.components;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.gdx.assets.*;
import com.badlogic.gdx.audio.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;

public class AssetRegisterSystem extends EntitySystem {
	
	private final ComponentMapper<RegisterTextureAssetComponent> registerTextureMapper;
	private final ComponentMapper<RegisterTextureAtlasAssetComponent> registerAtlasMapper;
	private final ComponentMapper<RegisterSoundAssetComponent> registerSoundMapper;
	private final ComponentMapper<RegisterNinePatchAssetComponent> registerNinePatchMapper;
	private final ComponentMapper<RegisterBitmapFontAssetComponent> registerFontMapper;

	private final ComponentMapper<AssetManagerComponent> assetManagerMapper;

	private ImmutableArray<Entity> entities, assetManagerEntities;

	public AssetRegisterSystem() {
		this.registerTextureMapper = ComponentMapper.getFor(RegisterTextureAssetComponent.class);
		this.registerAtlasMapper = ComponentMapper.getFor(RegisterTextureAtlasAssetComponent.class);
		this.registerSoundMapper = ComponentMapper.getFor(RegisterSoundAssetComponent.class);
		this.registerNinePatchMapper = ComponentMapper.getFor(RegisterNinePatchAssetComponent.class);
		this.registerFontMapper = ComponentMapper.getFor(RegisterBitmapFontAssetComponent.class);
		this.assetManagerMapper = ComponentMapper.getFor(AssetManagerComponent.class);
	}
	
	public void addedToEngine(Engine engine) {
		this.entities = engine.getEntitiesFor(
			Family.one(
				RegisterTextureAssetComponent.class,
				RegisterTextureAtlasAssetComponent.class,
				RegisterSoundAssetComponent.class,
				RegisterNinePatchAssetComponent.class,
				RegisterBitmapFontAssetComponent.class
			).get()
		);
		this.assetManagerEntities = engine.getEntitiesFor(
			Family.all(AssetManagerComponent.class).get()
		)
	}	

	public void update(float delta) {
		if(assetManagerEntities.size() < 1) return;
		AssetManagerComponent assetManager = assetManagerMapper.get(assetManagerEntities.first());

		for(Entity entity : entities) {
			RegisterTextureAssetComponent registerTexture = registerTextureMapper.get(entity);
			RegisterTextureAtlasAssetComponent registerTextureAtlas = registerTextureAtlasMapper.get(entity);
			RegisterSoundAssetComponent registerSound = registerSoundMapper.get(entity);
			RegisterNinePatchAssetComponent registerNinePatch = registerNinePatchMapper.get(entity);
			RegisterBitmapFontAssetComponent registerFont = registerFontMapper.get(entity);

			if(registerTexture != null ) {
				assetManager.load(registerTexture.fileName, Texture.class);
				entity.remove(RegisterTextureAssetComponent.class);
			}
			if(registerTextureAtlas != null ) {
				assetManager.load(registerTextureAtlas.fileName, TextureAtlas.class);
				entity.remove(RegisterTextureAtlasAssetComponent.class);
			}
			if(registerSound != null ) {
				assetManager.load(registerSound.fileName, Sound.class);
				entity.remove(RegisterSoundAssetComponent.class);
			}
			if(registerNinePatch != null ) {
				assetManager.load(registerNinePatch.fileName, NinePatch.class);
				entity.remove(RegisterNinePatchAssetComponent.class);
			}
			if(registerFont != null ) {
				assetManager.load(registerFont.fileName, BitmapFont.class);
				entity.remove(RegisterBitmapFontAssetComponent.class);
			}
		}
	}
}