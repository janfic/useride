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
        viewportComponent.viewport.setWorldSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        assetManagerEntity.add(viewportComponent);

        SizeComponent viewportSize = new SizeComponent(width: Gdx.graphics.getWidth(), height:  Gdx.graphics.getHeight());
        assetManagerEntity.add(viewportSize);

        PositionComponent viewportPosition = new PositionComponent();
        assetManagerEntity.add(viewportPosition);

        SpriteBatchComponent sbComponent = new SpriteBatchComponent();
        sbComponent.batch = new SpriteBatch();
        assetManagerEntity.add(sbComponent);

        this.engine.addEntity(assetManagerEntity);

        Entity table = new Entity();
		
        PositionComponent tablePosition = new PositionComponent();

        SizeComponent tableSize = new SizeComponent();
        tableSize.width = viewportComponent.viewport.getWorldWidth() / 20;
        tableSize.height = viewportComponent.viewport.getWorldHeight() / 20;

        table.add(tablePosition);
        table.add(tableSize);

        Entity topbar = new Entity();
                
        topbar.add(new ParentComponent(parent: table));
        topbar.add(new RelativePositionComponent(y: 1900, unit: "%"));
        topbar.add(new RelativeSizeComponent(width: 2000, height: 100, unit: "%"));
        topbar.add(new PositionComponent());
        topbar.add(new GetNinePatchComponent(name: "box"));
        topbar.add(new SizeComponent());

        Entity menuButton = new Entity();

        menuButton.add(new PositionComponent(x: 100, y:100, z:2));
        menuButton.add(new RelativePositionComponent(x: 5, y: 5));
        menuButton.add(new ParentComponent(parent: topbar));
        menuButton.add(new SizeComponent(width: 45, height: 45));
        menuButton.add(new GetTextureRegionComponent(name: 'menu'));
        menuButton.add(new ColorComponent(color: Color.GRAY));

        Entity textEntity = new Entity();
		
        textEntity.add(new TextToTimeComponent());
        textEntity.add(new PositionComponent(x: 100, y: 200, z:2));
        textEntity.add(new RegisterBitmapFontAssetComponent(fileName: "os/assets/userosgui/Lucida Console.fnt"));
        textEntity.add(new GetBitmapFontAssetComponent(fileName: "os/assets/userosgui/Lucida Console.fnt"));
        textEntity.add(new ColorComponent(color: Color.GRAY));
        textEntity.add(new RelativePositionComponent(x: 90, y: 50, unit: "%"));
        textEntity.add(new RelativeSizeComponent(width: 10, height: 100, unit: "%"));
        textEntity.add(new ParentComponent(parent: topbar));
        textEntity.add(new TextComponent(text: "time here"));
        textEntity.add(new SizeComponent(width: 200, height: 50));

        Entity background = new Entity();

        background.add(new PositionComponent(x: 0, y: 0, z: -1));
        background.add(new RegisterTextureAssetComponent(fileName: "os/assets/background.jpg"));
        background.add(new GetTextureAssetComponent(fileName: "os/assets/background.jpg"));
        background.add(new SizeComponent());
        background.add(new RelativeSizeComponent(width: 2000, height: 2000, unit: "%"));
        background.add(new ParentComponent(parent: table));

        engine.addEntity(background);
        engine.addEntity(topbar);
        engine.addEntity(menuButton);
        engine.addEntity(table);
        engine.addEntity(textEntity);

        engine.addSystem(new RenderSystem());
        engine.addSystem(new DragSystem());
        engine.addSystem(new RelativeSizeSystem());
        engine.addSystem(new RelativePositionSystem());
        engine.addSystem(new AssetLoadSystem());
        engine.addSystem(new AssetGetSystem());
        engine.addSystem(new AssetRegisterSystem());
        engine.addSystem(new TextureAtlasGetSystem());
        engine.addSystem(new TextToTimeSystem());
        engine.addSystem(new MouseClickSystem());
        engine.addSystem(new MouseHoverSystem());
        engine.addSystem(new ProgramStartOnMouseClickSystem());
        engine.addSystem(new ProgramStartSystem());
        engine.addSystem(new ProgramManagerSystem());
        engine.addSystem(new ChangeColorOnMouseHoverSystem());
        engine.addSystem(new FileLoadSystem());
        engine.addSystem(new ViewportSystem());
        engine.addSystem(new FocusSystem());
        engine.addSystem(new ProgramShortcutSystem());
    }
}