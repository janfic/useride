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
				program.add(new PositionComponent(x: 200, y: 100));
				program.add(new SizeComponent(width: 500, height: 400));
				program.add(new HitBoxComponent(rectangle: new Rectangle(0, 365, 500, 35)));
				program.add(new DragableComponent());
				program.add(new ClickableComponent());
				program.add(new ColorComponent(color: Color.LIGHT_GRAY));
				program.add(new TableSizeComponent(cellWidth: 25, cellHeight: 25));

				Entity titleText = new Entity();

				titleText.add(new PositionComponent(z: 1));
				titleText.add(new SizeComponent());
				titleText.add(new TablePositionComponent(x: 1, y: 15.25f));
				titleText.add(new TableComponent(table: program));
				titleText.add(new TextComponent(text: start.name));
				titleText.add(new GetBitmapFontAssetComponent(fileName: "os/assets/userosgui/Lucida Console.fnt"));


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
				this.getEngine().addEntity(titleText);
			}
		}
	}

	public void removedFromEngine(Engine engine) {
	
	}
}