package test.java.com.janfic.useride.kernel.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.files.FileHandle;
import com.janfic.useride.kernel.components.*;
import com.janfic.useride.kernel.systems.ProgramStartSystem;
import java.io.File;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author janfic
 */
public class ProgramStartSystemTest {

    static Engine engine;
    static Entity entity;

    @BeforeClass
    public static void setUpClass() {
        engine = new Engine();
        entity = new Entity();
        FileComponent fileComponent = new FileComponent();
        fileComponent.file = new FileHandle(new File("tests/ProgramStartSystemTest/testProgram"));
        ProgramStartRequestComponent startRequest = new ProgramStartRequestComponent();
        startRequest.name = "testProgram";
        entity.add(fileComponent);
        entity.add(startRequest);

        engine.addEntity(entity);
    }

    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of update method, of class ProgramStartSystem.
     */
    @Test
    public void testUpdate() {
        ProgramStartSystem system = new ProgramStartSystem();
        engine.addSystem(system);
        engine.update(0);
        assert (entity.getComponent(ProgramStartRequestComponent.class) == null);
        assert (entity.getComponent(FileComponent.class) != null);
        assert (entity.getComponent(EngineComponent.class) != null);
        assert (entity.getComponent(ClassLoaderComponent.class) != null);
        assert (entity.getComponent(IDComponent.class) != null);
        assert (entity.getComponent(NameComponent.class) != null);
    }
}
