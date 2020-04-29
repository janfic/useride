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

		LoadAssetsComponent loadAssetsComponent = new LoadAssetsComponent();
		assetManagerEntity.add(loadAssetsComponent);
		engine.addEntity(assetManagerEntity);

		Entity table = new Entity();
		
		PositionComponent tablePosition = new PositionComponent();
		tablePosition.x = 100
		tablePosition.y = 100;
		tablePosition.z = 100;

		TableSizeComponent tableSize = new TableSizeComponent();
		tableSize.cellWidth = 100;
		tableSize.cellHeight = 100;
		tableSize.width = 10;
		tableSize.height = 10;

		table.add(tablePosition);
		table.add(tableSize);

		Entity testEntity = new Entity();
		
		TableComponent tableComponnet = new TableComponent();
		tableComponnet.table = table;

		TablePositionComponent tablePos = new TablePositionComponent();
		tablePos.x = 2;
		tablePos.y = 2;

		GetTextureAssetComponent getTexture = new GetTextureAssetComponent();
		getTexture.fileName = "os/assets/badlogic.jpg";
		
		PositionComponent position = new PositionComponent();
		position.x = 300;
		position.y = 100;
		position.z = 2;
		
		SizeComponent size1 = new SizeComponent();
		
		testEntity.add(getTexture);
		testEntity.add(position);
		testEntity.add(size1);
		testEntity.add(tablePos);
		testEntity.add(tableComponnet);
		
		engine.addEntity(testEntity);
		engine.addEntity(table);
		
		Entity renderEntity = new Entity();
		ViewportComponent viewportComponent = new ViewportComponent();
		viewportComponent.viewport = new ScreenViewport();
		
		SpriteBatchComponent sbComponent = new SpriteBatchComponent();
		sbComponent.batch = new SpriteBatch();

		renderEntity.add(viewportComponent);
		renderEntity.add(sbComponent);
		
		engine.addEntity(renderEntity);
		

		engine.addSystem(new RenderSystem());
		engine.addSystem(new TableSystem());
		engine.addSystem(new AssetLoadSystem());
		engine.addSystem(new AssetGetSystem());
		engine.addSystem(new AssetRegisterSystem());
		engine.addSystem(new TableSystem());

		engine.removeSystem(this);
	}
}