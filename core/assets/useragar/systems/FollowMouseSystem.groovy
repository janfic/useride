package useragar.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.gdx.*;
import useragar.components.*


public class FollowMouseSystem extends EntitySystem {
    
    private final ComponentMapper<VelocityComponent> velocityMapper;
    private final ComponentMapper<PositionComponent> positionMapper;
    private final ComponentMapper<FollowMouseComponent> followMapper;
    
    private ImmutableArray<Entity> entities;
    
    public FollowMouseSystem() {
        this.velocityMapper = ComponentMapper.getFor(VelocityComponent.class);
        this.positionMapper = ComponentMapper.getFor(PositionComponent.class);
        this.followMapper = ComponentMapper.getFor(FollowMouseComponent.class);
    }
    
    @Override
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(
                VelocityComponent.class,
                PositionComponent.class,
                FollowMouseComponent.class
            ).get()
        );
    }
    
    @Override
    public void update(float delta) {
        float mx = Gdx.input.getX();
        float my = Gdx.graphics.getHeight() - Gdx.input.getY();
        
        for( Entity entity : entities ) {
            PositionComponent position = positionMapper.get(entity);
            VelocityComponent velocity = velocityMapper.get(entity);

            int x = position.x;
            int y = position.y;
            
            float dx = mx - x;
            float dy = my - y;
            float hyp = (float)Math.sqrt(dx * dx + dy * dy);
                
            
            if(hyp == 0) hyp = 1;
            
            velocity.dx = dx / hyp;
            velocity.dy = dy / hyp;
        }
    }
    
    @Override
    public void removedFromEngine(Engine engine) {
        
    }
}