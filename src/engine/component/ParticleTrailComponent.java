package engine.component;

import org.jsfml.graphics.Color;
import org.jsfml.system.Vector2f;

import engine.entity.Entity;
import engine.entity.SpawnFactory;
import util.Oscillator;
import util.Util;

public class ParticleTrailComponent extends Component implements UpdateableComponent {

	private float minSpeed;
	private float speeddamp;
	private Color mainColor;
	private float timesincespawn=0f;
	private float timebetweenspawns;
	

	public ParticleTrailComponent(Entity entity, float minSpeed, float speeddamp, float freq, Color mainColor) {
		super(entity);
		this.minSpeed = minSpeed;
		this.speeddamp = speeddamp;
		this.mainColor = mainColor;
		timebetweenspawns = 1/freq;
		;
	}


	@Override
	public void update(float dt, float t) {
		timesincespawn += dt;
		if(timesincespawn >= timebetweenspawns){
			timesincespawn -= timebetweenspawns;
			if(Util.magSquared(entity.getInstantaneousVelocity()) >= minSpeed*minSpeed){
				Vector2f pos = entity.getPosition();
				Vector2f vel = entity.getInstantaneousVelocity();
				vel = Vector2f.mul(vel, speeddamp);
				vel = new Vector2f(-vel.x + Util.randInRange(-.15f, .15f),-vel.y + Util.randInRange(-.15f, .15f));
				Color c = new Color(mainColor.r + Util.randInRange(-25, 25), mainColor.g+ Util.randInRange(-25, 25),mainColor.b+ Util.randInRange(-25, 25),Util.randInRange(100, 255));
				SpawnFactory.spawnParticle(pos, vel, Util.randInRange(-30f, 30f), Util.randInRange(.02f, .05f), c, .5f, Util.randInRange(3, 6));
			}
		}

	}

}
