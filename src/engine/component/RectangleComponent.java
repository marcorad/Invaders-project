package engine.component;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.system.Vector2f;

import engine.entity.Entity;

/**
 * Will draw a rectangle on screen. A class used for initial testing. This is a rudimentary class so there is no need for proper documentation.
 */
public class RectangleComponent extends ShapeComponent {

	protected Vector2f size = Vector2f.ZERO;

	
	public RectangleComponent(Entity entity) {
		super(entity, Color.CYAN, DisplayType.FILL);
		setSize(size);
	}

	public RectangleComponent(Entity entity, Color color,
			Vector2f size) {
		super(entity, color, DisplayType.FILL);
		setSize(size);
		setColor(color);		
	}

	public RectangleComponent(Entity entity, Color color,
			Vector2f size, DisplayType type) {
		super(entity, color, type);
		setSize(size);		
	}


	@Override
	protected void createShape() {
		shape = new RectangleShape();
		shape.setPosition(entity.getPosition());		
	}

	

	public Vector2f getSize() {
		return size;
	}


	public void setSize(Vector2f size) {
		this.size = size;
		((RectangleShape)shape).setSize(size);
		shape.setOrigin(Vector2f.mul(size, .5f));
	}




}
