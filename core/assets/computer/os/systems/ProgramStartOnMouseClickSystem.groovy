package os.systems;

import os.components.*;
import ui.components.*;
import com.janfic.useride.kernel.components.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.assets.*;
import com.badlogic.gdx.utils.viewport.*;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.Gdx;

import java.util.Properties;

import groovy.transform.CompileStatic;

@CompileStatic
public class ProgramStartOnMouseClickSystem extends EntitySystem {
	
    private final ComponentMapper<ProgramStartOnMouseClickComponent> programStartMapper;
    private final ComponentMapper<MouseClickEventComponent> clickMapper;
    private final ComponentMapper<PropertiesComponent> propertiesMapper;

    private ImmutableArray<Entity> entities, renderEntities;

    public ProgramStartOnMouseClickSystem() {
        this.programStartMapper = ComponentMapper.getFor(ProgramStartOnMouseClickComponent.class);
        this.clickMapper = ComponentMapper.getFor(MouseClickEventComponent.class);
        this.propertiesMapper = ComponentMapper.getFor(PropertiesComponent.class);
    }
	
    public void addedToEngine(Engine engine) {
        this.entities = engine.getEntitiesFor(
            Family.all(
                ProgramStartOnMouseClickComponent.class,
                ClickableComponent.class,
                MouseClickEventComponent.class,
                PropertiesComponent.class
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
            PropertiesComponent propertiesComponent = propertiesMapper.get(entity);
            
            if(click.count >= 2 ) {
            
                ProgramStartRequestComponent startRequest = new ProgramStartRequestComponent(name: propertiesComponent.properties.getProperty("name"));
                FileLoadRequestComponent fileRequest = new FileLoadRequestComponent(fileName: start.path);
                
                int centerWidth = (int)(Gdx.graphics.getWidth() / 2) - (int)(Integer.parseInt(propertiesComponent.properties.getProperty("width")) / 2); ///
                int centerHeight = (int)(Gdx.graphics.getHeight() / 2) - (int)(Integer.parseInt(propertiesComponent.properties.getProperty("height")) / 2); ///
                
                Entity leftResize = new Entity();
                Entity rightResize = new Entity();
                Entity topResize = new Entity();
                Entity bottomResize = new Entity();
                Entity topbar = new Entity();
                Entity program = new Entity();
                
                leftResize.add(new PositionComponent(x: centerWidth, y: centerHeight));
                leftResize.add(new HitBoxComponent(rectangle: new Rectangle(0,0, 10, Integer.parseInt(propertiesComponent.properties.getProperty("height")))));
                leftResize.add(new ClickableComponent());
                leftResize.add(new DragableComponent(verticle: false));
                leftResize.add(new MoveOnDragComponent());
                leftResize.add(new SizeComponent(width: 10, height: Integer.parseInt(propertiesComponent.properties.getProperty("height"))));
                leftResize.add(new RelativePositionComponent(x: 0, y: 0, unit: "%p", parent: bottomResize));
                leftResize.add(new ParentComponent(parent: program));
                leftResize.add(new HeightBetweenComponent(a: topResize, b: bottomResize));
                
                rightResize.add(new PositionComponent(x: centerWidth + Integer.parseInt(propertiesComponent.properties.getProperty("width")), y: centerHeight));
                rightResize.add(new HitBoxComponent(rectangle: new Rectangle(0,0, 10, Integer.parseInt(propertiesComponent.properties.getProperty("height")))));
                rightResize.add(new ClickableComponent());
                rightResize.add(new DragableComponent(verticle: false));
                rightResize.add(new MoveOnDragComponent());
                rightResize.add(new SizeComponent(width: 10, height: Integer.parseInt(propertiesComponent.properties.getProperty("height"))));
                rightResize.add(new RelativePositionComponent(x: 100, y: 0, unit: "%p", parent: program));
                rightResize.add(new ParentComponent(parent: program));
                rightResize.add(new HeightBetweenComponent(a: topResize, b: bottomResize));
                
                topResize.add(new PositionComponent(x: centerWidth, y: centerHeight + Integer.parseInt(propertiesComponent.properties.getProperty("height"))));
                topResize.add(new HitBoxComponent(rectangle: new Rectangle(0,0, Integer.parseInt(propertiesComponent.properties.getProperty("width")), 10)));
                topResize.add(new ClickableComponent());
                topResize.add(new MoveOnDragComponent());
                topResize.add(new DragableComponent(horizontal: false));
                topResize.add(new SizeComponent(width: Integer.parseInt(propertiesComponent.properties.getProperty("width")), height: 35));
                topResize.add(new RelativePositionComponent(x: 0, y: 100, unit: "p%", parent: program));
                topResize.add(new ParentComponent(parent: program));
                topResize.add(new WidthBetweenComponent(a: leftResize, b: rightResize));
                
                bottomResize.add(new PositionComponent(x: centerWidth, y: centerHeight));
                bottomResize.add(new HitBoxComponent(rectangle: new Rectangle(0,0, Integer.parseInt(propertiesComponent.properties.getProperty("width")), 10)));
                bottomResize.add(new ClickableComponent());
                bottomResize.add(new MoveOnDragComponent());
                bottomResize.add(new DragableComponent(horizontal: false));
                bottomResize.add(new SizeComponent(width: Integer.parseInt(propertiesComponent.properties.getProperty("width")), height: 10));
                bottomResize.add(new RelativePositionComponent(x: 0, y: 0));
                bottomResize.add(new WidthBetweenComponent(a: leftResize, b: rightResize));
                bottomResize.add(new ParentComponent(parent: program));
                
                
                program.add(startRequest);
                program.add(fileRequest);

                
                program.add(new GetNinePatchComponent(name: "window"));
                program.add(new PositionComponent(x: 0, y: 0, z: 2));
                program.add(new SizeComponent(width: Integer.parseInt(propertiesComponent.properties.getProperty("width")), height: Integer.parseInt(propertiesComponent.properties.getProperty("height"))));
                //program.add(new HitBoxComponent(rectangle: new Rectangle(0, 0, Integer.parseInt(propertiesComponent.properties.getProperty("width")), Integer.parseInt(propertiesComponent.properties.getProperty("height")))));
                //program.add(new ClickableComponent());
                program.add(new FocusableComponent());
                program.add(new FocusBringToFrontComponent());
                program.add(new FocusOnMouseClickComponent());
                program.add(new WidthBetweenComponent(a: leftResize, b: rightResize));
                program.add(new HeightBetweenComponent(a: topResize, b: bottomResize));
                program.add(new RelativePositionComponent(x: 0, y: -100, unit: "ps"));
                program.add(new ParentComponent(parent: topbar));
                
                
                topbar.add(new PositionComponent(x: centerWidth , y: centerHeight, z: 2));
                topbar.add(new GetNinePatchComponent(name: "topbar"));
                topbar.add(new SizeComponent(width: 100, height: 25));
                topbar.add(new RelativeSizeComponent(width: 100, unit: "% "));
                topbar.add(new RelativePositionComponent(x: 0, y: 100, unit: "p% ", parent: leftResize));
                topbar.add(new DragableComponent());
                topbar.add(new MoveOnDragComponent());
                topbar.add(new ClickableComponent());
                topbar.add(new ParentComponent(parent: program))
                topbar.add(new HitBoxComponent(rectangle: new Rectangle(0, 0, Integer.parseInt(propertiesComponent.properties.getProperty("width")), 35)));
                
                Entity titleText = new Entity();

                titleText.add(new PositionComponent(z: 2));
                titleText.add(new SizeComponent());
                titleText.add(new RelativePositionComponent(x: 2, y: 20, z:1, unit: "%%p"));
                titleText.add(new ParentComponent(parent: topbar));
                titleText.add(new TextComponent(text: propertiesComponent.properties.getProperty("displayName")));
                titleText.add(new GetBitmapFontAssetComponent(fileName: "computer/os/assets/userosgui/Lucida Console.fnt"));
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
                xButton.add(new RemoveEntitiesOnMouseClickComponent(entities: [program, titleText, topbar, xButton, leftResize, rightResize, topResize, bottomResize]));
                
                ProgramEntityInjectionComponent inject = new ProgramEntityInjectionComponent();
                inject.entities = new ArrayList<Entity>();
				
                Entity graphicsEntity = new Entity();
                ViewportComponent viewportComponent = new ViewportComponent();
                viewportComponent.viewport = new FitViewport(Integer.parseInt(propertiesComponent.properties.getProperty("width")), Integer.parseInt(propertiesComponent.properties.getProperty("height")));
                SpriteBatchComponent spriteBatchComponent = renderEntities.first().getComponent(SpriteBatchComponent.class);
                FrameBufferComponent frameBuffer = new FrameBufferComponent(frameBuffer: new FrameBuffer(Pixmap.Format.RGBA8888, Integer.parseInt(propertiesComponent.properties.getProperty("width")), Integer.parseInt(propertiesComponent.properties.getProperty("height")), true));
                SizeComponent size = new SizeComponent(width: Integer.parseInt(propertiesComponent.properties.getProperty("width")), height: Integer.parseInt(propertiesComponent.properties.getProperty("height")));
                graphicsEntity.add(spriteBatchComponent);
                graphicsEntity.add(frameBuffer);
                graphicsEntity.add(viewportComponent);
                graphicsEntity.add(size);
                
                
                program.add(frameBuffer);
				
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
                this.getEngine().addEntity(leftResize);
                this.getEngine().addEntity(rightResize);
                this.getEngine().addEntity(topResize);
                this.getEngine().addEntity(bottomResize);
                
                entity.remove(MouseClickEventComponent.class);
                entity.remove(MousePressEventComponent.class);
                entity.remove(MouseReleaseEventComponent.class);
            }
        }
    }

    public void removedFromEngine(Engine engine) {
	
    }
}