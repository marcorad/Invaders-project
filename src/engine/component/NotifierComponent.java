package engine.component;

import engine.entity.Entity;

/**
 * The base component that gets notified when a condition is met, and then performs an action.
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
	public void notifyWhenConditionMet(){ 
		if(notifyCondition()) {
			notifyAction();
		}
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
