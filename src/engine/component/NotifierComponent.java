package engine.component;

import engine.entity.Entity;

/**Specifies a component that gets notified when a condition is met
 * @author Marco
 *
 */
public abstract class NotifierComponent extends Component {

	/**
	 * @param entity The active entity
	 */
	protected NotifierComponent(Entity entity) {
		super(entity);
	}
	
	
	/**
	 * Check the condition and do the appropriate action when condition is met
	 */
	public void notifyWhenConditionMet(){ //spelled with capital N since the base object class cannot have its notify() method interfered with
		if(notifyCondition()) notifyAction();
	}

	/**The condition on which the component must be notified on
	 * @return Whether the component should be notified
	 */
	public abstract boolean notifyCondition();


	/**
	 * The action that is taken when notifyCondition is met
	 */
	public abstract void notifyAction();

	
}
