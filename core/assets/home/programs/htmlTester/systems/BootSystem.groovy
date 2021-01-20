package htmlTester.systems;

import com.janfic.useride.kernel.components.*;
import os.components.*;
import os.systems.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.gdx.*;
import htmlTester.components.*;
import ui.components.*;
import ui.systems.*;

import java.util.Arrays;

public class BootSystem extends EntitySystem {

    @Override
    public void addedToEngine(Engine engine) {
        ImmutableArray<Entity> inject = engine.getEntitiesFor(Family.all(ProgramEntityInjectionComponent.class).get());
        ProgramEntityInjectionComponent injection = inject.first().getComponent(ProgramEntityInjectionComponent.class);

        ProgramOutputComponent outputComponent = null;
        
        for(Entity e : injection.entities) {
            engine.addEntity(e);  
        }
        
        ImmutableArray<Entity> es = engine.getEntitiesFor(Family.all(ViewportComponent.class, SizeComponent.class).get());
        Entity viewportEntity = es.first();
        
        Entity e = new Entity();
        HTMLComponent html = new HTMLComponent();
        html.html = "<!DOCTYPE html><html lang='en'><head><meta charset='utf-8'></head><body><div class='parent'><p class='test'>Hello World</p><p class='test'>This is another text</p></div></body></html>";

        CSSComponent css = new CSSComponent();
        css.css = ".parent { color: #FF00FF; width: 100%; height: 100%; background-color: white; } .test { background-color: yellow; padding: 10%; margin: 10px; }"
        
        SizeComponent size = new SizeComponent(width: 0, height: 0);
        PositionComponent position = new PositionComponent(x: 0, y: 0);
        WidthComponent width = new WidthComponent(width: 1, type: "%");
        HeightComponent height = new HeightComponent(height: 1, type: "%");
        ParentComponent parent = new ParentComponent(parent: viewportEntity);
        
        e.add(size);
        e.add(position);
        e.add(css);
        e.add(html);
        e.add(width);
        e.add(height);
        e.add(parent);
        
        engine.addEntity(e);
        
        engine.addSystem(new HTMLToECSSystem());
        engine.addSystem(new CSSToECSSystem());
        engine.addSystem(new CSSPositioningSystem());

        engine.addSystem(new RenderSystem());
        engine.addSystem(new AssetGetSystem());
        engine.addSystem(new AssetLoadSystem());
        engine.addSystem(new AssetRegisterSystem());

        super.addedToEngine(engine);
        
        engine.removeSystem(this);
    }
}