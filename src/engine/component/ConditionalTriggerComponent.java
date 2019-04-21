package engine.component;

import engine.entity.Entity;

/**Enable(trigger) a linked component for a specified time once a specifies condition is met. 
 * Otherwise the linked component is always disabled.
 * 
 */
public abstract class  ConditionalTriggerComponent extends NotifierComponent implements UpdateableComponent{
	
	/**
	 * @param entity The active entity
	 * @param c The component to trigger
	 * @param t The time for which it is enabled
	 */
	protected ConditionalTriggerComponent(Entity entity, Component c, float t) {
		super(entity);
		comp = c;
		comp.setEnabled(false);
		this.t = t;
	}
	
	/**
	 * Called once the trigger completes. Can be used for any purpose by child classes.
	 */
	public abstract void reset();

	private boolean triggered = false;
	protected Component comp;
	private float t, elapsed_t;

	@Override
	public void notifyWhenConditionMet() {
		if(!triggered){
			if(notifyCondition()){
				notifyAction();
			}
		}
	}

	@Override
	public void notifyAction() {
		comp.setEnabled(true);
		elapsed_t = 0f;
		triggered = true;		
	}

	@Override
	public void update(float dt, float t) {
		elapsed_t += dt;
		if(elapsed_t >= this.t){
			reset();
			comp.setEnabled(false);
			triggered = false;			
		}
	}


	
	

}
