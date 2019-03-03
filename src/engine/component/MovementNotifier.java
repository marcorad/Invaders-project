package engine.component;

/**
 * Specifies a component that is notified once the position, scale or rotation of an entity is updated.
 * @author Marco
 *
 */
public interface MovementNotifier {
	/**
	 * Called when the entity updates position
	 */
	public void onPositionUpdate();
	/**
	 * Called when the entity updates rotation
	 */
	public void onRotationUpdate();
	/**
	 * Called when the entity updates scale
	 */
	public void onScaleUpdate();
}
