package systems;

import com.badlogic.ashley.core.*;
import components.*;

public class SavedSystem extends EntitySystem {

    @Override
    public void addedToEngine(Engine engine) {
        System.out.println("- Saved System: Added to Engine");
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine); //To change body of generated methods, choose Tools | Templates.
    }
}