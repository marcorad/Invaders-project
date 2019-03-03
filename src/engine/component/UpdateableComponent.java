package engine.component;

/**
 * Specifies a component that can be updated
 * @author Marco
 *
 */
public interface UpdateableComponent {
  /**Update according to a total elapsed time and a change in time
 * @param dt The change in time since the previous frame
 * @param t The total elapsed time of the game
 */
public void update(float dt, float t);
}
