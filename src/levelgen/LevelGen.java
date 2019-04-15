package levelgen;

import org.jsfml.system.Vector2f;

import engine.entity.SpawnFactory;
import util.MinMaxPair;

public class LevelGen{	
	
	public static float HEALTH_INCREASE_RATE = .06f, DAMAGE_INCREASE_RATE = .2f, DIFFICULTY_INCREASE_RATE = 1f; //increase per second

	@SuppressWarnings("unchecked")
	public static EnemySpecs[] ENEMY_SPAWN_SPECS = new EnemySpecs[]{			

			//min diff, chance rate, cps, max cps, start health, start damage

			//Enemy 1
			new EnemySpecs(difficultyAt(0f), 1/200f, 1/18f, 1/14f, 10f, 1f, new MinMaxPair<Vector2f>(new Vector2f(-1.2f, .8f), new Vector2f(-1.2f, .9f)), 
					new MinMaxPair<Vector2f>(new Vector2f(1.2f, .8f), new Vector2f(1.2f, .9f))){				
				public void spawn(Vector2f pos) {
					SpawnFactory.spawnEnemy1(pos);
				}				
			},

			//Enemy 2
			new EnemySpecs(difficultyAt(18f), 1/200f, 1/17f, 1/12f, 15f, 0f, new MinMaxPair<Vector2f>(new Vector2f(-.9f, 1.15f), new Vector2f(.9f, 1.25f))){				
				public void spawn(Vector2f pos) {
					SpawnFactory.spawnEnemy2(pos);
				}				
			},

			//Enemy 3
			new EnemySpecs(difficultyAt(40f), 1/200f, 1/35f, 1/25f, 30f, 0f, new MinMaxPair<Vector2f>(new Vector2f(-.9f, 1.15f), new Vector2f(.9f, 1.25f))){				
				public void spawn(Vector2f pos) {
					SpawnFactory.spawnEnemy3(pos);
				}				
			},

			//Enemy 4
			new EnemySpecs(difficultyAt(60f), 1/200f, 1/20f, 1/13f, 25f, 2f, new MinMaxPair<Vector2f>(new Vector2f(-1.2f, .8f), new Vector2f(-1.1f, 1f)), 
					new MinMaxPair<Vector2f>(new Vector2f(1.1f, .8f), new Vector2f(1.2f, 1f))){				
				public void spawn(Vector2f pos) {
					SpawnFactory.spawnEnemy4(pos);
				}				
			},

			//Enemy 5
			new EnemySpecs(difficultyAt(40f),  1/200f, 1/35f, 1/25f, 30f, 0f, new MinMaxPair<Vector2f>(new Vector2f(-.9f, 1.15f), new Vector2f(.9f, 1.25f))){				
				public void spawn(Vector2f pos) {
					SpawnFactory.spawnEnemy5(pos);
				}				
			},

			//Enemy 6
			new EnemySpecs(difficultyAt(75f), 1/200f, 1/25f, 1/15f, 10f, 0f, new MinMaxPair<Vector2f>(new Vector2f(-.9f, 1.15f), new Vector2f(.9f, 1.25f))){				
				public void spawn(Vector2f pos) {
					SpawnFactory.spawnEnemy6(pos);
				}				
			},

	}; //enemy 1 to 6 spawn specs

	

	private static float elapsed_time = 0f;
	private static float update_time = 0f;

	public static void update(float dt){
		elapsed_time += dt;
		update_time += dt;
		if(update_time >= 0.1f){ //only do spawn every 100ms (10 times per second)
			update_time -= 0.1f;
			float d = difficulty();
			for(EnemySpecs es : ENEMY_SPAWN_SPECS){
				if(d >= es.getMinDifficulty())
					es.doSpawn(difficulty());
			}
		}
	}

	public static float difficulty(){
		float diff =  DIFFICULTY_INCREASE_RATE*elapsed_time; //starts at 0
		return diff;
	}

	public static float difficultyAt(float t){
		return DIFFICULTY_INCREASE_RATE * t;
	}

	public static float health(EnemySpecs a) {
		float health = HEALTH_INCREASE_RATE*difficulty() + a.getStartHealth();
		return health;
	}

	public static float damage(EnemySpecs b){
		float damage = DAMAGE_INCREASE_RATE*difficulty()  + b.getStartDamage();
		return damage;
	}

	public static void reset(){
		elapsed_time = 0f;
		update_time = 0f;
	}	




	//	//TEST CODE
	//	public static void main(String[] args) {
	//
	//
	//		EnemySpecs es = new EnemySpecs(0f, .00001f, .1f, .1f, 10f, 10f, new MinMaxPair<Vector2f>(new Vector2f(-1.2f,.9f),(new Vector2f(-1.1f,.9f)))){
	//			@Override
	//			void spawn(Vector2f pos) {
	//				EnemyProperties prop = this.getProperties();
	//				System.out.println("SPAWNED WITH {HEALTH = " + prop.health + ", DAMAGE = " + prop.damage + "} AT t = " + elapsed_time + " WITH A CHANCE OF " + spawnChance(difficulty()) + " AT " + pos);
	//			}};
	//
	//			Clock clk = new Clock();
	//			float dt = 0f;
	//			while(elapsed_time < 10f){
	//
	//				dt = 100f*clk.restart().asSeconds();
	//				//System.out.println(dt);
	//				//simulate game that will update approximately 60 times a second
	//				update(dt);
	//					
	//							
	//			}
	//			System.out.println("DONE");
	//	}

}
