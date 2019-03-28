package engine.component;

import org.jsfml.graphics.Color;

import engine.entity.Entity;
import util.Oscillator.OscType;

public class DamageFlashComponent extends Component {
	
	private static float TIME = 1f;
	
	
	public DamageFlashComponent(Entity entity, SpriteComponent sc) {
		super(entity);
		
		ColorOscillationComponent osc = new ColorOscillationComponent(entity, Color.BLACK, Color.RED, 4f/(TIME), OscType.SQUARE);
		osc.addComponent(sc);
		
		new ConditionalTriggerComponent(entity, osc, TIME){
			@Override
			public boolean notifyCondition() {
				return !entity.getCollidingEntities().isEmpty();
			}

			@Override
			public void reset() {
				((ColorOscillationComponent)this.comp).setToStartColor();
			}};
	}



}
