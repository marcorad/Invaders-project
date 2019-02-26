package engine.component;

import engine.entity.Entity;
import engine.graphics.GraphicsHandler;
import engine.input.EventHandler;

/**
 * Specifies the base type of component. This component is associated with a single entity and cannot be removed from that entity.
 * @author Marco
 *
 */
public abstract class Component {	
	
	protected Entity entity; //associated entity

	
	protected boolean enable = true; //whether the component is enabled
	protected Component(Entity entity) {
		this.entity = entity;		
	}
	
	/**
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
	
	
}
