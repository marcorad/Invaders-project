package engine.component;

import engine.entity.Entity;

/**Specifies a component that does an action on a collision.
 * @author Marco
 *
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
