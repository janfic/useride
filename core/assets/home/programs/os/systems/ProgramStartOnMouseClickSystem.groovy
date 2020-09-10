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
            
            if(click.count >= 2 ) {
            
                ProgramStartRequestComponent startRequest = new ProgramStartRequestComponent(name: start.path);
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
                program.add(new FocusableComponent());
                program.add(new FocusBringToFrontComponent());
                program.add(new FocusOnMouseClickComponent());

                Entity topbar = new Entity();
                
                topbar.add(new PositionComponent(x: 100 , y: 100, z: 2));
                topbar.add(new RelativePositionComponent(x: 0, y: 100, z:0, unit: "p%p"));
                topbar.add(new GetNinePatchComponent(name: "topbar"));
                topbar.add(new SizeComponent(width: 100, height: 25));
                topbar.add(new ParentComponent(parent: program));
                topbar.add(new RelativeSizeComponent(width: 100, unit: "% "));
                
                Entity titleText = new Entity();

                titleText.add(new PositionComponent(z: 2));
                titleText.add(new SizeComponent());
                titleText.add(new RelativePositionComponent(x: 2, y: 40, z:1, unit: "%%p"));
                titleText.add(new ParentComponent(parent: topbar));
                titleText.add(new TextComponent(text: start.name));
                titleText.add(new GetBitmapFontAssetComponent(fileName: "home/programs/os/assets/userosgui/Lucida Console.fnt"));
                titleText.add(new ColorComponent(color: Color.BLACK))

                Entity xButton = new Entity();
                xButton.add(new PositionComponent(z: 100,  y: 100));
                xButton.add(new RelativePositionComponent(x: 95, y: 15, z: 1, unit: "%%p"));
                xButton.add(new ParentComponent(parent: topbar));
                xButton.add(new GetTextureRegionComponent(name: "x"));
                xButton.add(new SizeComponent(width: 15, height: 15));
                xButton.add(new ColorComponent(color: Color.WHITE));
                xButton.add(new HitBoxComponent(rectangle: new Rectangle(0,0,13,13)));
                xButton.add(new ClickableComponent());
                xButton.add(new HoverableComponent());
                xButton.add(new ChangeColorOnMouseHoverComponent(hoverColor: Color.LIGHT_GRAY, offColor: Color.WHITE));
                xButton.add(new ProgramEndOnMouseClickComponent(name: start.name));
                xButton.add(new RequestProgramIDComponent(program: program));
                xButton.add(new RemoveEntitiesOnMouseClickComponent(entities: [program, titleText, topbar, xButton]));
                
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

                this.getEngine().addEntity(program);
                this.getEngine().addEntity(topbar);
                this.getEngine().addEntity(titleText);
                this.getEngine().addEntity(xButton);
                
                entity.remove(MouseClickEventComponent.class);
                entity.remove(MousePressEventComponent.class);
                entity.remove(MouseReleaseEventComponent.class);
            }
        }
    }

    public void removedFromEngine(Engine engine) {
	
    }
}