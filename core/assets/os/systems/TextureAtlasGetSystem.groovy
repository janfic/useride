package os.systems;

import os.components.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.*;

public class TextureAtlasGetSystem extends EntitySystem {
	
    private final ComponentMapper<GetTextureRegionComponent> getRegionMapper;
    private final ComponentMapper<GetAnimationComponent> getAnimationMapper;
    private final ComponentMapper<GetNinePatchComponent> getNinePatchMapper;
	
    private final ComponentMapper<TextureAtlasComponent> textureAtlasMapper;

    private ImmutableArray<Entity> textureAtlasEntities, entities;

    public TextureAtlasGetSystem() {
        this.getRegionMapper = ComponentMapper.getFor(GetTextureRegionComponent.class);
        this.getAnimationMapper = ComponentMapper.getFor(GetAnimationComponent.class);
        this.getNinePatchMapper = ComponentMapper.getFor(GetNinePatchComponent.class);
        this.textureAtlasMapper = ComponentMapper.getFor(TextureAtlasComponent.class);
    }
	
    public void addedToEngine(Engine engine) {
        this.textureAtlasEntities = engine.getEntitiesFor(
            Family.all(TextureAtlasComponent.class).get()
        );
        this.entities = engine.getEntitiesFor(
            Family.one(GetTextureRegionComponent.class, GetAnimationComponent.class, GetNinePatchComponent.class).get()
        );
    }

    public void update(float delta) {
        for(Entity entity : entities) {
            GetAnimationComponent getAnimation = getAnimationMapper.get(entity);
            GetTextureRegionComponent getRegion = getRegionMapper.get(entity);
            GetNinePatchComponent getPatch = getNinePatchMapper.get(entity);

            if(getAnimation != null) {
                for(Entity textureAtlasEntity : textureAtlasEntities) {
                    TextureAtlasComponent textureAtlas = textureAtlasMapper.get(textureAtlasEntity);
                    Array<TextureRegion> frames =  textureAtlas.atlas.findRegions(getAnimation.name);
                    if(frames != null) {
                        AnimationComponent animationComponent = new AnimationComponent();
                        animationComponent.animation = new Animation(getAnimation.frameRate, frames);

                        entity.add(animationComponent);
                        entity.remove(GetAnimationComponent.class);
                        break;
                    }
                }
            }
            if(getRegion != null) {
                for(Entity textureAtlasEntity : textureAtlasEntities) {
                    TextureAtlasComponent textureAtlas = textureAtlasMapper.get(textureAtlasEntity);
                    TextureRegion region =  textureAtlas.atlas.findRegion(getRegion.name);
                    if(region != null) {
                        TextureRegionComponent textureRegionComponent = new TextureRegionComponent();
                        textureRegionComponent.textureRegion = region;
                        System.out.println("REGION");
                        entity.add(textureRegionComponent);
                        entity.remove(GetTextureRegionComponent.class);
                        break;
                    }
                }
            }
            if(getPatch != null) {
                for(Entity textureAtlasEntity : textureAtlasEntities) {
                    TextureAtlasComponent textureAtlas = textureAtlasMapper.get(textureAtlasEntity);
                    NinePatch patch = textureAtlas.atlas.createPatch(getPatch.name);
                    if(patch != null) {
                        NinePatchComponent ninePatchComponent = new NinePatchComponent();
                        ninePatchComponent.ninePatch = patch;
                        entity.add(ninePatchComponent);
                        entity.remove(GetNinePatchComponent.class);
                        break;
                    }
                }
            }
        }
    }
}