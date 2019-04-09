package levelGenerator;

public class LevelGen{
	private static float elapsed_time;
	public static void update(float dt){
		elapsed_time += dt;
	}
	public static float difficulty(){
		float diff = 10*elapsed_time + 100;
		//Linear increase in difficulty, 10 is gradient and 100 is base difficulty.
		return diff;
	}
	public static float health(float diff, EnemySpecs a) {
		float health = 10*elapsed_time + a.getStartHealth();
		return health;
	}
	public static float damage(float diff, EnemySpecs b){
		float damage = 10*elapsed_time + b.getStartDamage();
		return damage;
	}
	public static void reset(){
		elapsed_time = 0.0f;
	}
		
}
