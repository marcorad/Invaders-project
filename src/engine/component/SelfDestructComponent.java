package engine.component;

import engine.entity.Entity;

/**
 * Will kill the entity after a certain time has passed
 */
public class SelfDestructComponent extends Component implements UpdateableComponent {
	protected final float timealive; //the time the entity is alive
	protected float elapsedtime = 0.0f;

	/**
	 * @param entity The active entity
	 * @param timealive The time until removal
	 */
	public SelfDestructComponent(Entity entity, float timealive) {
		super(entity);
		this.timealive = timealive;
	}

	@Override
	public void update(float dt, float t) {
		elapsedtime += dt;
		if(elapsedtime >= timealive){
			entity.setHealth(0f);
		}	
	}

}
