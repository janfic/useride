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

public class OptionMenuSystem extends EntitySystem {
        
    private final ComponentMapper<MouseClickEventComponent> clickMapper;
    private final ComponentMapper<FileComponent> fileMapper;
    
    private ImmutableArray<Entity> entities, optionMenu, clickedEntities, pathEntity;
   
    public OptionMenuSystem() {
        this.clickMapper = ComponentMapper.getFor(MouseClickEventComponent.class);
        this.fileMapper = ComponentMapper.getFor(FileComponent.class);
    }
    
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(OpenOptionMenuOnRightClickComponent.class, MouseClickEventComponent.class, FileComponent.class).get()
        );
        this.optionMenu = engine.getEntitiesFor(
            Family.all(OptionMenuComponent.class).get()
        );
        this.clickedEntities = engine.getEntitiesFor(
            Family.one(MouseClickEventComponent.class, MousePressEventComponent.class, MouseReleaseEventComponent.class).exclude(OptionMenuComponent.class).get()
        );
        this.pathEntity = engine.getEntitiesFor(
            Family.all(TextComponent.class, KeyInputComponent.class).get()
        );
    }
    
    public void update(float delta) {
        
        if(Gdx.input.isTouched() && optionMenu.size() > 0 && clickedEntities.size() < 1) {
            for(Entity entity : optionMenu) {
                this.engine.removeEntity(entity);
            }
        }
        
        if(entities.size() < 1) return;
        
        for(Entity entity : optionMenu) {
            this.engine.removeEntity(entity);
        }
                
        for(Entity entity : entities) {
            MouseClickEventComponent click = clickMapper.get(entity);
            FileComponent fileComponent = fileMapper.get(entity);
            entity.remove(MouseClickEventComponent.class);
            if(click.button != Input.Buttons.RIGHT) continue;
            
            Entity optionMenu = new Entity();
            optionMenu.add(new PositionComponent(x: 100, y: 100, z: 2));
            optionMenu.add(new RelativePositionComponent(x: 64, y: -100));
            optionMenu.add(new ParentComponent(parent: entity));
            optionMenu.add(new GetNinePatchComponent(name: "container"));
            optionMenu.add(new SizeComponent(width: 100, height: 150));
            optionMenu.add(new OptionMenuComponent());
            optionMenu.add(new ClickableComponent());
            optionMenu.add(new HitBoxComponent(rectangle: new Rectangle(0,0,100,150)));
            
            Entity open = new Entity();
            open.add(new PositionComponent(z: 3));
            open.add(new RelativePositionComponent(x: 10, y: 130));
            open.add(new ParentComponent(parent: optionMenu));
            open.add(new TextComponent(text: "Open"));
            open.add(new GetBitmapFontAssetComponent(fileName: "home/programs/os/assets/userosgui/Lucida Console 12px.fnt"));
            open.add(new OptionMenuComponent());
            open.add(new ColorComponent(color: Color.BLACK));
            open.add(new HitBoxComponent(rectangle: new Rectangle(0,-10, 80, 20)));
            open.add(new ClickableComponent());
            open.add(new CloseOptionMenuOnMouseClickComponent());
            if(pathEntity.size() > 0) open.add(new OpenFileOnMouseClickComponent(path: fileComponent.file.path()));
            
            Entity rename = new Entity();
            rename.add(new PositionComponent(z: 3));
            rename.add(new RelativePositionComponent(x: 10, y: 110));
            rename.add(new ParentComponent(parent: optionMenu));
            rename.add(new TextComponent(text: "Rename"));
            rename.add(new GetBitmapFontAssetComponent(fileName: "home/programs/os/assets/userosgui/Lucida Console 12px.fnt"));
            rename.add(new OptionMenuComponent());
            rename.add(new ColorComponent(color: Color.BLACK));
            
            Entity cut = new Entity();
            cut.add(new PositionComponent(z: 3));
            cut.add(new RelativePositionComponent(x: 10, y: 90));
            cut.add(new ParentComponent(parent: optionMenu));
            cut.add(new TextComponent(text: "Cut"));
            cut.add(new GetBitmapFontAssetComponent(fileName: "home/programs/os/assets/userosgui/Lucida Console 12px.fnt"));
            cut.add(new OptionMenuComponent());
            cut.add(new ColorComponent(color: Color.BLACK));
            
            Entity copy = new Entity();
            copy.add(new PositionComponent(z: 3));
            copy.add(new RelativePositionComponent(x: 10, y: 70));
            copy.add(new ParentComponent(parent: optionMenu));
            copy.add(new TextComponent(text: "Copy"));
            copy.add(new GetBitmapFontAssetComponent(fileName: "home/programs/os/assets/userosgui/Lucida Console 12px.fnt"));
            copy.add(new OptionMenuComponent());
            copy.add(new ColorComponent(color: Color.BLACK));
            copy.add(new HitBoxComponent(rectangle: new Rectangle(0,-10, 80, 20)));
            copy.add(new ClickableComponent());
            copy.add(new CloseOptionMenuOnMouseClickComponent());
            copy.add(new FileCopyOnMouseClickComponent(entity: entity));
            
            Entity paste = new Entity();
            paste.add(new PositionComponent(z: 3));
            paste.add(new RelativePositionComponent(x: 10, y: 50));
            paste.add(new ParentComponent(parent: optionMenu));
            paste.add(new TextComponent(text: "Paste"));
            paste.add(new GetBitmapFontAssetComponent(fileName: "home/programs/os/assets/userosgui/Lucida Console 12px.fnt"));
            paste.add(new OptionMenuComponent());
            paste.add(new ColorComponent(color: Color.BLACK));
            paste.add(new HitBoxComponent(rectangle: new Rectangle(0,-10, 80, 20)));
            paste.add(new ClickableComponent());
            paste.add(new CloseOptionMenuOnMouseClickComponent());
            paste.add(new FilePasteOnMouseClickComponent(entity: entity));
            
            Entity delete = new Entity();
            delete.add(new PositionComponent(z: 3));
            delete.add(new RelativePositionComponent(x: 10, y: 30));
            delete.add(new ParentComponent(parent: optionMenu));
            delete.add(new TextComponent(text: "Delete"));
            delete.add(new GetBitmapFontAssetComponent(fileName: "home/programs/os/assets/userosgui/Lucida Console 12px.fnt"));
            delete.add(new OptionMenuComponent());
            delete.add(new ColorComponent(color: Color.BLACK));
            delete.add(new HitBoxComponent(rectangle: new Rectangle(0,-10, 80, 20)));
            delete.add(new ClickableComponent());
            delete.add(new CloseOptionMenuOnMouseClickComponent());
            delete.add(new FileDeleteOnMouseClickComponent(entity: entity));
            //delete.add(new FileSearchOnMouseClickComponent(entity: pathEntity.first()));
            
            this.engine.addEntity(optionMenu);
            this.engine.addEntity(open);
            this.engine.addEntity(rename);
            this.engine.addEntity(cut);
            this.engine.addEntity(copy);
            this.engine.addEntity(paste);
            this.engine.addEntity(delete);
        }
    }
    
    public void removedFromEngine(Engine engine) {
        
    }
}


