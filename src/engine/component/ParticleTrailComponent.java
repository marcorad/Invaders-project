package engine.component;

import org.jsfml.graphics.Color;
import org.jsfml.system.Vector2f;

import engine.entity.Entity;
import engine.entity.SpawnFactory;
import util.Oscillator;
import util.Util;

public class ParticleTrailComponent extends Component implements UpdateableComponent {

	private float minSpeed;
	private float speedDamp;
	private Color mainColor;
	private float timesincespawn=0f;
	private float timebetweenspawns;
	private int numParticles;
	private float lifetime;
	private float scaleDamp = .02f;
	

	public ParticleTrailComponent(Entity entity, float minSpeed, float speeddamp, float freq, Color mainColor, int numparticles, float lifetime) {
		super(entity);
		this.minSpeed = minSpeed;
		this.speedDamp = speeddamp;
		this.mainColor = mainColor;
		timebetweenspawns = 1/freq;
		numParticles = numparticles;
		this.lifetime = lifetime;
	}
	private float randomVel = .25f;
	private int colorVary = 50;


	@Override
	public void update(float dt, float t) {
		
		timesincespawn += dt;
		if(timesincespawn >= timebetweenspawns){
			timesincespawn -= timebetweenspawns;			
			if(Util.magSquared(entity.getInstantaneousVelocity()) >= minSpeed*minSpeed){
				
				for(int i = 0; i < numParticles; i ++){
				Vector2f vel = entity.getInstantaneousVelocity();				
				vel = new Vector2f(-vel.x + Util.randInRange(-randomVel, randomVel),-vel.y + Util.randInRange(-randomVel, randomVel));	
				vel = Vector2f.mul(vel, speedDamp);

				Vector2f pos = entity.getPosition();
				//Vector2f scale = entity.getScale();
				//scale = new Vector2f(Util.sgn(vel.x)*scale.x, Util.sgn(vel.y)*scale.y);
				pos = Vector2f.add(pos, Util.approxParticleOffset(Util.normalise(vel), entity));
				
				Color c = Util.colorWithVariation(mainColor, colorVary);
				SpawnFactory.spawnParticle(pos, vel, Util.randInRange(-360, 360), Util.randInRange(.9f*scaleDamp*Util.mag(vel), 1.1f*scaleDamp*Util.mag(vel)), c, Util.randInRange(lifetime - .1f*lifetime, lifetime+.1f*lifetime), Util.randInRange(3, 5));
				}
				
			}
		}

	}	

	

	public float getScaleDamp() {
		return scaleDamp;
	}



	public void setScaleDamp(float scaleDamp) {
		this.scaleDamp = scaleDamp;
	}



	public float getRandomVel() {
		return randomVel;
	}




	public void setRandomVel(float randomVel) {
		this.randomVel = randomVel;
	}




	public float getMinSpeed() {
		return minSpeed;
	}


	public void setMinSpeed(float minSpeed) {
		this.minSpeed = minSpeed;
	}


	public float getSpeedDamp() {
		return speedDamp;
	}


	public void setSpeedDamp(float speedDamp) {
		this.speedDamp = speedDamp;
	}


	public Color getMainColor() {
		return mainColor;
	}


	public void setMainColor(Color mainColor) {
		this.mainColor = mainColor;
	}


	public int getNumParticles() {
		return numParticles;
	}


	public void setNumParticles(int numParticles) {
		this.numParticles = numParticles;
	}


	public float getLifetime() {
		return lifetime;
	}


	public void setLifetime(float lifetime) {
		this.lifetime = lifetime;
	}


	public int getColorVary() {
		return colorVary;
	}


	public void setColorVary(int colorVary) {
		this.colorVary = colorVary;
	}
	
	

}
