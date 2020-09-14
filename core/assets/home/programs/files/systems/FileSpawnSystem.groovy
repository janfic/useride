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
        grid.add(new SizeComponent(width: 75, height: 75));
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
        
        FileHandle root = Gdx.files.local(path.text.trim());
        
        if(root.exists() && root.isDirectory()) {
            
            for(Entity entity : fileEntities) {
                this.getEngine().removeEntity(entity);
            }
            
            Entity parent = new Entity();
            parent.add(new PositionComponent())
            parent.add(new RelativePositionComponent(x: 0, y: 512, unit: "%"));
            parent.add(new ParentComponent(parent: grid));
            parent.add(new SizeComponent(width: 35, height: 20));
            parent.add(new GetNinePatchComponent(name: "button_up"));
            parent.add(new ClickableComponent());
            parent.add(new HitBoxComponent(rectangle: new Rectangle(0,0,80,20)));
            parent.add(new FileSearchOnMouseDoubleClickComponent());
            parent.add(new FileLoadRequestComponent(fileName: root.parent().path()));
            
            Entity parentSymbol = new Entity();
            parentSymbol.add(new PositionComponent(z: 1));
            parentSymbol.add(new RelativePositionComponent(x: 10, y: 2));
            parentSymbol.add(new ParentComponent(parent: parent));
            parentSymbol.add(new GetTextureRegionComponent(name: "up_arrow"));
            parentSymbol.add(new ColorComponent(color: Color.BLACK));
            
            
            FileHandle[] children = root.list();
            for(int i = 0; i < root.list().length; i++) {
                FileHandle child = children[i];
                Entity file = new Entity();
                
                int fx = 0 + 100 * (int)(i / 4);
                int fy = 400 - 100 * (i % 4);
                
                file.add(new PositionComponent());
                file.add(new RelativePositionComponent(x: fx, y: fy, unit: "%"));
                file.add(new ParentComponent(parent: grid));
                file.add(new GetTextureAssetComponent(fileName: child.isDirectory() ? "home/programs/files/assets/folder.png" : "home/programs/files/assets/file.png"));
                file.add(new FileLoadRequestComponent(fileName: child.path()));
                file.add(new HitBoxComponent(rectangle: new Rectangle(0,0, 64, 64)));
                file.add(new ClickableComponent());
                file.add(new FileLoadRequestComponent(fileName: child.path()));
                file.add(new FileSearchOnMouseDoubleClickComponent());
                
                Entity fileName = new Entity();
                
                String name = child.name().length() > 11 ? child.name().substring(0, 5) + ".." + child.name().substring(child.name().length()  - 4) : child.name();
                
                fileName.add(new GetBitmapFontAssetComponent(fileName: "home/programs/os/assets/userosgui/Lucida Console 12px.fnt"));
                fileName.add(new PositionComponent());
                fileName.add(new RelativePositionComponent(x: 0, y: -10));
                fileName.add(new TextComponent(text: name));
                fileName.add(new SizeComponent(width: 90, height: 20));
                fileName.add(new ParentComponent(parent: file));
                fileName.add(new ColorComponent(color: Color.BLACK));
                fileName.add(new FileLoadRequestComponent(fileName: child.path()));
                
                engine.addEntity(file);
                engine.addEntity(fileName);
            }
            //pathEntity.first().remove(FileSearchComponent.class);
            engine.addEntity(parent);
            engine.addEntity(parentSymbol);
        }
        pathEntity.first().remove(FileSearchComponent.class);
    }
    
    public void removedFromEngine(Engine engine) {
        
    }
}