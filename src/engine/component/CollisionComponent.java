package engine.component;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.ConvexShape;
import org.jsfml.system.Vector2f;

import engine.entity.Entity;
import engine.entity.EntityManager;
import util.Util;

/**A component that collides with other components of its type.
 *
 *
 */
public class CollisionComponent extends Component{

	private ConvexPolygonComponent hitbox;
	private CollisionID ID;
	private CollisionID[] collidingIDs;


	/**Creates a collision component with a convex polygon hitbox, having a specific id and 
	 * colliding with other collision components of specified IDs.
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
		} else {
			collidingIDs = collidingids;
		}
		setHitboxDraw(EntityManager.DRAW_HITBOXES);
	}	


	/**Tests for a collision with another component, regardless of ID.
	 * @param other The component to be collided with
	 * @return Whether they collide
	 */
	public boolean Collision(CollisionComponent other){
		return Util.convexPolyCollision(this.getHitbox(), other.getHitbox());
	}


	/**Get the hitbox of this component.
	 * @return The hitbox
	 */
	public ConvexShape getHitbox(){
		return (ConvexShape)hitbox.getShape();
	}

	/**Draw the hitbox.
	 * @param b Whether hitbox should be drawn
	 */
	public void setHitboxDraw(boolean b){
		hitbox.setColor( b ? Color.RED : Color.TRANSPARENT);
	}
	
	
	/**Get this component's ID
	 * @return The ID
	 */
	public CollisionID getID() {
		return ID;
	}

	/**Set this component's ID
	 * @param iD The ID
	 */
	public void setID(CollisionID iD) {
		ID = iD;
	}

	/**Get the IDs this component should collide with
	 * @return The colliding IDs
	 */
	public CollisionID[] getCollidingIDs() {
		return collidingIDs;
	}

	/**Set the IDs this component should collide with
	 * @param collidingIDs The colliding IDss
	 */
	public void setCollidingIDs(CollisionID[] collidingIDs) {
		this.collidingIDs = collidingIDs;
	}
	
	
	
	





}
