package os.systems;

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
		viewportComponent.viewport = new StretchViewport(800, 800);
		assetManagerEntity.add(viewportComponent);
		
		SpriteBatchComponent sbComponent = new SpriteBatchComponent();
		sbComponent.batch = new SpriteBatch();
		assetManagerEntity.add(sbComponent);

		engine.addEntity(assetManagerEntity);

		Entity table = new Entity();
		
		PositionComponent tablePosition = new PositionComponent();
		tablePosition.x = 0
		tablePosition.y = 0;
		tablePosition.z = 0;

		System.out.println(Gdx.graphics.getHeight() / 20);
		System.out.println(viewportComponent.viewport.getWorldHeight() / 20);

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

		ColorComponent topbarColor = new ColorComponent(color: Color.BLUE);

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
		ColorComponent menuButtonColor = new ColorComponent(color: Color.BLUE);

		menuButton.add(menuPosition);
		menuButton.add(menuTableComponent);
		menuButton.add(menuTablePositionComponent);
		menuButton.add(menuButtonSizeComponent);
		menuButton.add(getMenuIcon);
		menuButton.add(menuButtonColor);

		engine.addEntity(topbar);
		engine.addEntity(menuButton);
		engine.addEntity(table);
		
		engine.addSystem(new RenderSystem());
		engine.addSystem(new TableSystem());
		engine.addSystem(new AssetLoadSystem());
		engine.addSystem(new AssetGetSystem());
		engine.addSystem(new AssetRegisterSystem());
		engine.addSystem(new TextureAtlasGetSystem());

		engine.removeSystem(this);
	}
}