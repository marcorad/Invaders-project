package engine.component;

import engine.entity.Entity;

/**
 * Kills an entity when it collides with anything.
 */
public class KillOnCollisionComponent extends OnCollisionComponent{

	/**
	 * @param entity The active entity
	 */
	public KillOnCollisionComponent(Entity entity) {
		super(entity);		
	}

	@Override
	public void notifyAction() {
		entity.kill();		
	}

}
