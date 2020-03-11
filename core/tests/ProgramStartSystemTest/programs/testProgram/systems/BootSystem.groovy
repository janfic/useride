package systems;

import com.badlogic.ashley.core.*;

public class BootSystem extends EntitySystem {

    @Override
    public void addedToEngine(Engine engine) {
        System.out.println("- BOOT SYSTEM: Added to Engine");
        System.out.println("- Adding other systems: TestSystem");
        System.out.println("- New Print Line");
        engine.addSystem(new TestSystem());
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
