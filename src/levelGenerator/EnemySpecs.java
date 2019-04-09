package levelGenerator;

import java.util.Random;

//import java.util.Vector;
import org.jsfml.system.Vector2f;

import util.Util;

public abstract class EnemySpecs {
	private float minDiff;
	private float chanceRate;
	private float initialChance;
	private float maxChance;
	private float startHealth;
	private float startDamage;
	
	abstract void spawn(Vector2f pos);
	
	public float spawnChance(float difficulty){
		if (difficulty < minDiff ){
			return Util.clamp((chanceRate*difficulty+initialChance), 0, 1);
		} else return 0;
	}
	
	public void doSpawn(float difficulty){
		Random r = new Random();
		float random = r.nextFloat();
		float spw = spawnChance(difficulty);

		if (random < spw) spawn(Vector2f.ZERO);
	}
	
	//getters and setters for all variables
	public float getMinDiff() {
		return minDiff;
	}
	public void setMinDiff(float minDiff) {
		this.minDiff = minDiff;
	}
	public float getChanceRate() {
		return chanceRate;
	}
	public void setChanceRate(float chanceRate) {
		this.chanceRate = chanceRate;
	}
	public float getInitialChance() {
		return initialChance;
	}
	public void setInitialChance(float initialChance) {
		this.initialChance = initialChance;
	}
	public float getMaxChance() {
		return maxChance;
	}
	public void setMaxChance(float maxChance) {
		this.maxChance = maxChance;
	}
	public float getStartHealth() {
		return startHealth;
	}
	public void setStartHealth(float startHealth) {
		this.startHealth = startHealth;
	}
	public float getStartDamage() {
		return startDamage;
	}
	public void setStartDamage(float startDamage) {
		this.startDamage = startDamage;
	}
}
