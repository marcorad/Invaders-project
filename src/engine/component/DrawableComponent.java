package engine.component;

import engine.graphics.GraphicsHandler;

/**
 * Specifies a component that can be drawn
 * @author Marco
 *
 */
public interface DrawableComponent {
	/**Draws the component
	 * @param graphics The graphics which to draw to
	 */
	public void draw(GraphicsHandler graphics);
}
