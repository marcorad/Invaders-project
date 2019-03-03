package engine.input;

import org.jsfml.window.event.*;

/**Specifies methods that get called when there is mouse input
 * @author Marco
 *
 */
public interface MouseListener {
	
	/**The method called when a mouse button is pressed
	 * @param mbe The mouse button event
	 */
	public void onMousePress(MouseButtonEvent mbe);
	/**The method called when a mouse button is released 
	 * @param mbe The mouse button event
	 */
	public void onMouseRelease(MouseButtonEvent mbe);
	/**The method called when the mouse wheel is moved
	 * @param mwe The mouse wheel event
	 */
	public void onMouseWheelMoved(MouseWheelEvent mwe);
	/**The method called when the mouse is moved
	 * @param me The mouse event
	 */
	public void onMouseMoved(MouseEvent me);
	/**Whether this listener should be removed from the event handler
	 * @return Whether it should be removed
	 */
	public boolean isUseless();

}
