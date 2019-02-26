package engine.component;

import java.util.Iterator;
import java.util.Vector;

import org.jsfml.graphics.Color;

import engine.entity.Entity;
import util.MinMaxPair;
import util.Oscillator;
import util.Util;
import util.Oscillator.OscType;

/**Specifies a component that allows for a colour to oscillate from one colour to the next
 * @author Marco
 *
 */
public class ColorOscillationComponent extends ModifierComponent {
	private Vector<ColorComponent> comps;
	private Oscillator r,g,b,a;

	/**
	 * @param start The color to start on
	 * @param end The color to end on
	 * @param freq The frequency of oscillation
	 * @param type The type of oscillation
	 */
	public ColorOscillationComponent(Entity entity, Color start, Color end, float freq, OscType type){
		super(entity);
		comps = new Vector<>();		
		
		float dr = (float) (end.r - start.r)/2.0f;
		float dg = (float) (end.g - start.g)/2.0f;
		float db = (float) (end.b - start.b)/2.0f;
		float da = (float) (end.a - start.a)/2.0f;

		MinMaxPair rm = Util.getMinMax(new float[] {start.r,end.r});
		MinMaxPair gm = Util.getMinMax(new float[] {start.g,end.g});
		MinMaxPair bm = Util.getMinMax(new float[] {start.b,end.b});
		MinMaxPair am = Util.getMinMax(new float[] {start.a,end.a});

		r = new Oscillator(freq, dr, rm.min + (rm.max - rm.min) / 2.f,  .0f, type);
		g = new Oscillator(freq, dg, gm.min + (gm.max - gm.min) / 2.f,  .0f, type);
		b = new Oscillator(freq, db, bm.min + (bm.max - bm.min) / 2.f,  .0f, type);
		a = new Oscillator(freq, da, am.min + (am.max - am.min) / 2.f,  .0f, type);
	}



	/**
	 * @param cc The component to be modified
	 */
	public void addComponent(ColorComponent cc){
		comps.addElement(cc);
	}
	
	@Override
	protected void modify() {
		Color c = new Color((int)(r.get(elapsedtime)), (int)(g.get(elapsedtime)), (int)(b.get(elapsedtime)), (int)(a.get(elapsedtime)));
		
		//ensure the entities of the comps are still useful
		Iterator<ColorComponent> it = comps.iterator();
		while(it.hasNext()){
			ColorComponent sc = it.next();
			if(sc.getEntity().shouldBeRemoved()){
				it.remove();
			} else{
				sc.setColor(c);
			}
		}
	
	}
}
