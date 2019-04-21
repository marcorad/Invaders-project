package engine.component;

import engine.entity.Entity;

/**
 * The base component that may modify other components over time
  */
public abstract class ModifierComponent extends Component implements UpdateableComponent{

	/**
	 * The elapsed time since this component was created
	 */
	protected float elapsedtime = .0f;

	/**
	 * @param entity The active entity
	 */
	protected ModifierComponent(Entity entity) {
		super(entity);
	}

	/**User specified modification 
	 * @param dt Change in time
	 * @param t Total time elapsed since program start
	 */
	protected abstract void modify();

	@Override	
	public void update(float dt, float t){
		elapsedtime += dt;
		modify();
	}  

}
