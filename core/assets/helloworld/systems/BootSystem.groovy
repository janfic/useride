package helloworld.systems;

import com.janfic.useride.kernel.components.*;
import os.components.*;
import os.systems.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.gdx.graphics.*;
import helloworld.components.*;

public class BootSystem extends EntitySystem {

    @Override
    public void addedToEngine(Engine engine) {
        ImmutableArray<Entity> inject = engine.getEntitiesFor(Family.all(ProgramEntityInjectionComponent.class).get());
        ProgramEntityInjectionComponent injection = inject.first().getComponent(ProgramEntityInjectionComponent.class);

        for(Entity e : injection.entities) {
            engine.addEntity(e);  
        }

        Entity helloworld = new Entity();
        helloworld.add(new PositionComponent(x: 215, y: 200));
        helloworld.add(new TextComponent(text: "Hello World!"));
        RegisterBitmapFontAssetComponent registerFont = new RegisterBitmapFontAssetComponent(fileName: "os/assets/userosgui/Lucida Console.fnt");
        helloworld.add(registerFont);
        helloworld.add(new LoadAssetsComponent())
        helloworld.add(new GetBitmapFontAssetComponent(fileName: "os/assets/userosgui/Lucida Console.fnt"));

        engine.addEntity(helloworld);

        engine.addSystem(new RenderSystem());
        engine.addSystem(new AssetGetSystem());
        engine.addSystem(new AssetLoadSystem());
        engine.addSystem(new AssetRegisterSystem());
        engine.addSystem(new PrintTextSystem());

        super.addedToEngine(engine);
        
        engine.removeSystem(this);
    }
}