package os.systems;

import os.components.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;

public class CommandsSystem extends EntitySystem {

	private final ComponentMapper<CommandsComponent> commandsMapper;

	private ImmutableArray<Entity> entities;

	public CommandsSystem() {
		this.commandsMapper = ComponentMapper.getFor(CommandsComponent.class);
	}

	public void addedToEngine(Engine engine) {
		this.entities = engine.getEntitiesFor(Family.all(CommandsComponent.class).get());
	}
	
	public void update(float delta) {
		for(Entity entity : entities) {
		
			CommandsComponent commands = commandsMapper.get(entity);
			
			Family trigger = commands.family;
				
				
			if( !trigger.matches(entity)) {
				continue;
			}
				
			if(commands.entity != null && commands.add != null) {
				for(Component c : commands.add ) {
					entity.add(c);
				}
			}
			
			if(commands.entity != null && commands.remove != null) {
				for(Class<? extends Component> cl : commands.remove ) {
					entity.remove(cl);
				}
			}
		}
	}
	
	public void removedFromEngine(Engine engine) {
	
	}

}