package engine.component;

import engine.entity.Entity;

public class KillOnCollisionComponent extends OnCollisionComponent{

	public KillOnCollisionComponent(Entity entity) {
		super(entity);		
	}

	@Override
	public void notifyAction() {
		entity.kill();		
	}

}
