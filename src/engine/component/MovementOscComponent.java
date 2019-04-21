package engine.component;

import org.jsfml.system.Vector2f;

import engine.entity.Entity;
import util.Oscillator;

/**
 * Will provide oscillatory motion in a direction based off an oscillator object.
 */
public class MovementOscComponent extends MovementComponent {

	private float elapsed_time = .0f;
	
	protected Oscillator o;	
	protected Vector2f osc;
	protected Vector2f prev = Vector2f.ZERO, direction = Vector2f.ZERO; //subtracts from position so that displacement remains relative to the previous timestep
	
	
	/**
	 * @param entity The active entity
	 * @param o The oscillator
	 * @param direction The unit direction in which the entity oscillates
	 */
	public MovementOscComponent(Entity entity, Oscillator o, Vector2f direction) {
		super(entity);
		this.o = o;
		this.direction = direction;
	}	


	@Override
	public void update(float dt, float t) {
		elapsed_time += dt;
		osc = Vector2f.mul(direction, o.get(elapsed_time));
		entity.addToPosition(Vector2f.sub(osc, prev));
		prev = osc;
	}


	/**
	 * @return Unit vector direction of oscillator
	 */
	public Vector2f getDirection() {
		return direction;
	}


	/**
	 * @param direction Unit vector of oscillation direction
	 */
	public void setDirection(Vector2f direction) {
		this.direction = direction;
	}
	
	

}
