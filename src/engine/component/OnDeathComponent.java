package engine.component;

import engine.entity.Entity;

/**
 * Does an action once the entity's health reaches 0.
 */
public abstract class OnDeathComponent extends NotifierComponent{

	/**
	 * @param entity The active entity
	 */
	protected OnDeathComponent(Entity entity) {
		super(entity);
	}

	@Override
	public final boolean notifyCondition() {
		return entity.getHealth() <= 0f;
	}
	
	
}
