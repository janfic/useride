package systems;

import com.badlogic.ashley.core.*;

public class TestSystem extends EntitySystem {

    @Override
    public void addedToEngine(Engine engine) {
        System.out.println("- TEST SYSTEM: Added to Engine");
    }
    
    @Override
    public void update(float deltaTime) {
        System.out.println("from TestSystem::update");
    }

}
