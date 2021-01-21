package os.systems;

import groovy.transform.CompileStatic;

import os.components.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.viewport.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.*;
import com.badlogic.ashley.utils.*;

@CompileStatic
public class MouseClickSystem extends SortedIteratingSystem {
	
    private final ComponentMapper<ClickableComponent> clickableMapper;
    private final ComponentMapper<HitBoxComponent> hitboxMapper;
    private final ComponentMapper<PositionComponent> positionMapper;
    private final ComponentMapper<ViewportComponent> viewportMapper;

    private final ComponentMapper<MouseClickEventComponent> clickMapper;
    private final ComponentMapper<MousePressEventComponent> pressMapper;
    private final ComponentMapper<MouseReleaseEventComponent> releaseMapper;

    private ImmutableArray<Entity> renderEntity, oldEvents;

    private Vector2 mouseCoords;
    private boolean pressed;
    
    private InputProcessor inputProcessor; 
    
    int mx, my, button;
    boolean down;
    
    public MouseClickSystem() {
        super(Family.all(PositionComponent.class, HitBoxComponent.class, ClickableComponent.class).get(), new ZComparator());
        this.inputProcessor = new InputProcessor() {
            @Override
            public boolean keyDown(int keyCode) {return false;}
            public boolean keyUp(int keyCode) {return false;}
            public boolean keyTyped(char key) {return false;}
            public boolean mouseMoved(int x, int y) {
                mx = x;
                my = y;
                return false;
            }
            public boolean touchDown(int x, int y, int pointer, int button) {
                mx = x;
                my = y;
                down = true;
                MouseClickSystem.this.button = button;
                return false;
            }
            public boolean touchUp(int x, int y, int pointer, int button) {
                mx = x;
                my = y;
                down = false;
                MouseClickSystem.this.button = button;
                return false;
            }
            public boolean scrolled(int scroll){return false;}
            public boolean touchDragged(int dx, int dy, int button){return false;}
        };
        ((InputMultiplexer)(Gdx.input.getInputProcessor())).addProcessor(this.inputProcessor);
        this.clickableMapper = ComponentMapper.getFor(ClickableComponent.class);
        this.hitboxMapper = ComponentMapper.getFor(HitBoxComponent.class);
        this.positionMapper = ComponentMapper.getFor(PositionComponent.class);
        this.viewportMapper = ComponentMapper.getFor(ViewportComponent.class);

        this.clickMapper = ComponentMapper.getFor(MouseClickEventComponent.class);
        this.pressMapper = ComponentMapper.getFor(MousePressEventComponent.class);
        this.releaseMapper = ComponentMapper.getFor(MouseReleaseEventComponent.class);
    }
	
    public void addedToEngine(Engine engine) {
        this.oldEvents = engine.getEntitiesFor(
            Family.one(
                MouseClickEventComponent.class, 
                MousePressEventComponent.class, 
                MouseReleaseEventComponent.class
            ).get()
        );
        this.renderEntity = engine.getEntitiesFor(
            Family.all(ViewportComponent.class, SpriteBatchComponent.class).get()
        );
        super.addedToEngine(engine);
    }

    public void update(float delta) {
        
        for(Entity entity : oldEvents) {
            MouseClickEventComponent click = clickMapper.get(entity);
            MousePressEventComponent press = pressMapper.get(entity);
            MouseReleaseEventComponent release = releaseMapper.get(entity);

            //entity.remove(MouseClickEventComponent.class);

            if(press != null) {
                press.timer -= delta;
                if(press.timer < 0) {
                    press.count -= 1;
                    press.timer += 0.25f;
                }
                if(press.count <= 0) entity.remove(MousePressEventComponent.class);
            }
            if(release != null) {
                release.timer -= delta;
                if(release.timer < 0) {
                    release.timer += 0.25f;
                    release.count -= 1;
                }
                if(release.count <= 0) entity.remove(MouseReleaseEventComponent.class);
            }

        }
	
        if(renderEntity.size() < 1) return;
        ViewportComponent viewportComponent = viewportMapper.get(renderEntity.first());
		
        this.mouseCoords = getViewportCoords(viewportComponent.viewport, mx, my);
        forceSort()
        pressed = false;
        for(Entity entity : entities) {
            if(pressed == false)
            processEntity(entity, delta);
        }
    }

    public void processEntity(Entity entity, float delta) {

        PositionComponent position = positionMapper.get(entity);
        HitBoxComponent hitBox = hitboxMapper.get(entity);

        MouseClickEventComponent click = clickMapper.get(entity);
        MousePressEventComponent press = pressMapper.get(entity);
        MouseReleaseEventComponent release = releaseMapper.get(entity);

        Vector2 temp = new Vector2(hitBox.rectangle.getX(), hitBox.rectangle.getY());

        hitBox.rectangle.setPosition(temp.x + position.x, temp.y + position.y);
		
        if(hitBox.rectangle.contains(this.mouseCoords) && Gdx.input.justTouched()) {
            if(press == null) {
                press = new MousePressEventComponent();
                entity.add(press);
            }

            press.x = this.mouseCoords.x;
            press.y = this.mouseCoords.y;
            press.button = this.button;
            press.count += 1;
            press.timer = 0.25f;
            pressed = true;
        }
		
        if(hitBox.rectangle.contains(this.mouseCoords) && !Gdx.input.isTouched() && press != null) {
            if(press.timer > 0) {
                if(release == null) {
                    release = new MouseReleaseEventComponent();
                    entity.add(release);
                }

                release.count += 1;
                release.x = this.mouseCoords.x;
                release.y = this.mouseCoords.y;
                release.button = this.button;
                release.timer = 0.25f;
                pressed = true;
            }
        }

        if(press != null && release != null) {
            if(click == null) {
                click = new MouseClickEventComponent();
                entity.add(click);
            }

            click.count = Math.min(press.count, release.count);
            click.button = this.button;
            click.x = this.mouseCoords.x;
            click.y = this.mouseCoords.y;
        }

        hitBox.rectangle.setPosition(temp);
    }

    public Vector2 getViewportCoords(Viewport viewport, float mx, float my) {
        return viewport.unproject(new Vector2(mx, my));
    }

    private static class ZComparator implements Comparator<Entity> {
        private final ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
		
        @Override
        public int compare(Entity e1, Entity e2) {
            return (int)Math.signum( -pm.get(e1).z + pm.get(e2).z);
        }
    }
}