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
        
        
        Entity textEntry = new Entity();
        textEntry.add(new PositionComponent(x: 10, y: 20));
        textEntry.add(new GetBitmapFontAssetComponent(fileName: "home/programs/os/assets/userosgui/Lucida Console 12px.fnt"));
        textEntry.add(new TextComponent(text: "[U]:"));
        textEntry.add(new ColorComponent(color: Color.WHITE));
        textEntry.add(new SizeComponent(width: 400, height: 20));
        textEntry.add(new HitBoxComponent(rectangle: new Rectangle(0,0,100, 20)));
        textEntry.add(new ClickableComponent());
        textEntry.add(new FocusableComponent());
        textEntry.add(new FocusOnMouseClickComponent());
        textEntry.add(new KeyInputComponent());
        
        
        engine.addEntity(textEntry);
        engine.addEntity(assetManagerEntity);
        
        engine.addSystem(new TextInputSystem());
        engine.addSystem(new RenderSystem());
        engine.addSystem(new KeyboardInputSystem());
        engine.addSystem(new AssetLoadSystem());
        engine.addSystem(new AssetGetSystem());
        engine.addSystem(new AssetRegisterSystem());
        engine.addSystem(new MouseClickSystem());
        engine.addSystem(new MouseHoverSystem());
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