package os.systems;

import os.components.*;
import com.badlogic.gdx.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.*;
import com.badlogic.gdx.utils.*;

import ui.components.*;

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
    private final ComponentMapper<FrameBufferComponent> frameBufferMapper;
    private final ComponentMapper<BackgroundColorComponent> bgColorMapper;
    private final ComponentMapper<HitBoxComponent> hitBoxMapper;
		
    private ImmutableArray<Entity> renderEntities, cameraEntity;
	
    private Batch batch;
    private Viewport viewport;
    private FrameBuffer frameBuffer;
    private CameraComponent camera;
    private ShapeRenderer shapeRenderer;
    
    float ss_zStretch, angle;
	
    public RenderSystem() {
        super(
            Family.all(PositionComponent.class).one(
                TextureComponent.class, 
                NinePatchComponent.class, 
                TextureRegionComponent.class,
                TextComponent.class,
                SpriteStackComponent.class,
                FrameBufferComponent.class,
                BackgroundColorComponent.class
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
        this.regionMapper = ComponentMapper.getFor(TextureRegionComponent.class);
        this.textMapper = ComponentMapper.getFor(TextComponent.class);
        this.fontMapper = ComponentMapper.getFor(BitmapFontComponent.class);
        this.stackMapper = ComponentMapper.getFor(SpriteStackComponent.class);
        this.cameraMapper = ComponentMapper.getFor(CameraComponent.class);
        this.frameBufferMapper = ComponentMapper.getFor(FrameBufferComponent.class);
        //css stuff
        this.bgColorMapper = ComponentMapper.getFor(BackgroundColorComponent.class);
        this.hitBoxMapper = ComponentMapper.getFor(HitBoxComponent.class);
        this.shapeRenderer = new ShapeRenderer();
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
        this.frameBuffer = renderEntities.first().getComponent(FrameBufferComponent.class) == null ? null : renderEntities.first().getComponent(FrameBufferComponent.class).frameBuffer;        
        viewport.apply(true);
		
        batch.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        if(frameBuffer != null) frameBuffer.begin();
        batch.begin();
        super.update(delta);
        
        batch.end();
        if(frameBuffer != null) frameBuffer.end();
    }
	
    public void processEntity(Entity entity, float delta) {	
        PositionComponent position = positionMapper.get(entity);
        TextureRegionComponent region = regionMapper.get(entity);
        TextureComponent texture = textureMapper.get(entity);
        NinePatchComponent ninePatch = ninePatchMapper.get(entity);
        TextComponent textComponent = textMapper.get(entity);
        BitmapFontComponent fontComponent = fontMapper.get(entity);
        SpriteStackComponent stackComponent = stackMapper.get(entity);
        FrameBufferComponent frameBufferComponent = frameBufferMapper.get(entity);
        
        BackgroundColorComponent bgColorComponent = bgColorMapper.get(entity);
        HitBoxComponent hitBoxComponent = hitBoxMapper.get(entity);
        
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
	
        if(bgColorComponent != null && hitBoxComponent != null) {
            batch.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(bgColorComponent.color);
            shapeRenderer.rect(position.x + hitBoxComponent.rectangle.getX(), position.y + hitBoxComponent.rectangle.getY(), hitBoxComponent.rectangle.getWidth(), hitBoxComponent.rectangle.getHeight());
            shapeRenderer.end();
            batch.begin();
        }
        if(frameBufferComponent != null) {
            if(size == null) {width = frameBufferComponent.frameBuffer.getWidth(); height = frameBufferComponent.frameBuffer.getHeight();}
            batch.draw(frameBufferComponent.frameBuffer.getColorBufferTexture(), position.x, position.y, originX, originY, width, height, scaleX, scaleY, rotation, 0, 0, (int) width, (int)height, false, true);
            frameBufferComponent.frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, (int)width, (int)height, true);
        }
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
        
        if(region != null) {
            batch.draw(region.textureRegion, position.x, position.y, originX, originY, width, height, scaleX, scaleY, rotation);
        }
        if(textComponent != null && fontComponent != null) {
            if(color != null) fontComponent.font.setColor(color.color);
            if(scale != null) fontComponent.font.getData().setScale(scale.scaleX,scale.scaleY); else fontComponent.font.getData().setScale(1 , 1);
            if(size != null)
            fontComponent.font.draw( batch, textComponent.text, position.x , (float) (position.y + fontComponent.font.getCapHeight()), width, Align.left, true);
            else
            fontComponent.font.draw( batch, textComponent.text, position.x, (float) (position.y + fontComponent.font.getCapHeight()));
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