package groovyshell.systems;

import os.components.*;
import os.systems.*;
import groovyshell.components.*;
import com.janfic.useride.kernel.components.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.gdx.graphics.*;

import groovy.lang.GroovyShell;

public class BootSystem extends EntitySystem {
    
    @Override
    public void addedToEngine(Engine engine) {
        ImmutableArray<Entity> inject = engine.getEntitiesFor(Family.all(ProgramEntityInjectionComponent.class).get());
        ProgramEntityInjectionComponent injection = inject.first().getComponent(ProgramEntityInjectionComponent.class);

        for(Entity e : injection.entities) {
            engine.addEntity(e);  
        }
        
        Entity groovyShellCore = new Entity();
        
        RegisterBitmapFontAssetComponent registerFont = new RegisterBitmapFontAssetComponent(fileName: "os/assets/userosgui/Lucida Console.fnt");
        groovyShellCore.add(registerFont);
        groovyShellCore.add(new LoadAssetsComponent());
        
        Entity input = new Entity();
        input.add(new TextComponent(text: ""));
        input.add(new FocusableComponent());
        input.add(new KeyInputComponent());
        input.add(new GetBitmapFontAssetComponent(fileName: "os/assets/userosgui/Lucida Console.fnt"));
        
        
        Entity output = new Entity();
        
        Entity shell = new Entity();
        shell.add(new GroovyShellComponent(shell: new GroovyShell()));
        
        
        engine.addSystem(new RenderSystem());
        engine.addSystem(new MouseClickSystem());
        engine.addSystem(new TableSystem());
        engine.addSystem(new AssetGetSystem());
        engine.addSystem(new AssetLoadSystem());
        engine.addSystem(new AssetRegisterSystem());
    }
}