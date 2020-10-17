package terminal.systems;

import com.janfic.useride.kernel.components.*;
import com.janfic.useride.kernel.systems.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.viewport.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.assets.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.input.*;
import com.badlogic.gdx.math.*;
import os.components.*;
import os.systems.*;


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
        assetManagerEntity.add(new RegisterTextureAtlasAssetComponent(fileName: "home/programs/os/assets/userosgui/userosgui.atlas"));
        assetManagerEntity.add(new RegisterBitmapFontAssetComponent(fileName: "home/programs/os/assets/userosgui/Lucida Console 12px.fnt"));
        assetManagerEntity.add(new GetTextureAtlasAssetComponent(fileName: "home/programs/os/assets/userosgui/userosgui.atlas"));
        
        Entity scrollAnchor = new Entity();
        Entity scrollBar = new Entity();
        
        Entity textEntry = new Entity();
        textEntry.add(new PositionComponent(x: 0, y: 0));
        textEntry.add(new RelativePositionComponent(x: 10, y: 20));
        textEntry.add(new ParentComponent(parent: scrollAnchor))
        textEntry.add(new GetBitmapFontAssetComponent(fileName: "home/programs/os/assets/userosgui/Lucida Console 12px.fnt"));
        textEntry.add(new TextComponent(text: "[U]:"));
        textEntry.add(new ColorComponent(color: Color.WHITE));
        textEntry.add(new SizeComponent(width: 400, height: 20));
        textEntry.add(new HitBoxComponent(rectangle: new Rectangle(0,0,100, 20)));
        textEntry.add(new ClickableComponent());
        textEntry.add(new FocusableComponent());
        textEntry.add(new FocusOnMouseClickComponent());
        textEntry.add(new KeyInputComponent());
        
        scrollAnchor.add(new PositionComponent());
        scrollAnchor.add(new SizeComponent());
        scrollAnchor.add(new RelativePositionComponent(x: 0, y: 0, parentMultiplier: -1.0, unit: " p"));
        scrollAnchor.add(new ParentComponent(parent: scrollBar));
        
        scrollBar.add(new PositionComponent(x: 485, y: 0));
        scrollBar.add(new SizeComponent(width: 10, height: 40));
        scrollBar.add(new GetNinePatchComponent(name: "button_up"));
        scrollBar.add(new ClickableComponent());
        scrollBar.add(new HitBoxComponent(rectangle: new Rectangle(0,0,10,40)));
        scrollBar.add(new DragableComponent());
        scrollBar.add(new PositionBoundsComponent(minX: 485, maxX: 485, minY: 5, maxY: 355));
        
        engine.addEntity(textEntry);
        engine.addEntity(scrollAnchor);
        engine.addEntity(scrollBar);
        engine.addEntity(assetManagerEntity);
        
        engine.addSystem(new TextInputSystem());
        engine.addSystem(new RenderSystem());
        engine.addSystem(new MouseClickSystem());
        engine.addSystem(new MouseHoverSystem());
        engine.addSystem(new RelativePositionSystem());
        engine.addSystem(new DragSystem());
        engine.addSystem(new PositionBoundsSystem());
        engine.addSystem(new KeyboardInputSystem());
        engine.addSystem(new AssetLoadSystem());
        engine.addSystem(new AssetGetSystem());
        engine.addSystem(new AssetRegisterSystem());
        engine.addSystem(new TextureAtlasGetSystem());
        engine.addSystem(new FocusSystem());
        engine.addSystem(new KeyboardInputSystem());
        engine.addSystem(new FocusOnMouseClickSystem());
        
        engine.removeSystem(this);
    }
    
    public void update(float delta) {
        
    }
    
    public void removedFromEngine(Engine engine) {
        
    }
    
}