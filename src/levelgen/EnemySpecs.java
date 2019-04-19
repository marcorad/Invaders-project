package levelgen;

import java.util.Random;

import org.jsfml.system.Vector2f;

import engine.entity.SpawnFactory;
import game.Game;
import util.MinMaxPair;
import util.Pair;
import util.Util;

public abstract class EnemySpecs {
	
	public static final int MAX_ENEMIES_ON_SCREEN = 18;


	public class EnemyProperties{

		public EnemyProperties(float health, float damage) {
			this.health = health;
			this.damage = damage;
		}

		public float health, damage;
	}

	private MinMaxPair<Vector2f> regions[];
	private float minDiff;
	private float chanceRate;
	private float initialChance;
	private float maxChance;
	private float startHealth;
	private float startDamage;

	/**
	 * @param minDiff The minimum difficulty required for spawn
	 * @param chanceRate The rate at which the chance for spawn increases (chance per second per second)
	 * @param chancePerSecond The initial chance the enemy has to spawn (chance per second)
	 * @param maxChancePerSecond The maximum chance the enemy may have to spawn, which must be in the range [0,1)
	 * @param startHealth The starting health at difficulty 0
	 * @param startDamage The starting health at difficulty 0
	 * @param regions The different spawn regions, as a pair lower left corner and upper right corner
	 */
	public EnemySpecs(float minDiff, float chanceRate, float chancePerSecond, float maxChancePerSecond, float startHealth,
			float startDamage, MinMaxPair<Vector2f> ... regions) {
		this.minDiff = minDiff;
		this.chanceRate = chanceRate;
		this.regions = regions;


		/* The probability that an event with probability p will happen k times out of n trials is given by
		 * P(k, n, p) = (n choose k) * p^k * (1-p)^(n-k)
		 * When the pevent happens at least once out of 10 trials then
		 * P(k>=1, 10, p) = (sum from k=1 to 10)[(10 choose k) * (p)^k * (1-p)^(10-k)] = 1 - P(0, 10, p) 
		 * since P(k>=0, 10, p) = 1
		 * 
		 * To find the probability X s.t. chancePerSecond = Y occurs on average Y times per second
		 * Y = 1 - P(0, 10, X) 
		 *   = 1 - (10C0)*(X^0)*((1-X)^(10-0))
		 * (1-Y)^0.1 = 1-X
		 * X = 1 - (1-Y)^0.1 
		 */
		this.initialChance = 1f - (float)Math.pow(1.0 - chancePerSecond, .1);
		this.maxChance = 1f - (float)Math.pow(1.0 - maxChancePerSecond, .1);;
		this.startHealth = startHealth;
		this.startDamage = startDamage;
	}


	public abstract void spawn(Vector2f pos);

	public float spawnChance(float difficulty){		
		return Util.clamp((chanceRate*(difficulty-minDiff)+initialChance), 0f, maxChance); //offset difficulty by minDiff since an enemy that starts soawning after		
	}

	public void doSpawn(float difficulty){
		if(Game.getNumberOfEnemiesOnScreen() <= MAX_ENEMIES_ON_SCREEN){
			float random = Util.RANDOM.nextFloat();
			float spw = spawnChance(difficulty);
			if (spw > random)
				spawn(getSpawnPosition());	
		}
	}

	/**Get a random spawn position out of a random region
	 * @return The spawn position
	 */
	public Vector2f getSpawnPosition(){
		int i = Util.randInRange(0, regions.length); //choose random index
		MinMaxPair<Vector2f> reg = regions[i];
		return new Vector2f(Util.randInRange(reg.min.x, reg.max.x), Util.randInRange(reg.min.y, reg.max.y));
	}

	//getters and setters for all variables
	public float getMinDifficulty() {
		return minDiff;
	}
	public void setMinDifficulty(float minDiff) {
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

	public EnemyProperties getProperties(){
		return new EnemyProperties(LevelGen.health(this), LevelGen.damage(this));
	}
}
