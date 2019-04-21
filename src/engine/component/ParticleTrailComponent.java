package engine.component;

import org.jsfml.graphics.Color;
import org.jsfml.system.Vector2f;

import engine.entity.Entity;
import engine.entity.SpawnFactory;
import util.Util;

/**
 * Allow an entity to have a particle trail behind it based off certain parameters.
 */
public class ParticleTrailComponent extends Component implements UpdateableComponent {

	private float minSpeed;
	private float speedDamp;
	private Color mainColor;
	private float timesincespawn=0f;
	private float timebetweenspawns;
	private int numParticles;
	private float lifetime;
	private float scaleDamp = .02f;
	private float scaleClamp = Float.POSITIVE_INFINITY;
	

	/**
	 * @param entity The active entity
	 * @param minSpeed The minimum speed an entity must have before the particle trail can take effect
	 * @param speeddamp The damping factor applied to the speed of the particles. When set to 1, it will travel at the same speed, opposite the motion of the entity.
	 * @param freq The rate at which particles are spawned
	 * @param mainColor The main colour of the particles
	 * @param numparticles The number of particles spawned at a time
	 * @param lifetime The average lifetime of a particle
	 */
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
				pos = Vector2f.add(pos, Util.approxSpawnOffset(Util.normalise(vel), entity));
				
				Color c = Util.colorWithVariation(mainColor, colorVary);
				SpawnFactory.spawnParticle(pos, vel, Util.randInRange(-360, 360), Util.clamp(Util.randInRange(.9f*scaleDamp*Util.mag(vel), 1.1f*scaleDamp*Util.mag(vel)), 0f, scaleClamp), c, Util.randInRange(lifetime - .1f*lifetime, lifetime+.1f*lifetime), Util.randInRange(3, 5));
				}
				
			}
		}

	}	

	/**Get how the factor that determines how the speed of the particle affects the scale of the particle
	 * @return The scale damping factor
	 */
	public float getScaleDamp() {
		return scaleDamp;
	}

	/**Get the current scale clamp
	 * @return The scale clamp
	 */
	public float getScaleClamp() {
		return scaleClamp;
	}

	/**Set the absolute maximum scale of a spawned particle
	 * @param scaleClamp The scale clamp
	 */
	public void setScaleClamp(float scaleClamp) {
		this.scaleClamp = scaleClamp;
	}

	/**
	 *Set the factor that determines how the speed of the particle affects the scale of the particle.
	 *@param scaleDamp The scale damp
	 */
	public void setScaleDamp(float scaleDamp) {
		this.scaleDamp = scaleDamp;
	}


	/**Get the factor that randomises the velocity of a particle
	 * @return The random velocity factor
	 */
	public float getRandomVel() {
		return randomVel;
	}

	/***Set the factor that randomises the velocity of a particle
	 * @param randomVel The random velocity factor
	 */
	public void setRandomVel(float randomVel) {
		this.randomVel = randomVel;
	}


	/**Get the minimum speed required before the trail takes effect
	 * @return The minimum speed
	 */
	public float getMinSpeed() {
		return minSpeed;
	}


	/**Set the minimum speed required before the trail takes effect
	 * @param minSpeed The minimum speed
	 */
	public void setMinSpeed(float minSpeed) {
		this.minSpeed = minSpeed;
	}


	/**Get the damping factor that determines the speed of the particle relative to the attached entity.
	 * @return The speed damping factor
	 */
	public float getSpeedDamp() {
		return speedDamp;
	}


	/**Set the damping factor that determines the speed of the particle relative to the attached entity
	 * @param speedDamp The speed damping factor 
	 */
	public void setSpeedDamp(float speedDamp) {
		this.speedDamp = speedDamp;
	}


	/**Get the main colour of the trail
	 * @return The main colour
	 */
	public Color getMainColor() {
		return mainColor;
	}


	/**Set the main color of the trail
	 * @param mainColor The main colour
	 */
	public void setMainColor(Color mainColor) {
		this.mainColor = mainColor;
	}


	/**Get the number of particles spawned per call
	 * @return The number of particles
	 */
	public int getNumParticles() {
		return numParticles;
	}


	/**	Set the number of particles spawned per call
	 * @param numParticles The number of particles
	 */
	public void setNumParticles(int numParticles) {
		this.numParticles = numParticles;
	}


	/**Get the average lifetime of a particle
	 * @return The average lifetime 
	 */
	public float getLifetime() {
		return lifetime;
	}


	/**Set the average lifetime of a particle
	 * @param lifetime The average lifetime
	 */
	public void setLifetime(float lifetime) {
		this.lifetime = lifetime;
	}


	/**Get the factor that determines how the colour of a particle may vary
	 * @return The colour vary factor
	 */
	public int getColorVary() {
		return colorVary;
	}

	/**Set the factor that determines how the colour of a particle may vary
	 * @param colorVary The colour vary factor
	 */
	public void setColorVary(int colorVary) {
		this.colorVary = colorVary;
	}
	
}
