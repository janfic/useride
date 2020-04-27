package useragar.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import useragar.components.*;

public class BootSystem extends EntitySystem {

    @Override
    public void addedToEngine(Engine engine) {
        engine.addSystem(new MovementSystem());
        engine.addSystem(new RenderSystem());
        engine.addSystem(new FollowMouseSystem());
        engine.addSystem(new FoodSystem());
        engine.addSystem(new CollisionSystem());
        engine.addSystem(new HungerSystem());
        engine.addSystem(new AgarSystem());
                
        Entity playerAgar = new Entity();
        
        PositionComponent position = new PositionComponent();
        position.x = 300;
        position.y = 300;
        
        VelocityComponent velocity = new VelocityComponent();
        velocity.dx = 0;
        velocity.dy = 0;
        
        FollowMouseComponent follow = new FollowMouseComponent();
        
        CircleComponent circle = new CircleComponent();
        circle.radius = 20
        
        MassComponent mass = new MassComponent();
        mass.mass = 300;
        
        ColorComponent color = new ColorComponent();
        color.r = 255;
        color.g = 255;
        color.b = 255;
        
        playerAgar.add(position);
        playerAgar.add(velocity);
        playerAgar.add(follow);
        playerAgar.add(circle);
        playerAgar.add(color);
        playerAgar.add(mass);
        
        engine.addEntity(playerAgar);
        
        engine.removeSystem(this);
    }

}