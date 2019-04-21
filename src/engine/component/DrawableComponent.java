package engine.component;

import engine.graphics.GraphicsHandler;

/**
 * A component that can be drawn
  */
public interface DrawableComponent {
	/**Draws the component
	 * @param graphics The graphics which to draw to
	 */
	public void draw(GraphicsHandler graphics);
}
