package helloworld.systems;

import com.janfic.useride.kernel.components.*;
import os.components.*;
import os.systems.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.gdx.*;
import helloworld.components.*;

public class BootSystem extends EntitySystem {

    @Override
    public void addedToEngine(Engine engine) {
        ImmutableArray<Entity> inject = engine.getEntitiesFor(Family.all(ProgramEntityInjectionComponent.class).get());
        ProgramEntityInjectionComponent injection = inject.first().getComponent(ProgramEntityInjectionComponent.class);

        ProgramOutputComponent outputComponent = null;
        
        for(Entity e : injection.entities) {
            engine.addEntity(e);  
            if(e.getComponent(ProgramOutputComponent.class) != null) {
                outputComponent = e.getComponent(ProgramOutputComponent.class);
                outputComponent.lines.add("Hello World!");
            }
        }
        

        Entity helloworld = new Entity();
        helloworld.add(new PositionComponent(x: 100, y: 200));
        helloworld.add(new TextComponent(text: "Hello World!"));
        RegisterBitmapFontAssetComponent registerFont = new RegisterBitmapFontAssetComponent(fileName: "computer/os/assets/userosgui/Lucida Console.fnt");
        helloworld.add(registerFont);
        helloworld.add(new LoadAssetsComponent())
        helloworld.add(new ColorComponent(color: Color.RED));
        helloworld.add(new GetBitmapFontAssetComponent(fileName: "computer/os/assets/userosgui/Lucida Console.fnt"));
        helloworld.add(new KeyInputComponent())
        helloworld.add(new FocusedComponent())
        
        engine.addEntity(helloworld);

        engine.addSystem(new RenderSystem());
        engine.addSystem(new AssetGetSystem());
        engine.addSystem(new AssetLoadSystem());
        engine.addSystem(new AssetRegisterSystem());
        engine.addSystem(new PrintTextSystem());
        engine.addSystem(new TextInputSystem());
        engine.addSystem(new KeyboardInputSystem());

        super.addedToEngine(engine);
        
        engine.removeSystem(this);
    }
}