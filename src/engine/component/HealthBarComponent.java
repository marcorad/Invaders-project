package engine.component;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.system.Vector2f;

import engine.entity.Entity;
import engine.graphics.GraphicsHandler;
import engine.gui.Bar;
import util.Util;

/**A health bar that appears above the entity, indicating the amount of health it has.
 * @author Marco
 *
 */
public class HealthBarComponent extends DisplayComponent {

	private static float width = 1.9f, height = 0.2f;
	private RectangleShape frame;
	private RectangleShape bar;
	
	private Bar displaybar;
	
	/**
	 * @param entity The active entity
	 */
	public HealthBarComponent(Entity entity) {
		super(entity);
		displaybar = new Bar(width, height, new Color(255, 0, 0, 200), new Color(0, 255, 0, 200));
		frame = new RectangleShape(new Vector2f(width,height));
		frame.setOrigin(new Vector2f(width/2f, -1.1f - height/2));		
		frame.setFillColor(Color.TRANSPARENT);
		frame.setOutlineColor(new Color(0,0,0,190));
		frame.setOutlineThickness(.05f);
		
		bar = new RectangleShape(new Vector2f(width,height));
		bar.setOrigin(new Vector2f(width/2f, -1.1f - height/2));		
		bar.setFillColor(Color.GREEN);
		
		onPositionUpdate();
		onScaleUpdate();
	}

	@Override
	public void draw(GraphicsHandler graphics) {
		displaybar.draw(entity.getHealth(), entity.getMaxHealth());
	}

	@Override
	public void onPositionUpdate() {
		displaybar.setPosition(Vector2f.add(entity.getPosition(), new Vector2f(0f, entity.getScale().y + 0.03f)));
	}

	@Override
	public void onRotationUpdate() {
	}

	@Override
	public void onScaleUpdate() {
		displaybar.setScale(entity.getScale());
	}

}
