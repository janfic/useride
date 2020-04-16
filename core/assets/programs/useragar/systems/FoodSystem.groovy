package useragar.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.*;
import useragar.components.*
import com.badlogic.ashley.utils.*;

public class FoodSystem extends EntitySystem {

    private float timer;
    
    private Engine engine;
    
    @Override
    public void addedToEngine(Engine engine) {
        timer = 3;
        this.engine = engine;
    }
    
    @Override
    public void update(float delta) {
        timer -= delta;
        if(timer <= 0) {
            Entity food = new Entity();
            
            PositionComponent position = new PositionComponent();
            position.x = (float) (Math.random() * Gdx.graphics.getWidth());
            position.y = (float) (Math.random() * Gdx.graphics.getHeight());
       
            CircleComponent circle = new CircleComponent();
            circle.radius = 10;
        
            MassComponent mass = new MassComponent();
            mass.mass = 200;
        
            ColorComponent color = new ColorComponent();
            color.r = 128;
            color.g = 0;
            color.b = 255;
            
            food.add(color);
            food.add(mass);
            food.add(circle);
            food.add(position);

            this.engine.addEntity(food);
            
            timer = 5;
        }
    }
}