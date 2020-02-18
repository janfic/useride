package test.java.com.janfic.useride.kernel.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.files.FileHandle;
import com.janfic.useride.kernel.components.FileComponent;
import com.janfic.useride.kernel.components.FileSaveRequestComponent;
import com.janfic.useride.kernel.systems.FileSaveSystem;
import java.io.File;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for the FileSaveSystem
 *
 * @author janfic
 */
public class FileSaveSystemTest {

    static Engine engine;
    static Entity entity;
    static FileComponent file;
    
    @BeforeClass
    public static void setUpClass() {
        engine = new Engine();

        entity = new Entity();

        file = new FileComponent();
        file.file = new FileHandle(new File("tests/FileSaveSystemTest.txt"));
        file.file.delete();

        FileSaveRequestComponent saveRequest = new FileSaveRequestComponent();
        saveRequest.data = "hello".getBytes();

        entity.add(saveRequest);
        entity.add(file);

        engine.addEntity(entity);
    }

    @AfterClass
    public static void tearDownClass() {
        engine = null;
    }

    /**
     * Test of addedToEngine method, of class FileSaveSystem.
     */
    @Test
    public void testAddedToEngine() {
        System.out.println("addedToEngine");
        FileSaveSystem instance = new FileSaveSystem();
        engine.addSystem(instance);
        engine.update(0);
        
        //Assert file has been saved to
        assert(file.file.readString().equals("hello"));
        
        //Assert that entity does not have request component anymore
        assert(entity.getComponent(FileSaveRequestComponent.class) == null);
    }
}
