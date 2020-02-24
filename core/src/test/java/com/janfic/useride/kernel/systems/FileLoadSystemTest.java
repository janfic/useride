package test.java.com.janfic.useride.kernel.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.files.FileHandle;
import com.janfic.useride.kernel.components.FileComponent;
import com.janfic.useride.kernel.components.FileLoadRequestComponent;
import com.janfic.useride.kernel.systems.FileLoadSystem;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author janfc
 */
public class FileLoadSystemTest {

    static LwjglApplication app;
    static Engine engine;
    static Entity entity;

    @BeforeClass
    public static void setUpClass() {

        app = new LwjglApplication(new Game() {
            @Override
            public void create() {

            }
        });

        
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
        FileLoadSystem instance = new FileLoadSystem();
        engine.addSystem(instance);
        engine.update(0);
        
        assert(entity.getComponent(FileComponent.class) != null);
        assert(entity.getComponent(FileLoadRequestComponent.class) == null);
        
        FileHandle file = entity.getComponent(FileComponent.class).file;
        
        assert(file.name().equals("FileLoadSystemTest.txt"));
    }

}
