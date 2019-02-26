package engine.component;

import org.jsfml.graphics.ConvexShape;
import org.jsfml.system.Vector2f;

import engine.entity.Entity;
import engine.graphics.GraphicsHandler;
import engine.input.EventHandler;
import util.Util;

/**Specifies a component that collides
 * @author Marco
 *
 */
public class CollisionComponent extends DisplayComponent {
	
	private boolean drawhitbox = false;
	private ConvexPolygonComponent hitbox;
	

	/**Creates a collision component with a convex poly hitbox
	 * @param entity The active entity
	 * @param pts Points in the hitbox
	 */
	public CollisionComponent(Entity entity, Vector2f[] pts) {
		super(entity);	
		hitbox = new ConvexPolygonComponent(entity, pts);				
	}	
	
	/**Tests for a collision
	 * @param other The component to be collided with
	 * @return Whether they collide
	 */
	public boolean Collision(CollisionComponent other){
		return Util.convexPolyCollision(this.getHitbox(), other.getHitbox());	
	}
	
	@Override
	public void draw(GraphicsHandler graphics) {
		if(drawhitbox){
			graphics.drawToRenderTexture(getHitbox());
		}		
	}
	
	public ConvexPolygonComponent getDrawableHitbox(){
		return hitbox;
	}
	
	public ConvexShape getHitbox(){
		return (ConvexShape)hitbox.getShape();
	}

	@Override
	public void onPositionUpdate() {
		hitbox.onPositionUpdate();
	}

	@Override
	public void onRotationUpdate() {
		hitbox.onRotationUpdate();
	}

	@Override
	public void onScaleUpdate() {
		hitbox.onScaleUpdate();
	}

	/**
	 * @param b Whether hitbox should be drawn
	 */
	public void setHitboxDraw(boolean b){
		drawhitbox = b;
	}
	
	

}
