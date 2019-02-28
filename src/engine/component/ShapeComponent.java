package engine.component;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.PrimitiveType;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.Shape;
import org.jsfml.graphics.Vertex;
import org.jsfml.system.Vector2f;

import engine.entity.Entity;
import engine.graphics.GraphicsHandler;
import engine.input.EventHandler;
import util.Oscillator;
import util.Util;

/**A component that will display a shape
 * @author Marco
 *
 */
public abstract class ShapeComponent extends ColorComponent {

	protected DisplayType type; //defaults to fill
	protected Shape shape;	

	/**
	 * @param entity The active entity
	 * @param color The colour
	 * @param type The display type
	 */
	public ShapeComponent(Entity entity, Color color, DisplayType type) {
		super(entity, color);
		this.type = type;
		createShape();
		colorUpdate();
		onScaleUpdate();
		onRotationUpdate();
		onPositionUpdate();
	}
	
	@Override
	protected void colorUpdate(){
		switch(type){
		case FILL:
			shape.setFillColor(color);
			shape.setOutlineColor(Color.TRANSPARENT);
			break;
		case POINTS:
			shape.setFillColor(color);
			break;
		case WIREFRAME:
			shape.setOutlineThickness(.05f);
			shape.setOutlineColor(color);
			shape.setFillColor(Color.TRANSPARENT);
			break;
		default:
			break;}
	}
	
	/**
	 * User implementation that will create the shape
	 */
	protected abstract void createShape();


	@Override
	public void draw(GraphicsHandler graphics) {
		//graphics.drawToRenderTexture(verticesFromFloatRect(Util.getBoundingRect(shape)), PrimitiveType.LINE_STRIP);
		super.drawShape(graphics, shape, type);
	}

	/**Debug method to get the drawable bounds of the shape
	 * @return
	 */
	public RectangleShape drawableGlobalBounds(){
		RectangleShape rs = new RectangleShape();
		FloatRect fr = Util.getBoundingRect(shape);				
		rs.setSize(new Vector2f(fr.width, fr.height));
		rs.setFillColor(Color.BLUE);
		return rs;
	}

	/**
	 * @return The shape
	 */
	public Shape getShape(){
		return shape;
	}
	
	/**
	 * @return The display type
	 */
	public DisplayType getType() {
		return type;		
	}
	
	@Override
	public void onPositionUpdate() {
		shape.setPosition(entity.getPosition());		
	}

	@Override
	public void onRotationUpdate() {
		shape.setRotation(entity.getTheta());		
	}


	@Override
	public void onScaleUpdate() {
		shape.setScale(entity.getScale());		
	}

	/**
	 * @param type The display type
	 */
	public void setType(DisplayType type) {
		this.type = type;
		colorUpdate();
	}


	/**Debug method to get the vertices of a FloatRect
	 * @param f The rectangle
	 * @return The vertices
	 */
	public Vertex[] verticesFromFloatRect(FloatRect f){
		Vertex v[] = new Vertex[5];
		v[0] = new Vertex(new Vector2f(f.left, f.top), Color.RED);
		v[1] = new Vertex(new Vector2f(f.left + f.width, f.top), Color.RED);
		v[2] = new Vertex(new Vector2f(f.left + f.width, f.top - f.height), Color.RED);
		v[3] = new Vertex(new Vector2f(f.left, f.top - f.height), Color.RED);
		v[4] = new Vertex(new Vector2f(f.left, f.top), Color.RED);
		return v;
	}



}
