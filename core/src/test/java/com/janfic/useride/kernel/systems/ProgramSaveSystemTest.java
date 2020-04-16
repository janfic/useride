package test.java.com.janfic.useride.kernel.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.janfic.useride.kernel.components.EngineComponent;
import com.janfic.useride.kernel.components.FileComponent;
import com.janfic.useride.kernel.components.IDComponent;
import com.janfic.useride.kernel.components.NameComponent;
import com.janfic.useride.kernel.components.ProgramSaveRequestComponent;
import com.janfic.useride.kernel.systems.ProgramSaveSystem;
import java.io.File;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author janfc
 */
public class ProgramSaveSystemTest {

    public static Engine engine;
    public static Entity programEntity, saveEntity;

    @BeforeClass
    public static void setUpClass() {
        engine = new Engine();
        programEntity = new Entity();
        saveEntity = new Entity();

        EngineComponent engineComponent = new EngineComponent();
        engineComponent.engine = new Engine();
        Entity e = new Entity();
        NameComponent name = new NameComponent();
        name.name = "testEntity";
        e.add(name);
        engineComponent.engine.addEntity(e);

        IDComponent idComponent = new IDComponent();
        idComponent.id = 1;

        FileComponent fileComponent = new FileComponent();
        fileComponent.file = new FileHandle(new File("tests/ProgramSaveSystemTest/program"));

        programEntity.add(engineComponent);
        programEntity.add(idComponent);
        programEntity.add(fileComponent);

        ProgramSaveRequestComponent saveRequest = new ProgramSaveRequestComponent();
        saveRequest.id = 1;
        FileComponent saveFile = new FileComponent();
        saveFile.file = new FileHandle(new File("tests/ProgramSaveSystemTest/save"));
        saveFile.file.deleteDirectory();

        saveEntity.add(saveFile);
        saveEntity.add(saveRequest);

        engine.addEntity(programEntity);
        engine.addEntity(saveEntity);
    }

    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of update method, of class ProgramSaveSystem.
     */
    @Test
    public void testUpdate() {
        ProgramSaveSystem instance = new ProgramSaveSystem();
        engine.addSystem(instance);
        engine.update(0);

        assert (saveEntity.getComponent(ProgramSaveRequestComponent.class) == null);
        assert (saveEntity.getComponent(FileComponent.class).file.exists());
    }
}
