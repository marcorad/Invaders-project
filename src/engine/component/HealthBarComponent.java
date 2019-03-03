package engine.component;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.system.Vector2f;

import engine.entity.Entity;
import engine.graphics.GraphicsHandler;
import util.Util;

/**A health bar that appears above the entity, indicating the amount of health it has.
 * @author Marco
 *
 */
public class HealthBarComponent extends DisplayComponent {

	private float width = 1.9f, height = 0.2f;
	private RectangleShape frame;
	private RectangleShape bar;
	
	/**
	 * @param entity The active entity
	 */
	public HealthBarComponent(Entity entity) {
		super(entity);
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
		float factor = (entity.getHealth()/entity.getMaxHealth());
		Vector2f size = new Vector2f(width *  factor, height);		
		bar.setSize(size);
		bar.setFillColor(Util.lerpColor(factor, new Color(255,0,0,190), new Color(0,255,0,190)));
	    graphics.drawToRenderTexture(bar);
		graphics.drawToRenderTexture(frame);
	}

	@Override
	public void onPositionUpdate() {
		bar.setPosition(entity.getPosition());		
		frame.setPosition(entity.getPosition());
	}

	@Override
	public void onRotationUpdate() {
	}

	@Override
	public void onScaleUpdate() {
		frame.setScale(entity.getScale());
		bar.setScale(entity.getScale());
	}

}
