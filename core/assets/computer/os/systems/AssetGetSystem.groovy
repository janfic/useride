package os.systems;

import os.components.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;

public class AssetGetSystem extends EntitySystem {
	
    private final ComponentMapper<GetTextureAssetComponent> getTextureMapper;
    private final ComponentMapper<GetTextureAtlasAssetComponent> getAtlasMapper;
    private final ComponentMapper<GetSoundAssetComponent> getSoundMapper;
    private final ComponentMapper<GetBitmapFontAssetComponent> getFontMapper;

    private final ComponentMapper<AssetManagerComponent> assetManagerMapper;

    private ImmutableArray<Entity> entities, assetManagerEntities;

    public AssetGetSystem() {
        this.getTextureMapper = ComponentMapper.getFor(GetTextureAssetComponent.class);
        this.getAtlasMapper = ComponentMapper.getFor(GetTextureAtlasAssetComponent.class);
        this.getSoundMapper = ComponentMapper.getFor(GetSoundAssetComponent.class);
        this.getFontMapper = ComponentMapper.getFor(GetBitmapFontAssetComponent.class);
        this.assetManagerMapper = ComponentMapper.getFor(AssetManagerComponent.class);
    }
	
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.one(
                GetTextureAssetComponent.class,
                GetTextureAtlasAssetComponent.class,
                GetSoundAssetComponent.class,
                GetBitmapFontAssetComponent.class
            ).get()
        );
        this.assetManagerEntities = engine.getEntitiesFor(
            Family.all(AssetManagerComponent.class).get()
        );
    }

    public void update(float delta) {
        if(assetManagerEntities.size() < 1) return;
        AssetManagerComponent assetManager = assetManagerMapper.get(assetManagerEntities.first());
        for(Entity entity : entities) {
            GetTextureAssetComponent getTexture = getTextureMapper.get(entity);
            GetTextureAtlasAssetComponent getAtlas = getAtlasMapper.get(entity);
            GetSoundAssetComponent  getSound = getSoundMapper.get(entity);
            GetBitmapFontAssetComponent getFont = getFontMapper.get(entity);
            if(getTexture != null && assetManager.manager.isLoaded(getTexture.fileName, Texture.class)) {
                TextureComponent textureComponent = new TextureComponent();
                textureComponent.texture = assetManager.manager.get(getTexture.fileName, Texture.class);
                if(textureComponent.texture != null) {
                    entity.add(textureComponent);
                    entity.remove(GetTextureAssetComponent.class);
                }
            }
            if(getAtlas != null && assetManager.manager.isLoaded(getAtlas.fileName, TextureAtlas.class)) {
                TextureAtlasComponent textureAtlasComponent = new TextureAtlasComponent();
                textureAtlasComponent.atlas = assetManager.manager.get(getAtlas.fileName, TextureAtlas.class);
                if(textureAtlasComponent.atlas != null) {
                    entity.add(textureAtlasComponent);
                    entity.remove(GetTextureAtlasAssetComponent.class);
                }
            }
            if(getSound != null && assetManager.manager.isLoaded(getSound.fileName, Sound.class)) {
                SoundComponent soundComponent = new SoundComponent();
                soundComponent.sound = assetManager.manager.get(getSound.fileName, Sound.class);
                if(soundComponent.sound != null) {
                    entity.add(soundComponent);
                    entity.remove(GetSoundAssetComponent.class);
                }
            }
            if(getFont != null && assetManager.manager.isLoaded(getFont.fileName, BitmapFont.class)) {
                BitmapFontComponent fontComponent = new BitmapFontComponent();
                fontComponent.font = assetManager.manager.get(getFont.fileName, BitmapFont.class);
                fontComponent.font.setUseIntegerPositions(true);
                if(fontComponent.font != null) {
                    entity.add(fontComponent);
                    entity.remove(GetBitmapFontAssetComponent.class);
                }
            }
        }
    }

    public void removedFromEngine(Engine engine) {
	
    }

}