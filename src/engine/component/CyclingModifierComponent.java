package engine.component;

import java.util.Vector;
import engine.entity.Entity;

/**Specifies a component that cycles through a collection of components. Components are activated on a step by step basis and will cycle once a specified time has passed.
 * It is assumed that all components are added before the entity starts updating. Hence the first components in the list will be enabled.
 * @author Marco
 *
 */
public class CyclingModifierComponent extends ModifierComponent {
	private final Vector<Component[]> comps = new Vector<>();
	private final Vector<Float> times = new Vector<>();
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
				if(comps.get(index) != null)
					for(Component c : comps.get(index)){
						c.setEnabled(false);
					}			
				//increment index
				index = (index+1)%comps.size();
				//activate next list
				if(comps.get(index) != null)
					for(Component c : comps.get(index)){
						c.setEnabled(true);
					}
			}
		}
	}

	/**Adds a list of components to the cycle, with their time on. It disables components automatically, unless they are the first components in the cycle. Then it is up to the user to specify.
	 * Adding a null array will just add a time delay.
	 * Using a negative time will disable the cycling and only allow for that list of components to be enabled permanently.
	 * A negative time and null array disables all the components permanently and stops the cycling once it reaches that stage. This allows for a sequence of non-repeatable events.
	 * Components added to this list will be forcibly enabled when this CyclingModifierComponent gets updated.
	 * @param t The time on
	 * @param cl The Components 
	 */
	public void addToCycle(float t, Component... cl){
		if(comps.size() != 0 && cl != null)
			for(Component c : cl) c.setEnabled(false);
		comps.add(cl);
		times.addElement(t);
	}

}
