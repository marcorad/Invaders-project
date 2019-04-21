package engine.component;

import engine.entity.Entity;

/**
 * Does an action once a collision has occurred.
 */
public abstract class OnCollisionComponent extends NotifierComponent {

	/**
	 * @param entity The active entity
	 */
	protected OnCollisionComponent(Entity entity) {
		super(entity);
	}

	@Override
	public boolean notifyCondition() {
		return entity.getCollidingEntities().size() != 0;
	}
	

}
