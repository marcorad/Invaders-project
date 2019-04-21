package engine.component;

import engine.entity.Entity;

/**
 * The base type of component. This component is associated with a single entity and cannot be removed from that entity.
 * A component adds itself to the entity's internal lists.
 */
public abstract class Component {	
	
	protected Entity entity; //associated entity

	
	protected boolean enable = true; //whether the component is enabled
	
	/**Constructs a component and adds it to the entity
	 * @param entity The active entity.
	 */
	public Component(Entity entity) {
		this.entity = entity;		
		entity.addComponent(this);
	}
	
	/**Get the associated entity
	 * @return The active entity
	 */
	public Entity getEntity() {
		return entity;
	}
	
	/**
	 * @return Whether the component is active
	 */
	public boolean isEnabled(){
		return enable;
	}
	
	/**
	 * @param b Whether the component should be active
	 */
	public void setEnabled(boolean b){
		enable = b;
	}

	/**
	 * Toggle whether this component is active
	 */
	public void toggleEnable(){
		enable = !enable;
	}

	@Override
	public String toString() {
		return "Component{" + this.getClass().getName() + "}";
	}
	
	
	
	
}
