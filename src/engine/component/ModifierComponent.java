package engine.component;

import engine.entity.Entity;

/**Specifies a component that can modify other components over time
 * @author Marco
 *
 */
public abstract class ModifierComponent extends Component implements UpdateableComponent{

	/**
	 * The elapsed time since the component was created
	 */
	protected float elapsedtime = .0f;

	/**
	 * @param entity THe active entity
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
