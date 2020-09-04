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
        assetManagerEntity.add(new RegisterTextureAssetComponent(fileName: "files/assets/folder.png"));
        
        Entity background = new Entity();
        background.add(new PositionComponent(x:2, y:1, z:-1));
        background.add(new SizeComponent(width: 497, height: 398));
        background.add(new RegisterTextureAssetComponent(fileName: "files/assets/background.png"));
        background.add(new GetTextureAssetComponent(fileName: "files/assets/background.png"));
        
        
        Entity grid = new Entity();
        grid.add(new PositionComponent(x: 10, y: 10));
        grid.add(new SizeComponent(width: 75, height: 75));
        
        Entity path = new Entity();
        path.add(new TextComponent(text: "os"));
        path.add(new FileSearchComponent());
        path.add(new PositionComponent());
        path.add(new RelativePositionComponent(x: 0, y: 500, unit: "%"));
        path.add(new ParentComponent(parent: grid));
        path.add(new KeyInputComponent());
        path.add(new ClickableComponent());
        path.add(new FocusableComponent());
        path.add(new FocusedComponent());
        path.add(new FocusOnMouseClickComponent());
        path.add(new SizeComponent(width: 10, height: 30));
        path.add(new HitBoxComponent(rectangle: new Rectangle(0,0,300,50)));
        path.add(new RegisterBitmapFontAssetComponent(fileName: "os/assets/userosgui/Lucida Console.fnt"));
        path.add(new GetBitmapFontAssetComponent(fileName: "os/assets/userosgui/Lucida Console.fnt"));
        path.add(new RegisterTextureAssetComponent(fileName: "files/assets/file.png"));
        path.add(new ColorComponent(color: Color.BLACK));
        
        
        engine.addEntity(assetManagerEntity);
        engine.addEntity(path);
        engine.addEntity(grid);
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
    }
    
    public void update(float delta) {
        
    }
    
    public void removedFromEngine(Engine engine) {
        
    }
}