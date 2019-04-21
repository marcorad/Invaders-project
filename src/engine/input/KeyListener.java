package engine.input;

import org.jsfml.window.event.KeyEvent;

/**
 * Specifies methods that are called when keys are pressed 
 */
public interface KeyListener {
	/**Called when a key i spressed
	 * @param ke The key event
	 */
	public void onKeyPress(KeyEvent ke);
	/**Called when a key is released
	 * @param ke The key event
	 */
	public void onKeyRelease(KeyEvent ke);
	/**Whether this listener is useless and should be removed from the event handler
	 * @return Whether it should be removed
	 */
	public boolean isUseless();
}
