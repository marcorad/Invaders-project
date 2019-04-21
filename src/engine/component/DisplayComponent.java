package engine.component;

import org.jsfml.graphics.PrimitiveType;
import org.jsfml.graphics.Shape;

import engine.entity.Entity;
import engine.graphics.GraphicsHandler;
import util.Util;

/**
 * The base graphical component. This component can be drawn, and is notified when an entity moves so that any graphical elements
 * (in child classes) may implement their own custom update methods.
 */
public abstract class DisplayComponent extends Component implements DrawableComponent, EntityMovementListener {

	/**A helper method that can draw shapes of different display types.
	 * @param graphics The graphics handler object
	 * @param s The shape to be drawn
	 * @param type The type of drawing to be done
	 */
	protected static void drawShape(GraphicsHandler graphics, Shape s, DisplayType type){
		switch(type){
		
		case FILL: //purposeful fall-through			
		case WIREFRAME:
			graphics.drawToRenderTexture(s);
			break;
			
		case POINTS:
			graphics.drawToRenderTexture(Util.getTransformedVertices(s), PrimitiveType.POINTS);
			break;
			
		default:
			break;	
			
		}
	}	

	/**
	 * @param entity The active entity
	 */
	public DisplayComponent(Entity entity) {
		super(entity);		
	}
}
