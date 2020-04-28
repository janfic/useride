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
import com.badlogic.gdx.*;
import os.components.*;
import os.systems.*;


public class BootSystem extends EntitySystem {	
	public void addedToEngine(Engine engine) {
		
		Entity testEntity = new Entity();
		
		TextureComponent texture = new TextureComponent();
		texture.texture = new Texture("os/assets/badlogic.jpg");
		
		PositionComponent position = new PositionComponent();
		position.x = 300;
		position.y = 100;
		position.z = 2;
		
		SizeComponent size1 = new SizeComponent();
		size1.width = texture.texture.getWidth();
		size1.height = texture.texture.getHeight();
		
		testEntity.add(texture);
		testEntity.add(position);
		testEntity.add(size1);
		
		engine.addEntity(testEntity);
		
		Entity renderEntity = new Entity();
		ViewportComponent viewportComponent = new ViewportComponent();
		viewportComponent.viewport = new ScreenViewport();
		
		SpriteBatchComponent sbComponent = new SpriteBatchComponent();
		sbComponent.batch = new SpriteBatch();

		renderEntity.add(viewportComponent);
		renderEntity.add(sbComponent);
		
		engine.addEntity(renderEntity);
		

		engine.addSystem(new RenderSystem());
		engine.addSystem(new MouseClickSystem());
		engine.addSystem(new MouseHoverSystem());
		engine.addSystem(new TableSystem());
		engine.removeSystem(this);
	}
}