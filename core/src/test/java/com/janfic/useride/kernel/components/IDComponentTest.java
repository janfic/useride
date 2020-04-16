package test.java.com.janfic.useride.kernel.components;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.janfic.useride.kernel.components.FileComponent;
import java.io.File;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author janfc
 */
public class IDComponentTest {
    
    public IDComponentTest() {
        System.out.println("");
        
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
        
    }

//    @Test
//    public void testSomeMethod() {
//        Json json = new Json();
//        FileComponent ob = new FileComponent();
//        ob.file = new FileHandle(new File("hello.txt"));
//        System.out.print(json.toJson(ob));
//        FileComponent co = json.fromJson(FileComponent.class, json.toJson(ob));
//        System.out.println(co);
//    }
    
}
