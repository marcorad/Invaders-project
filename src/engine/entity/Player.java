package engine.entity;

import org.jsfml.graphics.Color;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.event.MouseWheelEvent;

import engine.component.CollisionComponent;
import engine.component.ComplexMovementComponent;
import engine.component.Component;
import engine.component.ConvexPolygonComponent;
import engine.component.HealthBarComponent;
import engine.component.KeyboardMoveComponent;
import engine.component.MouseMoveControlComponent;
import engine.component.MovementComponent;
import engine.component.ParticleTrailComponent;
import engine.component.SpriteComponent;
import engine.component.UpdateableComponent;
import util.Util;

/**A class that specifically describes a player entity, since its behaviour is complex.
 * @author Marco
 *
 */
public class Player extends Entity {
	
	protected static Vector2f currentmouse = Vector2f.ZERO;
	
	private ComplexMovementComponent movement;
	private SpriteComponent sprite;
	private CollisionComponent collision;
	private ConvexPolygonComponent hitbox;
	private KeyboardMoveComponent keys;
	private MouseMoveControlComponent mouse;
	private float accelmag = 4.5f;
	
	private Vector2f maxvel = new Vector2f(1f,1f);

	/**Construct a player at a certain position
	 * @param position
	 */
	public Player(Vector2f position) {
		super(position);	
		create();
		this.setScale(new Vector2f(.1f,.1f));
	}
	
	private void create(){
		movement = new ComplexMovementComponent(this, Vector2f.ZERO, Vector2f.ZERO, 0f, 0f);
		movement.setVelocityClamp(maxvel);
		this.setMinPosition(new Vector2f(-.88f, -.88f));
		this.setMaxPosition(new Vector2f(.88f, -.6f));
		
		keys = new KeyboardMoveComponent(this,Key.W, Key.S, Key.A, Key.D, null,null,movement){
			@Override
			public void onDirection(Vector2f dir) {
				ComplexMovementComponent c = (ComplexMovementComponent)movement;
				c.setAccel(Vector2f.mul(dir, accelmag));
				if(dir.x == 0f){
					c.decelerateX(accelmag);
				}
				if(dir.y == 0f){
					c.decelerateY(accelmag);
				}
			}

			@Override
			public void special1Pressed() {}

			@Override
			public void special1Released() {}

			@Override
			public void special2Pressed() {}

			@Override
			public void special2Released() {}
			
		};	
		
		hitbox = new ConvexPolygonComponent(this, new Vector2f[]{new Vector2f(0f,.9f), new Vector2f(.8f,.65f), new Vector2f(.8f,-1f), new Vector2f(-.8f,-1f), new Vector2f(-.8f,.65f)});
		hitbox.setColor(Color.RED);
		
		mouse = new MouseMoveControlComponent(this){
			@Override
			public void onRightMousePress(Vector2f worldpos) {
				System.out.println("RIGHT MOUSE PRESS: " + worldpos);
			}

			@Override
			public void onLeftMousePress(Vector2f worldpos) {
				Vector2f dir = Util.normalise(Vector2f.sub( currentmouse, position));				
				SpawnFactory.spawnTestProjectile(Vector2f.add(entity.getPosition(),Util.approxParticleOffset(dir, entity)), Vector2f.mul(dir, 3f));				
				System.out.println("LEFT MOUSE PRESS: " + worldpos);
			}

			@Override
			public void onRightMouseRelease(Vector2f worldpos) {
				System.out.println("RIGHT MOUSE RELEASE: " + worldpos);
			}

			@Override
			public void onLeftMouseRelease(Vector2f worldpos) {
				System.out.println("LEFT MOUSE RELEASE: " + worldpos);
				
			}

			@Override
			public void onMouseMove(Vector2f worldpos) {
				currentmouse = worldpos;				
			}
			
		};
		
		ParticleTrailComponent trail = new ParticleTrailComponent(this, .5f, .4f, 30f, Color.RED, 2, .5f);
		trail.setColorVary(255);
		trail.setRandomVel(.35f);
	
		new HealthBarComponent(this);
			
		
	}

	//overridden to make the rotation based of the mouse position
	@Override
	public void update(float dt, float t){
		collidingentities.clear(); //clear the colliding entities since it is now a new frame
		previous_dt = dt;
		previous_pos = position;
		
		Util.pointEntityInDirection(this,Vector2f.sub(currentmouse, position));
		
		for(UpdateableComponent uc : updatecomps){
			if(((Component)uc).isEnabled())
				uc.update(dt, t);
		}
		updateNotifierComponents();
		if(this.health <= 0f) remove();
	}
}
