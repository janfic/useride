package files.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.assets.*;
import com.badlogic.gdx.math.*;

import com.janfic.useride.kernel.components.*;
import com.janfic.useride.kernel.systems.*;
import os.components.*;
import os.systems.*;
import files.components.*;

public class BootSystem extends EntitySystem {
        
    public void addedToEngine(Engine engine) {
        
        ImmutableArray<Entity> inject = engine.getEntitiesFor(Family.all(ProgramEntityInjectionComponent.class).get());
        ProgramEntityInjectionComponent injection = inject.first().getComponent(ProgramEntityInjectionComponent.class);
        
        for(Entity e : injection.entities) {
            engine.addEntity(e);
        }
               
        Entity assetManagerEntity = new Entity();
        AssetManagerComponent amComp = new AssetManagerComponent();
        amComp.manager = new AssetManager();
        assetManagerEntity.add(amComp);
        assetManagerEntity.add(new LoadAssetsComponent());
        assetManagerEntity.add(new RegisterTextureAssetComponent(fileName: "home/programs/files/assets/folder.png"));
        assetManagerEntity.add(new RegisterTextureAtlasAssetComponent(fileName: "home/programs/os/assets/userosgui/userosgui.atlas"));
        assetManagerEntity.add(new GetTextureAtlasAssetComponent(fileName: "home/programs/os/assets/userosgui/userosgui.atlas"));
        
        Entity background = new Entity();
        background.add(new PositionComponent(x:2, y:1, z:-1));
        background.add(new SizeComponent(width: 497, height: 398));
        background.add(new RegisterTextureAssetComponent(fileName: "home/programs/files/assets/background.png"));
        background.add(new GetTextureAssetComponent(fileName: "home/programs/files/assets/background.png"));
        
        
        Entity grid = new Entity();
        grid.add(new PositionComponent(x: 10, y: 10));
        grid.add(new SizeComponent(width: 75, height: 75));
        
        Entity path = new Entity();
        path.add(new PositionComponent());
        path.add(new SizeComponent(width: 360, height: 20));
        path.add(new RelativePositionComponent(x: 50, y: 485, unit: "%"));
        path.add(new GetNinePatchComponent(name: "container"));
        path.add(new ParentComponent(parent: grid));
        
        Entity pathText = new Entity();
        pathText.add(new TextComponent(text: "/home"));
        pathText.add(new FileSearchComponent());
        pathText.add(new PositionComponent(z: 1));
        pathText.add(new RelativePositionComponent(x: 10, y: 10));
        pathText.add(new KeyInputComponent());
        pathText.add(new ClickableComponent());
        pathText.add(new FocusableComponent());
        pathText.add(new FocusedComponent());
        pathText.add(new ParentComponent(parent: path));
        pathText.add(new FocusOnMouseClickComponent());
        pathText.add(new SizeComponent(width: 400, height: 40));
        pathText.add(new HitBoxComponent(rectangle: new Rectangle(0,0,300,50)));
        pathText.add(new RegisterBitmapFontAssetComponent(fileName: "home/programs/os/assets/userosgui/Lucida Console 12px.fnt"));
        pathText.add(new GetBitmapFontAssetComponent(fileName: "home/programs/os/assets/userosgui/Lucida Console 12px.fnt"));
        pathText.add(new RegisterTextureAssetComponent(fileName: "home/programs/files/assets/file.png"));
        pathText.add(new ColorComponent(color: Color.BLACK));
        //pathText.add(new ScaleComponent(scaleX: 0.75, scaleY: 0.75));

        Entity search = new Entity();
        search.add(new PositionComponent())
        search.add(new RelativePositionComponent(x: 100, y: 0, unit: "%"));
        search.add(new ParentComponent(parent: path));
        search.add(new SizeComponent(width: 80, height: 20));
        search.add(new GetNinePatchComponent(name: "button_up"));
        search.add(new ClickableComponent());
        search.add(new HitBoxComponent(rectangle: new Rectangle(0,0,80,20)));
        search.add(new FileSearchOnMouseClickComponent(entity: pathText));
        
        Entity searchText = new Entity();
        searchText.add(new PositionComponent(z: 1))
        searchText.add(new RelativePositionComponent(x: 30, y: 10));
        searchText.add(new ParentComponent(parent: search));
        searchText.add(new ColorComponent(color: Color.BLACK));
        searchText.add(new TextComponent(text: "Go"));
        searchText.add(new GetBitmapFontAssetComponent(fileName: "home/programs/os/assets/userosgui/Lucida Console 12px.fnt"));
        
        
        engine.addEntity(assetManagerEntity);
        engine.addEntity(path);
        engine.addEntity(pathText);
        engine.addEntity(grid);
        engine.addEntity(search);
        engine.addEntity(searchText);
        engine.addEntity(background);
        
        engine.addSystem(new RenderSystem());
        engine.addSystem(new RelativeSizeSystem());
        engine.addSystem(new RelativePositionSystem());
        engine.addSystem(new AssetLoadSystem());
        engine.addSystem(new AssetGetSystem());
        engine.addSystem(new AssetRegisterSystem());
        engine.addSystem(new TextureAtlasGetSystem());
        engine.addSystem(new MouseClickSystem());
        engine.addSystem(new MouseHoverSystem());
        engine.addSystem(new FileLoadSystem());
        engine.addSystem(new FocusSystem());
        engine.addSystem(new KeyboardInputSystem());
        engine.addSystem(new FocusOnMouseClickSystem());
        engine.addSystem(new TextInputSystem());
        engine.addSystem(new FileSpawnSystem());
        engine.addSystem(new FileSearchOnMouseClickSystem());
        engine.addSystem(new FileSearchOnMouseDoubleClickSystem());
        engine.addSystem(new OptionMenuSystem());
    }
    
    public void update(float delta) {
        
    }
    
    public void removedFromEngine(Engine engine) {
        
    }
}