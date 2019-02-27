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
	private boolean decelx = false;
	private boolean decely = false;

	private float velybefore, velxbefore;


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
		decelx = false;
		decely = false;
	}



	public void decelerateX(float mag){
		if(velocity.x != 0f){
			decelx = true;
			velxbefore = velocity.x;
			accel = new Vector2f(-Util.sgn(velxbefore) * mag , accel.y );
		}
	}

	public void decelerateY(float mag){
		if(velocity.y != 0f){
			decely = true;
			velybefore = velocity.y;
			accel = new Vector2f( accel.x , -Util.sgn(velybefore) * mag );
		}
	}


	/**
	 * @param alpha The angular acceleration
	 */
	public void setAngularAccel(float alpha) {
		this.angularAccel = alpha;
	}


	@Override
	public void update(float dt, float t) {
		addToVelocity(Vector2f.mul(accel, dt));

		if(decelx){
			if(Util.sgn(velocity.x) != Util.sgn(velxbefore)){ //the moment the sign of a component changes, the deceleration has been successful
				velocity = new Vector2f(0f, velocity.y);
				accel =  new Vector2f(0f, accel.y);
				decelx = false;
			}
		}

		if(decely){
			if(Util.sgn(velocity.y) != Util.sgn(velybefore)){ //the moment the sign of a component changes, the deceleration has been successful
				velocity = new Vector2f(velocity.x, 0f);
				accel =  new Vector2f(accel.x, 0f);
				decely = false;
			}
		}


		omega += angularAccel*dt;
		super.update(dt, t);
	}




}
