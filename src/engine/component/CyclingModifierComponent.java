package engine.component;

import java.util.ArrayList;

import engine.entity.Entity;

/**A component that has a list of arrays of components attached to it. Each array in the list will get activated for a specified period of time.
 * Once the time has passed, the next array in the cycle gets activated. See addToCycle() method for specifics of operation.
 * This component is very powerful, for complicated behaviour.
 */
public class CyclingModifierComponent extends ModifierComponent {
	private final ArrayList<Component[]> comps = new ArrayList<>();
	private final ArrayList<Float> times = new ArrayList<>();
	int index = 0;

	/**
	 * @param entity The active entity
	 */
	public CyclingModifierComponent(Entity entity) {
		super(entity);
	}

	@Override
	protected void modify() {
		float t = times.get(index);
		for(Component c: comps.get(index)){ //forcibly enable this component
			c.setEnabled(true);
		}
		if(t >= 0f){
			if(this.elapsedtime >= t){
				elapsedtime -= t;			
				//deactivate current list
				if(comps.get(index) != null) {
					for(Component c : comps.get(index)){
						c.setEnabled(false);
					}
				}			
				//increment index
				index = (index+1)%comps.size();
				//activate next list
				if(comps.get(index) != null) {
					for(Component c : comps.get(index)){
						c.setEnabled(true);
					}
				}
			}
		}
	}

	/**Adds an array of components to the cycle, with their time on. It disables components automatically, unless they are the first components in the cycle. Then it is up to the user to specify.
	 * Adding a null array will just add a time delay.
	 * Using a negative time will disable the cycling and only allow for that list of components to be enabled permanently.
	 * A negative time and null array disables all the components permanently and stops the cycling once it reaches that stage. This allows for a sequence of non-repeatable events.
	 * Components added to this list will be forcibly enabled when this CyclingModifierComponent gets updated.
	 * @param t The time on
	 * @param cl The Components 
	 */
	public void addToCycle(float t, Component... cl){
		if(comps.size() != 0 && cl != null) {
			for(Component c : cl) {
				c.setEnabled(false);
			}
		}
		comps.add(cl);
		times.add(t);
	}

}
