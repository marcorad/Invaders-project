package levelGenerator;

import org.jsfml.system.Vector2f;

public class TestEnemy extends EnemySpecs {

	public TestEnemy() {
		setMinDiff(0.1f);
		setChanceRate(10);
		setInitialChance(0.5f);
		setStartDamage(10);
		setStartHealth(30);
	}

	@Override
	void spawn(Vector2f pos) {
		// TODO Auto-generated method stub
		System.out.println("Spawning test enemies all over the show!");
	}

}
