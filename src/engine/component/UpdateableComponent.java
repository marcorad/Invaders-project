package engine.component;

/**
 * A component that may update w.r.t. time. Should only be implemented by a Component object, since Entity assumes this.
 */
public interface UpdateableComponent {
  /**Update according to a total elapsed time and a change in time
 * @param dt The change in time since the previous frame
 * @param t The total elapsed time of the game
 */
public void update(float dt, float t);
}
