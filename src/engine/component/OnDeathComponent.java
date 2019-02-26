package engine.component;

import engine.entity.Entity;

/**Specifies a component that does an action upon entity death
 * @author Marco
 *
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
