package test.java.com.janfic.useride.kernel.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.janfic.useride.kernel.components.FileLoadRequestComponent;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author janfc
 */
public class FileLoadSystemTest {
    
    static Engine engine;
    static Entity entity;

    @BeforeClass
    public static void setUpClass() {

        engine = new Engine();
        entity = new Entity();

        FileLoadRequestComponent loadRequest = new FileLoadRequestComponent();
        loadRequest.fileName = "tests/FileLoadSystemTest.txt";
        
        entity.add(loadRequest);
        engine.addEntity(entity);
    }

    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of addedToEngine method, of class FileLoadSystem.
     */
    @Test
    public void test() {
        //FileLoadSystem instance = new FileLoadSystem();
        //engine.addSystem(instance);
        //engine.update(0);
    }

}
