package engine.component;

import org.jsfml.graphics.PrimitiveType;
import org.jsfml.graphics.Shape;
import org.jsfml.graphics.Vertex;

import engine.entity.Entity;
import engine.graphics.GraphicsHandler;
import engine.input.EventHandler;
import util.Util;

/**
 * Specifies a graphical component
 * @author Marco
 *
 */
public abstract class DisplayComponent extends Component implements DrawableComponent, MovementNotifier {

	/**A helper method that can draw shapes of different display types
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
