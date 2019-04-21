package levelgen;

import org.jsfml.system.Vector2f;

import engine.entity.SpawnFactory;
import game.Game;
import state.StateMachine.State;
import util.MinMaxPair;

/**
 *Randomly spawns enemies based off predefined SpawnSpecs. Specs are functions of difficulty, with difficulty a function of time.
 *THESE SPECS TOOK A LOT OF PLAYTESTING.
 */
public class LevelGen{	

	public static float HEALTH_INCREASE_RATE = .05f, DAMAGE_INCREASE_RATE = .01f, DIFFICULTY_INCREASE_RATE = 1.05f; //increase per second

	public static SpawnSpecs[] ENEMY_SPAWN_SPECS = new SpawnSpecs[]{			

			//min diff, chance rate, cps, max cps, start health, start damage

			//Enemy 1
			new SpawnSpecs(difficultyAt(0f), 1/200f, 1/184f, 1/14f, 10f, 2f, new MinMaxPair<Vector2f>(new Vector2f(-1.2f, .8f), new Vector2f(-1.2f, .9f)), 
					new MinMaxPair<Vector2f>(new Vector2f(1.2f, .8f), new Vector2f(1.2f, .9f))){				
				@Override
				public void spawn(Vector2f pos) {
					SpawnFactory.spawnEnemy1(pos);
				}				
			},

			//Enemy 2
			new SpawnSpecs(difficultyAt(18f), 1/200f, 1/174f, 1/15f, 15f, 0f, new MinMaxPair<Vector2f>(new Vector2f(-.9f, 1.15f), new Vector2f(.9f, 1.25f))){				
				@Override
				public void spawn(Vector2f pos) {
					SpawnFactory.spawnEnemy2(pos);
				}				
			},

			//Enemy 3
			new SpawnSpecs(difficultyAt(40f), 1/200f, 1/703f, 1/65f, 30f, 0f, new MinMaxPair<Vector2f>(new Vector2f(-.9f, 1.15f), new Vector2f(.9f, 1.25f))){				
				@Override
				public void spawn(Vector2f pos) {
					SpawnFactory.spawnEnemy3(pos);
				}				
			},

			//Enemy 4
			new SpawnSpecs(difficultyAt(60f), 1/200f, 1/304f, 1/25f, 18f, 3f, new MinMaxPair<Vector2f>(new Vector2f(-1.2f, .8f), new Vector2f(-1.1f, 1f)), 
					new MinMaxPair<Vector2f>(new Vector2f(1.1f, .8f), new Vector2f(1.2f, 1f))){				
				@Override
				public void spawn(Vector2f pos) {
					SpawnFactory.spawnEnemy4(pos);
				}				
			},

			//Enemy 5
			new SpawnSpecs(difficultyAt(40f),  1/200f, 1/703f, 1/65f, 30f, 0f, new MinMaxPair<Vector2f>(new Vector2f(-.9f, 1.15f), new Vector2f(.9f, 1.25f))){				
				@Override
				public void spawn(Vector2f pos) {
					SpawnFactory.spawnEnemy5(pos);
				}				
			},

			//Enemy 6
			new SpawnSpecs(difficultyAt(75f), 1/200f, 1/804f, 1/40f, 7.5f, 0f, new MinMaxPair<Vector2f>(new Vector2f(-.9f, 1.15f), new Vector2f(.9f, 1.25f))){				
				@Override
				public void spawn(Vector2f pos) {
					SpawnFactory.spawnEnemy6(pos);
				}				
			},

	}; //enemy 1 to 6 spawn specs



	private static float elapsed_time = 0f;
	private static float update_time = 0f;

	/**Update the level generator
	 * @param dt Change in time
	 */
	public static void update(float dt){
		if(Game.getNumberOfEnemiesLeftToKill() == 0){
			Game.stateMachine.setCurrentState(State.GAME_OVER);
		}
		elapsed_time += dt;
		update_time += dt;
		if(update_time >= 0.1f){ //only do spawn every 100ms (10 times per second)
			update_time -= 0.1f;
			float d = difficulty();			
			for(SpawnSpecs es : ENEMY_SPAWN_SPECS){
				if(d >= es.getMinDifficulty()){
					if( Game.getNumberOfEnemiesLeftToSpawn() > 0){
						es.doSpawn(difficulty());
					} 
				}
			}
		}
	}

	/**
	 * @return Difficulty as a function of time
	 */
	public static float difficulty(){
		float diff =  DIFFICULTY_INCREASE_RATE*elapsed_time; //starts at 0
		return diff;
	}

	/**Inverse difficulty function.
	 * @param t The time
	 * @return The difficulty at that specific time.
	 */
	public static float difficultyAt(float t){
		return DIFFICULTY_INCREASE_RATE * t;
	}

	/**Get the health of based off a SpawnSpecs object.
	 * @param a The specs
	 * @return The health as a function of difficulty.
	 */
	public static float health(SpawnSpecs a) {
		float health = HEALTH_INCREASE_RATE*difficulty() + a.getStartHealth();
		return health;
	}

	/**Get the damage of based off a SpawnSpecs object.
	 * @param b The specs
	 * @return The damage as a function of difficulty.
	 */
	public static float damage(SpawnSpecs b){
		float damage = DAMAGE_INCREASE_RATE*difficulty()  + b.getStartDamage();
		return damage;
	}

	/**
	 * Reset the level generator.
	 */
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
