package engine.component;

import org.jsfml.system.Vector2f;

import engine.entity.Entity;

/** 
 * Removes the entity once it is considered off-screen.
 */
public class OffscreenRemoveComponent extends NotifierComponent {
	
	public static float MAX_X_DISTANCE = 1.4f, MAX_Y_DISTANCE = 1.4f;

	/**
	 * @param entity The active entity.
	 */
	public OffscreenRemoveComponent(Entity entity) {
		super(entity);
	}

	@Override
	public boolean notifyCondition() {
		Vector2f pos = entity.getPosition();
		return pos.x > MAX_X_DISTANCE || pos.x < -MAX_X_DISTANCE || pos.y > MAX_Y_DISTANCE || pos.y < -MAX_Y_DISTANCE;
	}

	@Override
	public void notifyAction() {
		entity.remove();
	}

}
