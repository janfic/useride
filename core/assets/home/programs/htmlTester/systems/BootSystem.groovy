package htmltester.systems;

import com.janfic.useride.kernel.components.*;
import os.components.*;
import os.systems.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.gdx.*;
import com.badlogic.gdx.*;
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
        html.html = Gdx.files.local("home/programs/htmlTester/assets/index.html").readString();

        CSSComponent css = new CSSComponent();
        css.css =  Gdx.files.local("home/programs/htmlTester/assets/styles.css").readString();
        
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
        engine.addSystem(new ViewportSystem());
        engine.addSystem(new KeyboardInputSystem())
        engine.addSystem(new MouseClickSystem());
        engine.addSystem(new MouseHoverSystem());
        engine.addSystem(new TextInputSystem());
        engine.addSystem(new TextSelectionSystem());
        engine.addSystem(new RelativePositionSystem());
        engine.addSystem(new FocusOnMouseClickSystem());
        engine.addSystem(new ChangeBackgroundColorOnFocusedSystem());
        engine.addSystem(new ChangeBorderColorOnFocusedSystem());
        engine.addSystem(new TextDragSystem());
        engine.addSystem(new DragSystem());

        super.addedToEngine(engine);
        
        engine.removeSystem(this);
    }
}