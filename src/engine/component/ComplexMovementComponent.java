package engine.component;

import org.jsfml.system.Vector2f;

import engine.entity.Entity;
import engine.graphics.GraphicsHandler;
import engine.input.EventHandler;
import util.Util;

/**Specifies movement that includes acceleration
 * @author Marco
 *
 */
public class ComplexMovementComponent extends SimpleMovementComponent {
	
	
	protected Vector2f accel;
	protected float angularAccel;
	private boolean decel = false;
	private float decelmag = .0f;
	
	
	/**
	 * @param entity The active entity
	 * @param velocity The velocity
	 * @param accel The acceleration
	 * @param omega The angular speed
	 * @param alpha The angular acceleration
	 */
	public ComplexMovementComponent(Entity entity,
			Vector2f velocity,  Vector2f accel, float omega,  float alpha) {
		super(entity, velocity, omega);
		this.accel = accel;
		this.angularAccel = alpha;
	}


	/**
	 * @return The acceleration
	 */
	public Vector2f getAccel() {
		return accel;
	}


	/**
	 * @return The angular acceleration
	 */
	public float getAngularAccel() {
		return angularAccel;
	}


	/**
	 * @param accel The acceleration
	 */
	public void setAccel(Vector2f accel) {
		this.accel = accel;
		decel = false;
	}
	
	
	/**Sets the acceleration such that the component will component will come to a stand still. Does not affect the angular acceleration.
	 * @param mag The magnitude of the acceleration 
	 */
	public void decelerate(float mag){
		decel = true;
		decelmag = mag;
	}


	/**
	 * @param alpha The angular acceleration
	 */
	public void setAngularAccel(float alpha) {
		this.angularAccel = alpha;
	}


	@Override
	public void update(float dt, float t) {
		if(decel){
			accel = Vector2f.mul(Util.normalise(this.velocity),-decelmag); //decel in opposite direction
		}
		addToVelocity(Vector2f.mul(accel, dt));
		omega += angularAccel*dt;
		super.update(dt, t);
	}
	
	


}
