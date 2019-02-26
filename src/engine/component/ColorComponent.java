package engine.component;



import org.jsfml.graphics.Color;

import engine.entity.Entity;
import engine.graphics.GraphicsHandler;
import engine.input.EventHandler;

/**Specifies a component that has a colour associated with it.
 * @author Marco
 *
 */
public abstract class ColorComponent extends DisplayComponent {

	protected Color color = Color.BLACK;
	
	
	/**
	 * @param entity The active entity
	 * @param color The colour
	 */
	public ColorComponent(Entity entity, Color color) {
		super(entity);
		this.color = color;
	}

	/**
	 * What happens when the colour changes
	 */
	protected abstract void colorUpdate();

	/**
	 * @return The colour
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @param color The colour
	 */
	public void setColor(Color color) {
		this.color = color;
		colorUpdate();
	}
}
