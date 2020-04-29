package os.systems;

import os.components.*;
import com.badlogic.gdx.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.viewport.*;

import com.janfic.useride.kernel.components.*;

public class RenderSystem extends SortedIteratingSystem {
	
	private final ComponentMapper<PositionComponent> positionMapper;
	private final ComponentMapper<ScaleComponent> scaleMapper;
	private final ComponentMapper<SizeComponent> sizeMapper;
	private final ComponentMapper<ColorComponent> colorMapper;
	private final ComponentMapper<TextureComponent> textureMapper;
	private final ComponentMapper<TextureRegionComponent> regionMapper;
	private final ComponentMapper<NinePatchComponent> ninePatchMapper;
	private final ComponentMapper<RotationComponent> rotationMapper;
	
	private final ComponentMapper<EngineComponent> engineMapper;
	
	private ImmutableArray<Entity> renderEntities;
	
	private Batch batch;
	private Viewport viewport;
	
	public RenderSystem() {
		super(
			Family.all(PositionComponent.class).one(
				TextureComponent.class, 
				NinePatchComponent.class, 
				EngineComponent.class, 
				TextureRegionComponent.class
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
	}
	
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		this.renderEntities = engine.getEntitiesFor(
			Family.all(
				SpriteBatchComponent.class,
				ViewportComponent.class
			).get()
		);
	}
	
	public void update(float delta) {
		if(renderEntities.size() == 0) return;
		this.batch = renderEntities.first().getComponent(SpriteBatchComponent.class).batch;
		this.viewport = renderEntities.first().getComponent(ViewportComponent.class).viewport;
		
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		viewport.apply();
		
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
		
		SizeComponent size = sizeMapper.get(entity);
		ScaleComponent scale = scaleMapper.get(entity);
		RotationComponent rot = rotationMapper.get(entity);
		ColorComponent color = colorMapper.get(entity);
		
		
		float width = size == null ? texture.texture.getWidth() : size.width;
		float height = size == null ? texture.texture.getHeight() : size.height;
		float scaleX = scale == null ? 1f : scale.scaleX;
		float scaleY = scale == null ? 1f : scale.scaleY;
		float rotation = rot == null ? 0f : rot.rotation;
		float originX = position.x + width / 2;
		float originY = position.y + height / 2;
		
		if(color != null) {
			batch.setColor(color.color);
		}
		if(texture != null) {
			batch.draw(texture.texture, position.x, position.y, originX, originY, width, height, scaleX, scaleY, rotation, 0, 0, (int) texture.texture.getWidth(), (int) texture.texture.getHeight(), false, false);
		}
		if(ninePatch != null) {
			ninePatch.ninePatch.draw(batch, position.x, position.y, originX, originY, width, height, scaleX, scaleY, rotation); 
		}
		if(engineComponent != null) {
			RenderSystem renderSystem = engineComponent.engine.getSystem(RenderSystem.class);
			if(renderSystem != null) {
				renderSystem.update(delta); 
				renderSystem.setProcessing(false);
			}
		}
		if(region != null) {
			batch.draw(region.textureRegion, position.x, position.y, originX, originY, width, height, scaleX, scaleY, rotation);
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