package engine.component;

import java.util.Vector;
import engine.entity.Entity;

/**Specifies a component that cycles through a collection of components. Components are activated on a step by step basis and will cycle once a specified time has passed.
 * It is assumed that all components are added before the entity starts updating. Hence the first components in the list will be enabled.
 * @author Marco
 *
 */
public class CyclingModifierComponent extends ModifierComponent {
	private final Vector<Vector<Component>> comps = new Vector<>();
	private final Vector<Float> times = new Vector<>();
	int index = 0;

	protected CyclingModifierComponent(Entity entity) {
		super(entity);
	}

	@Override
	protected void modify() {
		float t = times.get(index);
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
	
	/**Adds a list of components to the cycle, with their time on. It disables components automatically, unless they are the first components in the cycle. Then it is up to the user to specify.
	 * Adding a null list will just add a time delay.
	 * Using a negative time will disable the cycling and only allow for that list of components to be enabled permanently.
	 * A negative time and null list disables all the components permanently and stops the cycling once it reaches that stage. This allows for a sequence of non-repeatable events.
	 * @param c The components
	 * @param t The time on
	 */
	protected void addToCycle(Vector<Component> cl, float t){
		if(comps.size() != 0 && cl != null)
			for(Component c : cl) c.setEnabled(false);
		comps.add(cl);
		times.addElement(t);
	}

}
