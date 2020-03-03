package test.java.com.janfic.useride.kernel.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.files.FileHandle;
import com.janfic.useride.kernel.components.*;
import com.janfic.useride.kernel.systems.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author janfc
 */
public class ProgramLoadSystemTest {

    static Engine engine;
    static Entity programEntity, loadRequestEntity;

    @BeforeClass
    public static void setUpClass() {
        engine = new Engine();

        //Building Program
        programEntity = new Entity();

        FileComponent programLocation = new FileComponent();
        programLocation.file = new FileHandle("tests/ProgramLoadSystemTest/testProgram");

        ProgramStartRequestComponent startRequest = new ProgramStartRequestComponent();
        startRequest.name = "testProgram";

        programEntity.add(programLocation);
        programEntity.add(startRequest);

        //Building Load Request
        loadRequestEntity = new Entity();

        FileComponent saveLocation = new FileComponent();
        saveLocation.file = new FileHandle("tests/ProgramLoadSystemTest/save");

        ProgramLoadRequestComponent loadRequestComponent = new ProgramLoadRequestComponent();
        loadRequestComponent.id = 0;

        loadRequestEntity.add(loadRequestComponent);
        loadRequestEntity.add(saveLocation);

        engine.addEntity(programEntity);
        engine.addEntity(loadRequestEntity);
    }

    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of update method, of class ProgramLoadSystem.
     */
    @Test
    public void testUpdate() {
        engine.addSystem(new ProgramStartSystem());

        engine.update(0);

        engine.addSystem(new ProgramLoadSystem());

        engine.update(0);

        System.out.println(programEntity.getComponent(EngineComponent.class).engine.getEntities());
        for (Entity entity : programEntity.getComponent(EngineComponent.class).engine.getEntities()) {
            System.out.println(entity.getComponents());
        }
    }
}
