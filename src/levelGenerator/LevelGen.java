package levelGenerator;

import org.jsfml.system.Clock;

public class LevelGen{
	private static float elapsed_time;
	public static void update(float dt){
		elapsed_time += dt;
	}
	public static float difficulty(){
		float diff = 10*elapsed_time;
		//Linear increase in difficulty, 0.05f is the gradient and 1 is base difficulty.
		return diff;
	}
	public static float health(float diff, EnemySpecs a) {
		float health = 0.05f*elapsed_time + a.getStartHealth();
		return health;
	}
	public static float damage(float diff, EnemySpecs b){
		float damage = 10*elapsed_time + b.getStartDamage();
		return damage;
	}
	public static void reset(){
		elapsed_time = 0.0f;
	}
	
	public static void main(String[] args) {
		float et = 0.0f;
		Clock clk = new Clock();
		while(et < 10){
			float dt = 10*clk.restart().asSeconds(); /* The 10 is the scale at which the time difference is multiplied */
			et += dt;
			//Testing
			float diff = difficulty();
			System.out.println(diff);
			update(dt);
		}
	}
		
}
