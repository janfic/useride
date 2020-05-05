package os.systems;

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
		Entity assetManagerEntity = new Entity();
		AssetManagerComponent amComp = new AssetManagerComponent();
		amComp.manager = new AssetManager();
		assetManagerEntity.add(amComp);

		RegisterTextureAssetComponent registerTexture = new RegisterTextureAssetComponent();
		registerTexture.fileName = "os/assets/badlogic.jpg";
		assetManagerEntity.add(registerTexture);

		RegisterTextureAtlasAssetComponent registerAtlas = new RegisterTextureAtlasAssetComponent();
		registerAtlas.fileName = "os/assets/userosgui/userosgui.atlas";
		assetManagerEntity.add(registerAtlas);

		GetTextureAtlasAssetComponent getAtlas = new GetTextureAtlasAssetComponent();
		getAtlas.fileName = registerAtlas.fileName;
		assetManagerEntity.add(getAtlas);

		LoadAssetsComponent loadAssetsComponent = new LoadAssetsComponent();
		assetManagerEntity.add(loadAssetsComponent);

		ViewportComponent viewportComponent = new ViewportComponent();
		viewportComponent.viewport = new ScreenViewport();
		viewportComponent.viewport.setWorldSize(1920, 1080);
		assetManagerEntity.add(viewportComponent);

		SizeComponent viewportSize = new SizeComponent(width: 1920, height: 1080);
		assetManagerEntity.add(viewportSize);

		PositionComponent viewportPosition = new PositionComponent();
		assetManagerEntity.add(viewportPosition);

		SpriteBatchComponent sbComponent = new SpriteBatchComponent();
		sbComponent.batch = new SpriteBatch();
		assetManagerEntity.add(sbComponent);

		this.engine.addEntity(assetManagerEntity);

		Entity table = new Entity();
		
		PositionComponent tablePosition = new PositionComponent();
		tablePosition.x = 0
		tablePosition.y = 0;
		tablePosition.z = 0;

		TableSizeComponent tableSize = new TableSizeComponent();
		tableSize.cellWidth = viewportComponent.viewport.getWorldWidth() / 20;
		tableSize.cellHeight = viewportComponent.viewport.getWorldHeight() / 20;
		tableSize.width = 20;
		tableSize.height = 20;

		table.add(tablePosition);
		table.add(tableSize);

		Entity topbar = new Entity();

		GetNinePatchComponent getNinePatch = new GetNinePatchComponent()
		getNinePatch.name = "box";

		PositionComponent topbarPosition = new PositionComponent();
		topbarPosition.x = 0;
		topbarPosition.y = 0;

		SizeComponent size = new SizeComponent();
		size.width = 100;
		size.height = 200;

		TableComponent tableComponent = new TableComponent();
		tableComponent.table = table;

		TablePositionComponent tablePositionComponent = new TablePositionComponent();
		tablePositionComponent.x = 0;
		tablePositionComponent.y = 19;

		TableSpanComponent tableSpan = new TableSpanComponent();
		tableSpan.height = 1;
		tableSpan.width = 20;

		TableSizeComponent topbarTableSize = new TableSizeComponent();
		topbarTableSize.cellWidth = tableSize.cellHeight;
		topbarTableSize.cellHeight = tableSize.cellHeight;

		ColorComponent topbarColor = new ColorComponent(color: Color.LIGHT_GRAY);

		topbar.add(size);
		topbar.add(topbarColor);
		topbar.add(tableComponent);
		topbar.add(tablePositionComponent);
		topbar.add(tableSpan);
		topbar.add(topbarPosition);
		topbar.add(getNinePatch);
		topbar.add(topbarTableSize);

		Entity menuButton = new Entity();
		
		PositionComponent menuPosition = new PositionComponent(x: 100, y:100, z:2);
		TablePositionComponent menuTablePositionComponent = new TablePositionComponent(x: 0, y: 0);
		TableComponent menuTableComponent = new TableComponent(table: topbar);
		SizeComponent menuButtonSizeComponent = new SizeComponent(width: 52, height: 52);
		GetTextureRegionComponent getMenuIcon = new GetTextureRegionComponent(name: 'menu');
		ColorComponent menuButtonColor = new ColorComponent(color: Color.GRAY);

		menuButton.add(menuPosition);
		menuButton.add(menuTableComponent);
		menuButton.add(menuTablePositionComponent);
		menuButton.add(menuButtonSizeComponent);
		menuButton.add(getMenuIcon);
		menuButton.add(menuButtonColor);

		Entity textEntity = new Entity();

		PositionComponent textPosition = new PositionComponent(x: 100, y: 200, z:6);
		RegisterBitmapFontAssetComponent registerFont = new RegisterBitmapFontAssetComponent(fileName: "os/assets/userosgui/Lucida Console.fnt");
		GetBitmapFontAssetComponent getFont = new GetBitmapFontAssetComponent(fileName: registerFont.fileName);
		TextComponent textComponent = new TextComponent(text: "Hello World!");
		ColorComponent textColor = new ColorComponent(color: Color.GRAY);
		TablePositionComponent textTablePositionComponent = new TablePositionComponent(x: 32, y: 0.5);
		TableComponent textTableComponent = new TableComponent(table: topbar);
		TableSpanComponent textTableSpanComponent = new TableSpanComponent(height:1, width:3);
		SizeComponent textSizeComponent = new SizeComponent();
		TextToTimeComponent t2t = new TextToTimeComponent()
		textEntity.add(t2t);

		textEntity.add(textPosition);
		textEntity.add(registerFont);
		textEntity.add(getFont);
		textEntity.add(textColor);
		textEntity.add(textTablePositionComponent);
		textEntity.add(textTableComponent);
		textEntity.add(textComponent);
		textEntity.add(textSizeComponent);
		textEntity.add(textTableSpanComponent);

		Entity background = new Entity();

		background.add(new PositionComponent(x: 0, y: 0, z: -1));
		background.add(new RegisterTextureAssetComponent(fileName: "os/assets/background.jpg"));
		background.add(new GetTextureAssetComponent(fileName: "os/assets/background.jpg"));
		background.add(new SizeComponent());
		background.add(new TablePositionComponent(x: 0, y:0));
		background.add(new TableComponent(table: table));
		background.add(new TableSpanComponent(width: 20, height: 19));

		engine.addEntity(background);
		engine.addEntity(topbar);
		engine.addEntity(menuButton);
		engine.addEntity(table);
		engine.addEntity(textEntity);
		
		Entity shortcutTable = new Entity();
		shortcutTable.add(new TableSizeComponent(cellWidth: 75, cellHeight: 75));
		shortcutTable.add(new PositionComponent(x: 25, y: 25));
		engine.addEntity(shortcutTable);


		Entity terminalShortcut = new Entity();

		terminalShortcut.add(new PositionComponent(z: 0));
		terminalShortcut.add(new RegisterTextureAssetComponent(fileName: "os/assets/icons/terminal.png"));
		terminalShortcut.add(new GetTextureAssetComponent(fileName: "os/assets/icons/terminal.png"));
		terminalShortcut.add(new SizeComponent(width: 100, height: 100));
		terminalShortcut.add(new HitBoxComponent(rectangle: new Rectangle(0,0,75,75)));
		terminalShortcut.add(new TablePositionComponent(x: 0, y: 12));
		terminalShortcut.add(new TableComponent(table: shortcutTable));
		terminalShortcut.add(new DragableComponent());
		terminalShortcut.add(new ClickableComponent());
		terminalShortcut.add(new ProgramStartOnMouseClickComponent(name: "Hello World!", path:"helloworld"));

		engine.addEntity(terminalShortcut);

		engine.addSystem(new RenderSystem());
		engine.addSystem(new TableSystem());
		engine.addSystem(new AssetLoadSystem());
		engine.addSystem(new AssetGetSystem());
		engine.addSystem(new AssetRegisterSystem());
		engine.addSystem(new TextureAtlasGetSystem());
		engine.addSystem(new TextToTimeSystem());
		engine.addSystem(new MouseClickSystem());
		engine.addSystem(new DragSystem());
		engine.addSystem(new ProgramStartOnMouseClickSystem());
		engine.addSystem(new ProgramStartSystem());
		engine.addSystem(new ProgramManagerSystem());
		engine.addSystem(new FileLoadSystem());
		engine.addSystem(new ViewportSystem());

	}
}