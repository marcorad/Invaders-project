package engine.component;

import org.jsfml.graphics.Color;

import engine.entity.Entity;
import util.Oscillator.OscType;

/**
 * Makes an entity flash red when it has taken damage.
 *
 */
public class DamageFlashComponent extends Component {
	
	private static float TIME = 1f;
	
	
	/**
	 * @param entity The active entity
	 * @param sc The sprite to flash.
	 */
	public DamageFlashComponent(Entity entity, SpriteComponent sc) {
		super(entity);
		
		ColorOscillationComponent osc = new ColorOscillationComponent(entity, entity.getSpriteColour(), Color.RED, 4f/TIME, OscType.SQUARE);
		osc.addComponent(sc);
		
		new ConditionalTriggerComponent(entity, osc, TIME){
			@Override
			public boolean notifyCondition() {
				return entity.collidedThisFrame();
			}

			@Override
			public void reset() {
				((ColorOscillationComponent)this.comp).setToStartColor();
			}};
	}



}
