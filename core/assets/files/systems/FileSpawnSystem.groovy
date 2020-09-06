package files.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.assets.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.files.*;

import com.janfic.useride.kernel.components.*;
import com.janfic.useride.kernel.systems.*;
import os.components.*;
import files.components.*;

import groovy.transform.CompileStatic;


@CompileStatic
public class FileSpawnSystem extends EntitySystem {
        
    
    private final ComponentMapper<TextComponent> textMapper;
    
    private ImmutableArray<Entity> pathEntity, fileEntities;
    
    private Entity grid;
    
    public FileSpawnSystem() {
        this.textMapper = ComponentMapper.getFor(TextComponent.class);
    }
    
    public void addedToEngine(Engine engine) {
        this.grid = new Entity();
        grid.add(new PositionComponent(x: 10, y: -10));
        grid.add(new SizeComponent(width: 90, height: 80));
        engine.addEntity(grid);
        this.pathEntity = engine.getEntitiesFor(
            Family.all(FileSearchComponent.class, TextComponent.class).get()
        );
        this.fileEntities = engine.getEntitiesFor(
            Family.all(FileComponent.class).get()
        );
    }
    
    public void update(float delta) {
        if(pathEntity.size() < 1) return;
        TextComponent path = textMapper.get(pathEntity.first());
        
        FileHandle root = Gdx.files.local(path.text);
        
        if(root.exists() && root.isDirectory()) {
            
            for(Entity entity : fileEntities) {
                this.getEngine().removeEntity(entity);
            }
            
            FileHandle[] children = root.list();
            for(int i = 0; i < root.list().length; i++) {
                FileHandle child = children[i];
                Entity file = new Entity();
                
                int fx = 0 + 100 * (int)(i / 4);
                int fy = 400 - 100 * (i % 4);
                
                file.add(new PositionComponent());
                file.add(new RelativePositionComponent(x: fx, y: fy, unit: "%"));
                file.add(new ParentComponent(parent: grid));
                file.add(new GetTextureAssetComponent(fileName: child.isDirectory() ? "files/assets/folder.png" : "files/assets/file.png"));
                file.add(new FileLoadRequestComponent(fileName: child.path()));
                
                Entity fileName = new Entity();
                
                String name = child.name().length() > 12 ? child.name().substring(0, 5) + ".." + child.name().substring(child.name().length()  - 5) : child.name();
                
                fileName.add(new GetBitmapFontAssetComponent(fileName: "os/assets/userosgui/Lucida Console.fnt"));
                fileName.add(new PositionComponent());
                fileName.add(new RelativePositionComponent(x: 0, y: -10));
                fileName.add(new TextComponent(text: name));
                fileName.add(new SizeComponent(width: 90, height: 20));
                fileName.add(new ScaleComponent(scaleX: 0.5f, scaleY: 0.5f));
                fileName.add(new ParentComponent(parent: file));
                fileName.add(new ColorComponent(color: Color.BLACK));
                fileName.add(new FileLoadRequestComponent(fileName: child.path()));
                
                engine.addEntity(file);
                engine.addEntity(fileName);
            }
            pathEntity.first().remove(FileSearchComponent.class);
        }
        
    }
    
    public void removedFromEngine(Engine engine) {
        
    }
}