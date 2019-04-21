package engine.entity;

import org.jsfml.system.Vector2f;

import engine.component.CollisionComponent;
import engine.component.CollisionID;
import engine.component.SelfDestructComponent;
import game.Game;
import util.Util;

/**
 * A poison cloud that deals damage every frame. Drawn by the graphics card using GLSL shaders.
 */
public class PoisonCloud extends Entity {

	private float r;
	float elapsedTime = 0f;

	/**
	 * @param position The centre of the cloud
	 * @param r The radius
	 * @param damage The damage per frame
	 */
	public PoisonCloud(Vector2f position, float r, float damage) {
		super(position);
		this.r = r;
		this.damage = damage;
		create();
	}

	/**
	 * Helper method that creates the cloud
	 */
	private void create(){
		this.setDamage(damage);
		this.setScale(new Vector2f(r,r));
		if(damage >= 0f) {
			new CollisionComponent(this, Util.REGULAR_POLYGONS[7], CollisionID.PLAYER_PROJECTILE, CollisionID.ENEMY);
		}
		new SelfDestructComponent(this, 2f); //only lasts 2 seconds
		Game.graphics.addPoisonCloud(this);
	}

	@Override
	public void update(float dt, float t) {
		elapsedTime += dt;
//		while(Util.randInRange(0f, 1f) < spawnChance){
//			int amount = Util.randInRange(1, 4);
//			for(int i = 0; i < amount; i++){
//				Vector2f particlepos = Vector2f.mul(Util.randomUnitVector(0f, 360f), Util.randInRange(0, r));
//				Vector2f vel = Vector2f.mul(Util.randomUnitVector(0f, 360f), Util.randInRange(-.1f, .1f));				
//				SpawnFactory.spawnParticle(Vector2f.add(this.position, particlepos), vel, Util.randInRange(-500f, 500f), Util.randInRange(.005f, .008f), new Color(30,150,70,180), Util.randInRange(.5f, 2f), 3);
//			}
//		}
		super.update(dt, t);
	}

	public float getElapsedTime() {
		return elapsedTime;
	}
}
