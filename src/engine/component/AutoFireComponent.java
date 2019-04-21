package engine.component;

import engine.entity.Entity;
import engine.entity.Weapon;


/**
 * Always firing projectiles, specified by a weapon.
 *
 */
public class AutoFireComponent extends Component implements UpdateableComponent{

	/**
	 * @param entity The active entity
	 * @param w The weapon specifying the projectiles
	 */
	public AutoFireComponent(Entity entity, Weapon w) {
		super(entity);
		weapon = w;
		w.setFiring(true);
	}

	private Weapon weapon;

	@Override
	public void update(float dt, float t) {
		weapon.update(dt);
	}

}
