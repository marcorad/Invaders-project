package engine.component;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.ConvexShape;
import org.jsfml.system.Vector2f;

import engine.entity.Entity;

/**A convex polygon that can be drawn to the screen.
 */
public class ConvexPolygonComponent extends ShapeComponent {
	

	/**Convenience constructor that defaults to a red wireframe. Used mostly for debugging.
	 * @param entity The active entity
	 * @param pts Points in the poly
	 */
	public ConvexPolygonComponent(Entity entity, Vector2f pts[]) {		
		super(entity, Color.RED, DisplayType.WIREFRAME);
		((ConvexShape)shape).setPoints(pts);
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * @param entity The active entity
	 * @param pts Points in the poly
	 * @param color Color of the poly
	 * @param type Type of display
	 */
	public ConvexPolygonComponent(Entity entity, Vector2f pts[], Color color, DisplayType type) {		
		super(entity, color, type);
		if(pts != null) {
			((ConvexShape)shape).setPoints(pts);
		}
	}
	
	@Override
	protected void createShape() {
		shape = new ConvexShape();
		shape.setPosition(entity.getPosition());
	}
	
	/**Set the points of the polygon
	 * @param pts The points of the poly
	 */
	public void setPoints(Vector2f pts[]){
		if (pts[0] != null) {
			((ConvexShape)shape).setPoints(pts);
		}
	}

}
