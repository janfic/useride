package test.java.com.janfic.useride.kernel.systems;

import com.badlogic.ashley.core.*;
import com.janfic.useride.kernel.components.*;
import com.janfic.useride.kernel.systems.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author janfc
 */
public class ProgramEndSystemTest {

    static Engine engine;
    static Entity entity;
    static Entity program;

    @BeforeClass
    public static void setUpClass() {
        engine = new Engine();
        entity = new Entity();
        program = new Entity();

        ProgramEndRequestComponent endRequest = new ProgramEndRequestComponent();
        endRequest.id = 1;
        endRequest.programName = "TestProgram";

        entity.add(endRequest);

        IDComponent id = new IDComponent();
        id.id = 1;

        program.add(id);
        program.add(new EngineComponent());
        program.add(new ClassLoaderComponent());
        program.add(new NameComponent());

        engine.addEntity(program);
        engine.addEntity(entity);
    }

    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of update method, of class ProgramEndSystem.
     */
    @Test
    public void testUpdate() {
        System.out.println("update");
        ProgramEndSystem instance = new ProgramEndSystem();
        engine.addSystem(instance);
        engine.update(0);

        assert (entity.getComponent(ProgramEndRequestComponent.class) == null);
        assert (program.getComponent(IDComponent.class) == null);
        assert (program.getComponent(EngineComponent.class) == null);
        assert (program.getComponent(ClassLoaderComponent.class) == null);
        assert (program.getComponent(NameComponent.class) == null);

    }

}
