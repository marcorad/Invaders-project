package engine.component;

/**
 * Specifies a component that is notified once the position, scale or rotation of an entity is updated.
 * @author Marco
 *
 */
public interface MovementNotifier {
	public void onPositionUpdate();
	public void onRotationUpdate();
	public void onScaleUpdate();
}
