package os.systems;

import java.util.Comparator;

import os.components.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.ashley.systems.*;
import com.badlogic.gdx.files.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;

public class ProgramShortcutSystem extends EntitySystem {

    private final ComponentMapper<ProgramStartOnMouseClickComponent> shortcutMapper;
    
    private ImmutableArray<Entity> entities;
    
    private ArrayList<FileHandle> programs;
    
    Properties defaultProperties;
    
    public ProgramShortcutSystem() {
        this.shortcutMapper = ComponentMapper.getFor(ProgramStartOnMouseClickComponent.class);
        this.programs = new ArrayList<FileHandle>();
        this.defaultProperties = new Properties();
        this.defaultProperties.setProperty("width", "500");
        this.defaultProperties.setProperty("height", "400");
        this.defaultProperties.setProperty("fullscreen", "false");
    }
	
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(ProgramStartOnMouseClickComponent.class).get()
        );
        this.programs.clear();
        FileHandle programsDir = Gdx.files.local("home/programs/");
        for(FileHandle file : programsDir.list()) {
            if(file.isDirectory() && file.child("components").exists() && file.child("systems").exists() && file.child("assets/icon.png").exists()) {
                this.programs.add(file);
            }
        }
        
        Entity shortcuts = new Entity();
        shortcuts.add(new SizeComponent(width: 90, height: 100));
        shortcuts.add(new PositionComponent(x: 25, y: 10));
        
        for(int i = 0; i < this.programs.size(); i++) {
            
            FileHandle program = this.programs.get(i);

            String propertiesName = program.name() + ".properties";
            Properties properties = new Properties(defaultProperties);
            properties.setProperty("name", program.name());
            properties.setProperty("displayName", program.name());
            properties.setProperty("shortcutName", program.name());
            if(program.child("assets/" + propertiesName).exists()) {
                try {
                    properties.load(new FileInputStream(program.child("assets/" + propertiesName).file()));
                }
                catch(Exception e) {}
            }
            
           
            Entity shortcut = new Entity();
            shortcut.add(new PositionComponent());
            shortcut.add(new SizeComponent(width: 75, height: 75));
            shortcut.add(new ProgramStartOnMouseClickComponent(name: properties.getProperty("name"), path: program.path()));
            shortcut.add(new ParentComponent(parent: shortcuts));
            shortcut.add(new RelativePositionComponent(x: ((int) (i / 12)) * 100, y: 900 - (i * 100), unit: "%"));
            shortcut.add(new GetNinePatchComponent(name: "shortcut"));
            shortcut.add(new DragableComponent());
            shortcut.add(new ClickableComponent());
            shortcut.add(new HoverableComponent());
            shortcut.add(new HitBoxComponent(rectangle: new Rectangle(0,0,75,75)));
            shortcut.add(new ColorComponent(color: Color.CLEAR));
            shortcut.add(new ChangeColorOnMouseHoverComponent(hoverColor: Color.WHITE.cpy().sub(0,0,0,0.5f), offColor: Color.CLEAR));
            shortcut.add(new PropertiesComponent(properties: properties));

            Entity icon = new Entity();

            icon.add(new PositionComponent(z: 1));
            icon.add(new ParentComponent(parent: shortcut));
            icon.add(new RelativePositionComponent(x: 5, y: 5, unit: "%"));
            icon.add(new RelativeSizeComponent(width: 90, height: 90, unit: "%"));
            icon.add(new RegisterTextureAssetComponent(fileName: program.path() + "/assets/icon.png"));
            icon.add(new GetTextureAssetComponent(fileName: program.path() + "/assets/icon.png"));
            icon.add(new SizeComponent(width: 100, height: 100));
            
            Entity text = new Entity();
            text.add(new PositionComponent());
            text.add(new ParentComponent(parent: shortcut));
            text.add(new RelativePositionComponent(x: 0, y: -20));
            text.add(new SizeComponent(width: 90));
            text.add(new ScaleComponent(scaleX: 0.75f, scaleY: 0.75f));
            text.add(new GetBitmapFontAssetComponent(fileName: "computer/os/assets/userosgui/Lucida Console.fnt"));
            text.add(new TextComponent(text: properties.getProperty("shortcutName")));
            
            engine.addEntity(icon);
            engine.addEntity(text);
            engine.addEntity(shortcut);
        }
    }

    public void update(float delta) {
        
    }
    public void removedFromEngine(Engine engine) {
    }
}