package os.systems;

import os.components.*;
import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.*;

import java.time.*;

public class TextToTimeSystem extends EntitySystem {
	
	private final ComponentMapper<TextComponent> textMapper;;

	private ImmutableArray<Entity> entities;

	private LocalDateTime dateTime;

	public TextToTimeSystem() {
		this.textMapper = ComponentMapper.getFor(TextComponent.class);
		this.dateTime = LocalDateTime.now();
	}
	
	public void addedToEngine(Engine engine) {
		this.entities = engine.getEntitiesFor(
			Family.one(TextComponent.class, TextToTimeComponent.class).get()
		);
	}

	public void update(float delta) {
		this.dateTime = LocalDateTime.now();
		String time = dateTime.getHour() + ":" + dateTime.getMinute() + ":" + dateTime.getSecond() + " " + ( dateTime.getHour() >= 12 ? "PM" : "AM");

		for(Entity entity : entities) {
			TextComponent text = textMapper.get(entity);
			text.text = time;
		}
	}
}