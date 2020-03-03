package systems;

import com.badlogic.ashley.core.*;
import components.*;

public class BootSystem extends EntitySystem {

    @Override
    public void addedToEngine(Engine engine) {
        System.out.println("- BOOT SYSTEM: Added to Engine");
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