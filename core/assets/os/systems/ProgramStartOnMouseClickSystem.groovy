package os.systems;

import os.components.*;
import com.janfic.useride.kernel.components.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.assets.*;
import com.badlogic.gdx.utils.viewport.*;

public class ProgramStartOnMouseClickSystem extends EntitySystem {
	
    private final ComponentMapper<ProgramStartOnMouseClickComponent> programStartMapper;
    private final ComponentMapper<MouseClickEventComponent> clickMapper;

    private ImmutableArray<Entity> entities, renderEntities;

    public ProgramStartOnMouseClickSystem() {
        this.programStartMapper = ComponentMapper.getFor(ProgramStartOnMouseClickComponent.class);
        this.clickMapper = ComponentMapper.getFor(MouseClickEventComponent.class);
    }
	
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(
                ProgramStartOnMouseClickComponent.class,
                ClickableComponent.class,
                MouseClickEventComponent.class
            ).get()
        );
        this.renderEntities = engine.getEntitiesFor(
            Family.all(
                SpriteBatchComponent.class,
                ViewportComponent.class
            ).get()
        );
    }

    public void update(float delta) {
        for(Entity entity : entities) {
            ProgramStartOnMouseClickComponent start = programStartMapper.get(entity);
            MouseClickEventComponent click = clickMapper.get(entity);

            if(click.count >= 2 && start.started == false) {
                ProgramStartRequestComponent startRequest = new ProgramStartRequestComponent(name: start.name);
                FileLoadRequestComponent fileRequest = new FileLoadRequestComponent(fileName: start.path);

                Entity program = new Entity();
                program.add(startRequest);
                program.add(fileRequest);

                program.add(new GetNinePatchComponent(name: "window"));
                program.add(new PositionComponent(x: 200, y: 100, z: 2));
                program.add(new SizeComponent(width: 500, height: 400));
                program.add(new HitBoxComponent(rectangle: new Rectangle(0, 400, 500, 35)));
                program.add(new DragableComponent());
                program.add(new ClickableComponent());

                Entity topbar = new Entity();
                
                topbar.add(new PositionComponent(x: 100 , y: 100, z: 2));
                topbar.add(new RelativePositionComponent(y: 100, unit: "%"));
                topbar.add(new GetNinePatchComponent(name: "topbar"));
                topbar.add(new SizeComponent(width: 100, height: 35));
                topbar.add(new ParentComponent(parent: program));
                topbar.add(new RelativeSizeComponent(width: 100, unit: "% "));
                
                Entity titleText = new Entity();

                titleText.add(new PositionComponent(z: 2));
                titleText.add(new SizeComponent());
                titleText.add(new RelativePositionComponent(x: 2, y: 50, unit: "%"));
                titleText.add(new ParentComponent(parent: topbar));
                titleText.add(new TextComponent(text: start.name));
                titleText.add(new GetBitmapFontAssetComponent(fileName: "os/assets/userosgui/Lucida Console.fnt"));
                titleText.add(new ColorComponent(color: Color.BLACK))

                ProgramEntityInjectionComponent inject = new ProgramEntityInjectionComponent();
                inject.entities = new ArrayList<Entity>();
				
                Entity graphicsEntity = new Entity();
                ViewportComponent viewportComponent = new ViewportComponent();
                viewportComponent.viewport = new FitViewport(500, 400);
                SpriteBatchComponent spriteBatchComponent = renderEntities.first().getComponent(SpriteBatchComponent.class);
                graphicsEntity.add(spriteBatchComponent);
                graphicsEntity.add(viewportComponent);
				
                Entity assetManager = new Entity();
                AssetManagerComponent assetManagerComponent = new AssetManagerComponent();
                assetManagerComponent.manager = new AssetManager();
                assetManager.add(assetManagerComponent);


                inject.entities.add(assetManager);
                inject.entities.add(graphicsEntity);
				
                program.add(inject);
                program.add(viewportComponent);

                start.started = true;
                this.getEngine().addEntity(program);
                this.getEngine().addEntity(topbar);
                this.getEngine().addEntity(titleText);
            }
        }
    }

    public void removedFromEngine(Engine engine) {
	
    }
}