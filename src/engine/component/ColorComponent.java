package engine.component;



import org.jsfml.graphics.Color;

import engine.entity.Entity;

/**A component that has a colour associated with it.
  */
public abstract class ColorComponent extends DisplayComponent {

	protected Color color;
	
	
	/**
	 * @param entity The active entity
	 * @param color The colour
	 */
	public ColorComponent(Entity entity, Color color) {
		super(entity);
		this.color = color;
	}

	/**
	 * Called when the colour changes/updates, to implemented by child classes
	 */
	protected abstract void colorUpdate();

	/**Get the colour
	 * @return The colour
	 */
	public Color getColor() {
		return color;
	}

	/**Set the colour
	 * @param color The colour
	 */
	public void setColor(Color color) {
		this.color = color;
		colorUpdate();
	}
}
