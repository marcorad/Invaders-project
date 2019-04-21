package engine.component;

import engine.entity.Entity;

/**
 * Base component that will involve movement
 */
public abstract class MovementComponent extends Component implements UpdateableComponent{

	/**
	 * @param entity The active entity
	 */
	public MovementComponent(Entity entity) {
		super(entity);
	}
	
}
