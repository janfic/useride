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

public class FileSpawnSystem extends EntitySystem {
        
    
    private final ComponentMapper<TextComponent> textMapper;
    
    private ImmutableArray<Entity> pathEntity, fileEntities;
    
    private Entity grid;
    
    public FileSpawnSystem() {
        this.textMapper = ComponentMapper.getFor(TextComponent.class);
    }
    
    public void addedToEngine(Engine engine) {
        this.grid = new Entity();
        grid.add(new PositionComponent(x: 10, y: 355));
        grid.add(new SizeComponent(width: 75, height: 75));
        
        Entity scrollRoot = new Entity();
        scrollRoot.add(new SizeComponent(height: 355));
        
        Entity scrollBar = new Entity();
        scrollBar.add(new PositionComponent(x: 485, y: 355, z: 4));
        scrollBar.add(new SizeComponent(width: 10, height: 40));
        scrollBar.add(new GetNinePatchComponent(name: "button_up"));
        scrollBar.add(new ClickableComponent());
        scrollBar.add(new HitBoxComponent(rectangle: new Rectangle(0,0,10,40)));
        scrollBar.add(new DragableComponent());
        scrollBar.add(new HitBoxBoundsComponent(minX: 485, maxX: 495, minY: 0, maxY: 355));
        scrollBar.add(new ScrollBarComponent(scrollRoot: scrollRoot, scrollContents: grid));
        
        grid.add(new PositionComponent(x: 10, y: 20));
        grid.add(new SizeComponent(width: 450));
        grid.add(new RelativePositionComponent(x: 0, y: 350, parentMultiplier: -1.0, unit: " p"));
        grid.add(new ParentComponent(parent: scrollBar));
        grid.add(new TextLineRootComponent());
        
        engine.addEntity(scrollRoot);
        engine.addEntity(scrollBar);
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
            parent.add(new PositionComponent(x: 10, y: 375, z: 2))
            parent.add(new SizeComponent(width: 35, height: 20));
            parent.add(new GetNinePatchComponent(name: "button_up"));
            parent.add(new ClickableComponent());
            parent.add(new HitBoxComponent(rectangle: new Rectangle(0,0,40,20)));
            parent.add(new OpenFileOnMouseDoubleClickComponent(path: root.parent().path()));

            parent.add(new FileLoadRequestComponent(fileName: root.parent().path()));
            
            Entity background = new Entity();
            background.add(new PositionComponent(x: 0, y: 0, z: -2))
            background.add(new SizeComponent(width: 500, height: 400));
            background.add(new ClickableComponent());
            background.add(new HitBoxComponent(rectangle: new Rectangle(0,0,500,400)));
            background.add(new OpenFileOnMouseDoubleClickComponent(path: root.parent().path()));
            background.add(new OpenOptionMenuOnRightClickComponent());      
            background.add(new FileLoadRequestComponent(fileName: root.path()));
            
            Entity parentSymbol = new Entity();
            parentSymbol.add(new PositionComponent(z: 3));
            parentSymbol.add(new RelativePositionComponent(x: 10, y: 2));
            parentSymbol.add(new ParentComponent(parent: parent));
            parentSymbol.add(new GetTextureRegionComponent(name: "up_arrow"));
            parentSymbol.add(new ColorComponent(color: Color.BLACK));
            
            Entity ghostChild = new Entity();
            ghostChild.add(new RelativePositionComponent());
            ghostChild.add(new PositionComponent());
            ghostChild.add(new ParentComponent(parent: grid));
            ghostChild.add(new TextLineComponent(lineNumber: 0));
            ghostChild.add(new SizeComponent(width: 0, height: 20));
            ghostChild.add(new FileComponent());
            engine.addEntity(ghostChild);
            
            FileHandle[] children = root.list();
            for(int i = 0; i < root.list().length; i++) {
                FileHandle child = children[i];
                Entity file = new Entity();
                
                int fx = 0 + 100 * (int)(i / 4);
                int fy = 400 - 100 * (i % 4);
                
                file.add(new PositionComponent());
                file.add(new RelativePositionComponent());
                file.add(new ParentComponent(parent: grid));
                file.add(new TextLineComponent(lineNumber: i + 1));
                file.add(new GetTextureAssetComponent(fileName: child.isDirectory() ? "home/programs/files/assets/folder_icon.png" : "home/programs/files/assets/file_icon.png"));
                file.add(new HitBoxComponent(rectangle: new Rectangle(0,0, 20, 20)));
                file.add(new SizeComponent(width: 20, height: 20));
                file.add(new FileLoadRequestComponent(fileName: child.path()));
                Entity fileName = new Entity();
                
                //String name = child.name().length() > 11 ? child.name().substring(0, 5) + ".." + child.name().substring(child.name().length()  - 4) : child.name();
                
                fileName.add(new GetBitmapFontAssetComponent(fileName: "home/programs/os/assets/userosgui/Lucida Console 12px.fnt"));
                fileName.add(new PositionComponent());
                fileName.add(new RelativePositionComponent(x: 30, y: 10));
                fileName.add(new TextComponent(text: child.name()));
                fileName.add(new SizeComponent(width: 450, height: 20));
                fileName.add(new ParentComponent(parent: file));
                fileName.add(new ColorComponent(color: Color.BLACK));
                fileName.add(new FileLoadRequestComponent(fileName: child.path()));
                fileName.add(new FocusableComponent());
                fileName.add(new HitBoxComponent(rectangle: new Rectangle(0,-10, 450, 20)));
                fileName.add(new FileRenameOnKeyEnterComponent());
                fileName.add(new ClickableComponent());
                fileName.add(new KeyInputComponent());
                fileName.add(new ChangeColorOnFocusedComponent(focusedColor: Color.BLUE, unfocusedColor: Color.BLACK));
                fileName.add(new OpenOptionMenuOnRightClickComponent());                
                fileName.add(new OpenFileOnMouseDoubleClickComponent(path: child.path()));
                
                Entity highlight = new Entity();
                highlight.add(new PositionComponent(z: 0));
                highlight.add(new RelativePositionComponent(x: -5));
                highlight.add(new ParentComponent(parent: file));
                highlight.add(new SizeComponent(width: 450, height: 20));
                highlight.add(new GetNinePatchComponent(name: "border"));
                highlight.add(new HoverableComponent());
                highlight.add(new HitBoxComponent(rectangle: new Rectangle(0,0, 450, 20)));
                highlight.add(new ColorComponent(color: Color.CLEAR));
                highlight.add(new ChangeColorOnMouseHoverComponent(hoverColor: Color.WHITE, offColor: Color.CLEAR));
                highlight.add(new FileLoadRequestComponent(fileName: child.path()));
                
                engine.addEntity(file);
                engine.addEntity(fileName);
                engine.addEntity(highlight);
            }
            //pathEntity.first().remove(FileSearchComponent.class);
            engine.addEntity(parent);
            engine.addEntity(parentSymbol);
            engine.addEntity(background);
        }
        pathEntity.first().remove(FileSearchComponent.class);
    }
    
    public void removedFromEngine(Engine engine) {
        
    }
}