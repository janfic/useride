package useragar.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.gdx.graphics.glutils.*;
import useragar.components.*;

public class RenderSystem extends EntitySystem {
	
    private final ComponentMapper<CircleComponent> circleMapper;
    private final ComponentMapper<PositionComponent> positionMapper;
    private final ComponentMapper<ColorComponent> colorMapper;
    
    private ShapeRenderer shapeRenderer;
    
    private ImmutableArray<Entity> entities;
    
    public RenderSystem() {
        this.circleMapper = ComponentMapper.getFor(CircleComponent.class);
        this.positionMapper = ComponentMapper.getFor(PositionComponent.class);
        this.colorMapper = ComponentMapper.getFor(ColorComponent.class);
        this.shapeRenderer = new ShapeRenderer();
    }
    
    @Override
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(
                ColorComponent.class,
                PositionComponent.class,
                CircleComponent.class
            ).get()        
        );
    }
    
    @Override
    public void update(float delta) {
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        for(Entity entity : entities) {
            CircleComponent circle = circleMapper.get(entity);
            PositionComponent position = positionMapper.get(entity);
            ColorComponent color = colorMapper.get(entity);
            
            shapeRenderer.setColor((float)(color.r / 256f),(float) (color.g / 256f), (float)(color.b / 256f), 1.0f);
            shapeRenderer.circle(position.x, position.y, circle.radius);
        }
        shapeRenderer.end();
    }
    
    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);
    }
}