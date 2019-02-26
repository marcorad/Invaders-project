package engine.component;

import engine.entity.Entity;
import engine.input.MouseListener;

/**
 * Specifies a component that will listen to mouse input.
 * @author Marco
 *
 */
public abstract class MouseMoveControlComponent extends Component implements MouseListener {
	
	/**
	 * @param entity The active entity
	 */
	protected MouseMoveControlComponent(Entity entity) {
		super(entity);
	}

	@Override
	public boolean isUseless() {
		return entity.shouldBeRemoved();
	} 
}
