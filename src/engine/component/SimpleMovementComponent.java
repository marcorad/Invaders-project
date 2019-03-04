package engine.component;

import org.jsfml.system.Vector2f;

import engine.entity.Entity;
import engine.graphics.GraphicsHandler;
import engine.input.EventHandler;
import util.Util;

/**Describes simple movement that include a velocity and angular velocity
 * @author Marco
 *
 */
public class SimpleMovementComponent extends MovementComponent {
  
	protected Vector2f velocity; 
	protected float omega; //rotational speed
	protected Vector2f velocityclamp = new Vector2f(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY); // the magnitude of velocity components is clamped to this vector
	
	/**
	 * @param entity The active entity
	 * @param velocity The velocity
	 * @param omega The angular velocity
	 */
	public SimpleMovementComponent(Entity entity,Vector2f velocity, float omega) {
		super(entity);
		this.velocity = velocity;
		this.omega = omega;
	}
	
	
	/**Add to angular velocity
	 * @param d_omega To add
	 */
	public void addToAngularSpeed(float d_omega){
		omega+=d_omega;
	}

	/**Add to velocity
	 * @param dv To add
	 */
	public void addToVelocity(Vector2f dv){
		setVelocity(Vector2f.add(velocity, dv));
	}
	
	private Vector2f mom_vel= Vector2f.ZERO;
	private float mom_vel_elapsed_t,mom_vel_t;
	private boolean mom_vel_active = false;
	
	
	public void addMomentaryVelocity(Vector2f mvel, float t){
		addToVelocity(Vector2f.mul(mom_vel, -1f));
		mom_vel = new Vector2f(
				Util.clamp(mvel.x, -velocityclamp.x, velocityclamp.x), Util.clamp(mvel.y, -velocityclamp.y, velocityclamp.y)
				);
		mom_vel_t = t;
		mom_vel_elapsed_t = 0f;
		mom_vel_active = true;
		addToVelocity(mom_vel);
	}

	/**
	 * @return Angular velocity
	 */
	public float getOmega() {
		return omega;
	}

	/**
	 * @return Velocity
	 */
	public Vector2f getVelocity() {
		return velocity;
	}

	/**
	 * @param omega Angular velocity
	 */
	public void setOmega(float omega) {
		this.omega = omega;
	}

	/**
	 * @param velocity Velocity
	 */
	public void setVelocity(Vector2f velocity) {
		this.velocity = new Vector2f(
				Util.clamp(velocity.x, -velocityclamp.x, velocityclamp.x), Util.clamp(velocity.y, -velocityclamp.y, velocityclamp.y)
				);
	}
	
	/**Clamps the magnitude of velocity components to these max values. USE ONLY POSITIVE VALUES.
	 * @param clamp The clamp consisting of x and y components
	 */
	public void setVelocityClamp(Vector2f clamp){
		velocityclamp = clamp;
	}
	
	@Override
	public void update(float dt, float t) {
		
		if(mom_vel_active){
			mom_vel_elapsed_t += dt;
			if(mom_vel_elapsed_t>= mom_vel_t){
				addToVelocity(Vector2f.mul(mom_vel, -1f));
				mom_vel_active = false;
				mom_vel = Vector2f.ZERO;
			}
		}
		
		entity.addToPosition(Vector2f.mul(velocity, dt));	
		entity.addToRotation(omega * dt);
	}
}
