package os.systems;

import os.components.*;
import com.badlogic.gdx.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.viewport.*;
import com.badlogic.gdx.utils.*;

import com.janfic.useride.kernel.components.*;

import groovy.transform.CompileStatic;

@CompileStatic
public class RenderSystem extends SortedIteratingSystem {
	
    private final ComponentMapper<PositionComponent> positionMapper;
    private final ComponentMapper<ScaleComponent> scaleMapper;
    private final ComponentMapper<SizeComponent> sizeMapper;
    private final ComponentMapper<ColorComponent> colorMapper;
    private final ComponentMapper<TextureComponent> textureMapper;
    private final ComponentMapper<TextureRegionComponent> regionMapper;
    private final ComponentMapper<NinePatchComponent> ninePatchMapper;
    private final ComponentMapper<RotationComponent> rotationMapper;
    private final ComponentMapper<BitmapFontComponent> fontMapper;
    private final ComponentMapper<TextComponent> textMapper;
    private final ComponentMapper<SpriteStackComponent> stackMapper;
    private final ComponentMapper<CameraComponent> cameraMapper;
	
    private final ComponentMapper<EngineComponent> engineMapper;
	
    private ImmutableArray<Entity> renderEntities, cameraEntity;
	
    private Batch batch;
    private Viewport viewport;
    private CameraComponent camera;
    
    float ss_zStretch, angle;
	
    public RenderSystem() {
        super(
            Family.all(PositionComponent.class).one(
                TextureComponent.class, 
                NinePatchComponent.class, 
                EngineComponent.class, 
                TextureRegionComponent.class,
                TextComponent.class,
                SpriteStackComponent.class
            ).get(), 
            new ZComparator()
        );
        this.positionMapper = ComponentMapper.getFor(PositionComponent.class);
        this.scaleMapper = ComponentMapper.getFor(ScaleComponent.class);
        this.sizeMapper = ComponentMapper.getFor(SizeComponent.class);
        this.colorMapper = ComponentMapper.getFor(ColorComponent.class);
        this.textureMapper = ComponentMapper.getFor(TextureComponent.class);
        this.ninePatchMapper = ComponentMapper.getFor(NinePatchComponent.class);
        this.rotationMapper = ComponentMapper.getFor(RotationComponent.class);
        this.engineMapper = ComponentMapper.getFor(EngineComponent.class);
        this.regionMapper = ComponentMapper.getFor(TextureRegionComponent.class);
        this.textMapper = ComponentMapper.getFor(TextComponent.class);
        this.fontMapper = ComponentMapper.getFor(BitmapFontComponent.class);
        this.stackMapper = ComponentMapper.getFor(SpriteStackComponent.class);
        this.cameraMapper = ComponentMapper.getFor(CameraComponent.class);
    }
	
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.renderEntities = engine.getEntitiesFor(
            Family.all(
                SpriteBatchComponent.class,
                ViewportComponent.class
            ).get()
        );
        this.cameraEntity = engine.getEntitiesFor(
            Family.all(
                CameraComponent.class
            ).get()
        );
    }
	
    public void update(float delta) {
        
        if(renderEntities.size() == 0) return;
        if(cameraEntity.size() > 0) camera = cameraMapper.get(cameraEntity.first());
        forceSort();
        this.batch = renderEntities.first().getComponent(SpriteBatchComponent.class).batch;
        this.viewport = renderEntities.first().getComponent(ViewportComponent.class).viewport;
        
        //viewport.update(viewport.getScreenWidth(), viewport.getScreenHeight(), true);
        viewport.apply(true);
		
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        super.update(delta);
        
        batch.end();
    }
	
    public void processEntity(Entity entity, float delta) {	
        PositionComponent position = positionMapper.get(entity);
        TextureRegionComponent region = regionMapper.get(entity);
        TextureComponent texture = textureMapper.get(entity);
        NinePatchComponent ninePatch = ninePatchMapper.get(entity);
        EngineComponent engineComponent = engineMapper.get(entity);
        TextComponent textComponent = textMapper.get(entity);
        BitmapFontComponent fontComponent = fontMapper.get(entity);
        SpriteStackComponent stackComponent = stackMapper.get(entity);
		
        SizeComponent size = sizeMapper.get(entity);
        ScaleComponent scale = scaleMapper.get(entity);
        RotationComponent rot = rotationMapper.get(entity);
        ColorComponent color = colorMapper.get(entity);
		                
        float width = 0, height = 0;
        if(texture != null ) { width = texture.texture.getWidth(); height = texture.texture.getHeight(); } 
        if(region != null ) { width = region.textureRegion.getRegionWidth(); height =  region.textureRegion.getRegionHeight(); } 
        if(size != null ) { width = size.width; height = size.height; } 
        float scaleX = scale == null ? 1f : scale.scaleX;
        float scaleY = scale == null ? 1f : scale.scaleY;
        float rotation = rot == null ? 0f : rot.rotation;
        float originX = 0;//position.x + width / 2;
        float originY = 0;//position.y + height / 2;
		
        if(color != null) {
            batch.setColor(color.color);
        }
        if(texture != null) {
            batch.draw(texture.texture, position.x, position.y, originX, originY, width, height, scaleX, scaleY, rotation, 0, 0, (int) texture.texture.getWidth(), (int) texture.texture.getHeight(), false, false);
        }
        if(ninePatch != null) {
            ninePatch.ninePatch.draw(batch, position.x, position.y, originX, originY, width, height, scaleX, scaleY, rotation); 
        }
        if(stackComponent != null && stackComponent.stack != null && camera != null) {
            float zScale = camera.zScale;
            float angle = camera.angle;
            
            float xStep = (float)(zScale * Math.cos(90 + angle));
            float yStep = (float)(zScale * Math.sin(90 + angle));
            
            for(int i = 0; i < stackComponent.stack.size; i++) {
                batch.draw(stackComponent.stack.get(i), position.x - (i * xStep * scaleX), position.y - (i * scaleY * yStep) - position.z, (float)(width / 2),(float)( height / 2), width, height, scaleX, scaleY, rotation);
            }
        }
        if(engineComponent != null) {
            RenderSystem renderSystem = engineComponent.engine.getSystem(RenderSystem.class);      
            if(renderSystem != null) {
                batch.end();
                renderSystem.update(delta); 
                renderSystem.setProcessing(false);
                viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
                viewport.apply();
                batch.setProjectionMatrix(viewport.getCamera().combined);
                batch.begin();
            }
        }
        if(region != null) {
            batch.draw(region.textureRegion, position.x, position.y, originX, originY, width, height, scaleX, scaleY, rotation);
        }
        if(textComponent != null && fontComponent != null) {
            if(color != null) fontComponent.font.setColor(color.color);
            if(scale != null) fontComponent.font.getData().setScale(scale.scaleX,scale.scaleY); else fontComponent.font.getData().setScale(1 , 1);
            if(size != null)
            fontComponent.font.draw( batch, textComponent.text, position.x , (float) (position.y + fontComponent.font.getCapHeight() / 2), width, Align.left, true);
            else
            fontComponent.font.draw( batch, textComponent.text, position.x, (float) (position.y + fontComponent.font.getCapHeight() / 2));
            batch.flush();
            if(color != null) fontComponent.font.setColor(Color.WHITE); 
        }
        
        batch.setColor(Color.WHITE);
    }
	
    private static class ZComparator implements Comparator<Entity> {
        private final ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
		
        @Override
        public int compare(Entity e1, Entity e2) {
            return (int)Math.signum(pm.get(e1).z - pm.get(e2).z);
        }
    }
	
}