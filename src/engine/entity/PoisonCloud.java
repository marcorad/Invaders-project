package engine.entity;

import org.jsfml.graphics.Color;
import org.jsfml.system.Vector2f;

import engine.component.CollisionComponent;
import engine.component.CollisionID;
import engine.component.SelfDestructComponent;
import util.Util;

public class PoisonCloud extends Entity {
	
	private float r;

	public PoisonCloud(Vector2f position, float r, float damage) {
		super(position);
		this.r = r;
		this.damage = damage;
		create();
	}
	
	private void create(){
		this.setDamage(damage);
		this.setScale(new Vector2f(r,r));
		new CollisionComponent(this, Util.REGULAR_POLYGONS[7], CollisionID.PLAYER_PROJECTILE, CollisionID.ENEMY);
		new SelfDestructComponent(this, 4.5f);
	}

	private static float spawnChance = .4f;
	
	@Override
	public void update(float dt, float t) {
		while(Util.randInRange(0f, 1f) < spawnChance){
			int amount = Util.randInRange(1, 4);
			for(int i = 0; i < amount; i++){
				Vector2f pos = Vector2f.mul(Util.randomUnitVector(0f, 360f), Util.randInRange(0, r));
				Vector2f vel = Vector2f.mul(Util.randomUnitVector(0f, 360f), Util.randInRange(-.1f, .1f));				
				SpawnFactory.spawnParticle(pos, vel, Util.randInRange(-500f, 500f), Util.randInRange(.005f, .008f), Color.GREEN, Util.randInRange(.5f, 2f), 3);
			}
		}
		super.update(dt, t);
	}
	
	

}
