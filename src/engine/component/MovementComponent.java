package engine.component;

import engine.entity.Entity;
import engine.graphics.GraphicsHandler;
import engine.input.EventHandler;

/**
 * Specifies a component that will involve movement
 * @author Marco
 *
 */
public abstract class MovementComponent extends Component implements UpdateableComponent{

	/**
	 * @param entity The active entity
	 */
	public MovementComponent(Entity entity) {
		super(entity);
	}
	
}
