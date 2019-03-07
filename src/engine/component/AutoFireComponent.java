package engine.component;

import engine.entity.Entity;
import engine.entity.Weapon;

/**A component that always fires projectiles, specified by a weapon.
 * @author Marco
 *
 */
public class AutoFireComponent extends Component implements UpdateableComponent{

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
