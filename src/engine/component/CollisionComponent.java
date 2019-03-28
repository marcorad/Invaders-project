package engine.component;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.ConvexShape;
import org.jsfml.system.Vector2f;

import engine.entity.Entity;
import engine.entity.EntityManager;
import engine.graphics.GraphicsHandler;
import util.Util;

/**Specifies a component that collides
 * @author Marco
 *
 */
public class CollisionComponent extends Component{

	private ConvexPolygonComponent hitbox;
	private CollisionID ID;
	private CollisionID[] collidingIDs;


	/**Creates a collision component with a convex poly hitbox, having a specific id and colliding with other collision components.
	 * @param entity The active entity
	 * @param pts Points in the hitbox
	 * @param id The ID associated with this collision component
	 * @param collidingids The ID's this component can collide with
	 */
	public CollisionComponent(Entity entity, Vector2f[] pts, CollisionID id, CollisionID... collidingids) {
		super(entity);	
		hitbox = new ConvexPolygonComponent(entity, pts);				
		ID = id;
		if(collidingids == null){ //protects against Marco being clumsy
			collidingIDs = new CollisionID[]{};
		} else
			collidingIDs = collidingids;
		setHitboxDraw(EntityManager.DRAW_HITBOXES);
	}	


	/**Tests for a collision
	 * @param other The component to be collided with
	 * @return Whether they collide
	 */
	public boolean Collision(CollisionComponent other){
		return Util.convexPolyCollision(this.getHitbox(), other.getHitbox());
	}


	public ConvexShape getHitbox(){
		return (ConvexShape)hitbox.getShape();
	}

	/**
	 * @param b Whether hitbox should be drawn
	 */
	public void setHitboxDraw(boolean b){
		hitbox.setColor( b ? Color.RED : Color.TRANSPARENT);
	}
	
	
	public CollisionID getID() {
		return ID;
	}

	public void setID(CollisionID iD) {
		ID = iD;
	}

	public CollisionID[] getCollidingIDs() {
		return collidingIDs;
	}

	public void setCollidingIDs(CollisionID[] collidingIDs) {
		this.collidingIDs = collidingIDs;
	}
	
	
	
	





}
