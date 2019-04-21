package engine.component;

import org.jsfml.system.Vector2f;

import engine.entity.Entity;
import util.Util;

/**
 * Describes simple movement that include a velocity and angular velocity 
 */
public class SimpleMovementComponent extends MovementComponent {
  
	protected Vector2f velocity; 
	protected float omega; //rotational speed
	protected float speedclamp = Float.POSITIVE_INFINITY; // the magnitude of velocity components is clamped to this vector
	
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
	
	private Vector2f knockback_vel= Vector2f.ZERO;
	private float knockback_elapsed_t,knockback_t;
	private boolean knockback_active = false;
	
	
	/**Apply knockback by momentarily adding to the velocity for a specific time period. Note that this is independent of the speed clamping.
	 * @param kb The knockback velocity
	 * @param t The time to apply the velocity
	 */
	public void applyKnockback(Vector2f kb, float t){
		knockback_vel = kb;
		knockback_t = t;
		knockback_elapsed_t = 0f;
		knockback_active = true;
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
		this.velocity = velocity;
		clampVel();
	}
	
	/**Clamps the magnitude of velocity components to these max values. USE ONLY POSITIVE VALUES.
	 * @param clamp The clamp consisting of x and y components
	 */
	public void setSpeedClamp(float clamp){
		speedclamp = clamp;
	}
	
	/**
	 * Clamp the velocity.
	 */
	private void clampVel(){
		if(Util.magSquared(velocity) > speedclamp*speedclamp){
			velocity = Util.getVectorFromPolar(speedclamp, Util.vectorAngle(velocity));
		}
	}
	
	@Override
	public void update(float dt, float t) {
		
		if(knockback_active){
			knockback_elapsed_t += dt;
			if(knockback_elapsed_t>= knockback_t){
				knockback_active = false;
			} else {
				entity.addToPosition(Vector2f.mul(knockback_vel, dt));
			}
		}
		
		entity.addToPosition(Vector2f.mul(velocity, dt));	
		entity.addToRotation(omega * dt);
	}
}
