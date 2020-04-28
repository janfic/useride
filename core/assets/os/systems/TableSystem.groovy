package os.systems;

import os.components.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;
import com.badlogic.ashley.systems.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.viewport.*;
import com.badlogic.gdx.*;

public class TableSystem extends EntitySystem {
	
	private final ComponentMapper<TableComponent>			tableMapper;
	private final ComponentMapper<TableSizeComponent>		tableSizeMapper;
	private final ComponentMapper<TablePositionComponent>	tablePositionMapper;
	private final ComponentMapper<TableSpanComponent>		tableSpanMapper;
	private final ComponentMapper<TableAlignComponent>		tableAlignMapper;
	private final ComponentMapper<SizeComponent>			sizeMapper;
	private final ComponentMapper<PositionComponent>		positionMapper;
	
	private ImmutableArray<Entity> entities;
	
	private Viewport viewport;
	private Vector2 mouseViewportCoords;
	
	public TableSystem() {
		this.positionMapper = ComponentMapper.getFor(PositionComponent.class);
		this.tableMapper = ComponentMapper.getFor(TableComponent.class);
		this.tableSizeMapper = ComponentMapper.getFor(TableSizeComponent.class);
		this.tablePositionMapper = ComponentMapper.getFor(TablePositionComponent.class);
		this.tableSpanMapper = ComponentMapper.getFor(TableSpanComponent.class);
		this.tableAlignMapper = ComponentMapper.getFor(TableAlignComponent.class);
		this.sizeMapper = ComponentMapper.getFor(SizeComponent.class);
	}
	
	public void addedToEngine(Engine engine) {
		this.entities = engine.getEntitiesFor(
			Family.all(
				PositionComponent.class,
				TableComponent.class,
				TablePositionComponent.class,
				SizeComponent.class
			).get()
		);
	}
	
	public void update(float delta) {
		for(Entity entity : entities) {
			PositionComponent positionMapper.get(entity);
			TableComponent tableComponent = tableMapper.get(entity);
			TablePositionComponent tablePosition = tablePositionMapper.get(entity);
			SizeComponent size = sizeMapper.get(entity);

			if(tableComponent.table != null) {
				TableSizeComponent tableSize = tableSizeMapper.get(tableComponent.table);
				PositionComponent tableEntityPosition = positionMapper.get(tableComponent.table);
				if(tableEntityPosition == null) continue; 

				position.x = tableEntityPosition.x + tableSize.width * tablePosition.x;
				position.y = tableEntityPosition.y + tableSize.height * tablePosition.y;

				size.width = tableSize.width;
				size.height = tableSize.height;

			}
		}
	}
}