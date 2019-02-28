package engine.component;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.system.Vector2f;

import engine.entity.Entity;
import engine.graphics.GraphicsHandler;

public class HealthBarComponent extends DisplayComponent {

	private float width = 1.9f, height = 0.2f;
	private RectangleShape frame;
	private RectangleShape bar;
	
	public HealthBarComponent(Entity entity) {
		super(entity);
		frame = new RectangleShape(new Vector2f(width,height));
		frame.setOrigin(new Vector2f(width/2f, -1f - height/2));		
		frame.setFillColor(Color.TRANSPARENT);
		frame.setOutlineColor(Color.BLACK);
		frame.setOutlineThickness(.05f);
		
		bar = new RectangleShape(new Vector2f(width,height));
		bar.setOrigin(new Vector2f(width/2f, -1f - height/2));		
		bar.setFillColor(Color.GREEN);
		
		onPositionUpdate();
		onScaleUpdate();
	}

	@Override
	public void draw(GraphicsHandler graphics) {
		Vector2f size = new Vector2f(width * (entity.getHealth()/entity.getMaxHealth()) , height);		
		bar.setSize(size);
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
