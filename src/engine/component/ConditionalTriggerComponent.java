package engine.component;

import engine.entity.Entity;

/**Enabled a component for a specified time once a specifies condition is met. Otherwise the linked component is always disabled.
 * @author Marco
 *
 */
public abstract class  ConditionalTriggerComponent extends NotifierComponent implements UpdateableComponent{
	
	protected ConditionalTriggerComponent(Entity entity, Component c, float t) {
		super(entity);
		comp = c;
		comp.setEnabled(false);
		this.t = t;
	}

	private boolean triggered = false;
	private Component comp;
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
			comp.setEnabled(false);
			triggered = false;
		}
	}


	
	

}
