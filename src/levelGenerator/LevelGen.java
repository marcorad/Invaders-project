package levelGenerator;

import org.jsfml.system.Clock;

public class LevelGen{
	private static float elapsed_time;
	public static void update(float dt){
		elapsed_time += dt;
	}
	public static float difficulty(){
		float diff = 0.05f*elapsed_time;
		return diff;
	}
	public static float health(float diff, EnemySpecs a) {
		float health = 15*diff*elapsed_time + a.getStartHealth();
		return health;
	}
	public static float damage(float diff, EnemySpecs b){
		float damage = 2*diff*elapsed_time + b.getStartDamage();
		return damage;
	}
	public static void reset(){
		elapsed_time = 0.0f;
	}
	
	public static void main(String[] args) {
		float et = 0.0f;
		Clock clk = new Clock();
		EnemySpecs Reaper = new TestEnemy();		
		while(et < 10){
			float dt = 100*clk.restart().asSeconds(); /* The 10 is the scale at which the time difference is multiplied */
			et += dt;
			float diff = difficulty();
//			The following tests functions: difficulty, doSpawn, health, damage.
//			System.out.println(diff);
//			Reaper.doSpawn(diff); 
//			float healthCheck = health(diff, Reaper);
//			System.out.println("Health: " + healthCheck);
//			float damageCheck = damage(diff, Reaper);
//			System.out.println("Damage: " + damageCheck);	
			update(dt);
		}
	}
		
}
