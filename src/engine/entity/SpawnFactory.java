package engine.entity;

import java.util.Vector;

import org.jsfml.graphics.Color;
import org.jsfml.system.Vector2f;

import engine.component.*;
import engine.entity.Weapon.WeaponID;
import engine.graphics.GraphicsHandler;
import game.Game;
import game.GameData;
import util.Oscillator;
import util.Oscillator.OscType;
import util.Util;

/**A class that contains all the code necessary to spawn specific entities with specific behavioural components.
 * @author Marco
 *
 */
public class SpawnFactory {
	
	public static Weapon createDefaultEnemyWeapon(Entity enem, float damage){
		Weapon w = new Weapon(1f, 1f, WeaponID.ENEMY_WEAPON, enem){
			@Override
			public void spawnProjectiles() {
				spawnBasicEnemyBullet(this.shooter, this.damage);
			}};			
			return w;
	}
	
	
	public static void spawnBasicEnemyBullet(Entity enem, float damage){
		Entity bullet = new Entity(enem.getPosition());
		bullet.setScale(new Vector2f(.05f, .05f));
		new SimpleMovementComponent(bullet, new Vector2f(0f, -1f), damage);
		new KillOnCollisionComponent(bullet);
		attachMandatoryProjectileComponents(bullet, Util.REGULAR_POLYGONS[0], CollisionID.ENEMY_PROJECTILE, CollisionID.PLAYER, CollisionID.SHIELD);
	}
	
	public static void attachMandatoryProjectileComponents(Entity projectile, Vector2f[] hitbox, CollisionID thisid, CollisionID... ids){
		new OffscreenKillComponent(projectile);
		CollisionComponent cc = new CollisionComponent(projectile, hitbox, thisid, ids);
		cc.setHitboxDraw(true);
	}
	

	/**Spawn a particle that is a regular n-gon
	 * @param pos The position in the world
	 * @param vel The velocity
	 * @param angularvel The rotation rate
	 * @param scale The scale 
	 * @param color The colour
	 * @param lifetime The lifetime of the particle
	 * @param n The number of points in the particle
	 */
	public static void spawnParticle(Vector2f pos, Vector2f vel, float angularvel, float scale, Color color, float lifetime, int n){
		Entity p = new Entity(pos);
		p.setScale(new Vector2f(scale, scale));		
		new SimpleMovementComponent(p, vel, angularvel);
		new SelfDestructComponent(p, lifetime);
		Vector2f[] pts;
		if(n<10 && n>2){
			pts = Util.REGULAR_POLYGONS[n-3];
		}
		else pts = Util.getRegularPoly(n);
		ConvexPolygonComponent shape = new ConvexPolygonComponent(p, pts, color, DisplayType.FILL);
		ColorOscillationComponent  c = new ColorOscillationComponent(p, color, Color.TRANSPARENT, .5f/lifetime, OscType.TRIANGLE);
		c.addComponent(shape);			
	}
	
	/**Spawn a projectile used for testing
	 * @param pos The world position
	 * @param vel The velocity
	 */
	public static void spawnTestProjectile(Vector2f pos, Vector2f vel){
		Entity p = new Entity(pos);
		p.setDamage(.5f);
		p.setScale(new Vector2f(.05f,.05f));
		Util.pointEntityInDirection(p, vel);
		Vector2f[] pts = new Vector2f[]{new Vector2f(0f,1f),new Vector2f(.5f,-1f),new Vector2f(-.5f,-1f)};
		new CollisionComponent(p, pts, CollisionID.PLAYER_PROJECTILE, CollisionID.ENEMY);
		new ConvexPolygonComponent(p, pts , Color.CYAN, DisplayType.FILL);
		new SimpleMovementComponent(p, vel, 0);
		new SelfDestructComponent(p, 2f);
		ParticleTrailComponent trail = new ParticleTrailComponent(p, 1f, .1f, 40f, Color.CYAN, 2, .3f);
		new OnCollisionComponent(p){
			@Override
			public void notifyAction() {
				entity.kill();
			}			
		};
		trail.setScaleDamp(.03f);
		trail.setRandomVel(.5f);
		trail.setSpeedDamp(.1f);
		spawnGunPowder(pos, vel, 3f, Color.BLACK);
	}
	
	/**Target practise and testing
	 * @param pos The position in the world
	 */
	public static void spawnTestEnemy(Vector2f pos){
		Entity enem = new Entity(pos);
		enem.setScale(new Vector2f(.07f,.08f));
		enem.setMaxHealth(4f);
		enem.healFully();
		CollisionComponent cc = new CollisionComponent(enem, Util.REGULAR_POLYGONS[7], CollisionID.ENEMY, CollisionID.PLAYER_PROJECTILE);
		new HealthBarComponent(enem);
		CyclingModifierComponent cycle = new CyclingModifierComponent(enem);
		cycle.addToCycle( .5f/.3f,new MovementOscComponent(enem, new Oscillator(.3f, .4f, 0f, 0f, OscType.SINE), new Vector2f(.1f,.4f)));
		cycle.addToCycle(.5f, new SimpleMovementComponent(enem, new Vector2f(.8f, -.5f), 0f));
		cycle.addToCycle(.5f, new SimpleMovementComponent(enem, new Vector2f(-.8f, .5f), -720f));
		cycle.addToCycle(-1f);		
		new MovementOscComponent(enem, new Oscillator(1f, .03f, 0f, Util.randInRange(0f, 2*Util.PI), OscType.SINE), new Vector2f(0f,1f));		
		new SpriteComponent(enem, 128, 0f, GameData.TEX_EXAMPLE_ENEMY);		
		ParticleTrailComponent trail = new ParticleTrailComponent(enem, .18f, .1f, 10f, Color.CYAN, 2, 1f);
		trail.setScaleDamp(.04f);
		trail.setRandomVel(.7f);
		new AutoFireComponent(enem, createDefaultEnemyWeapon(enem, 1f));
		new CollisionSoundComponent(enem, GameData.SOUND_PEEG);
	}
	
	/** Spawn a gunpowder effect used when shooting and projectile collisions
	 * @param pos The position in the world
	 * @param generalDir The general direction of the gunpowder
	 * @param magnitude The magnitude of the explosion
	 * @param color The main colour of the explosion
	 */
	public static void spawnGunPowder(Vector2f pos, Vector2f generalDir, float magnitude, Color color ){
		int amount = (int)(magnitude*5f);
		for(int i = 0; i < amount; i++){
			spawnParticle(pos, Vector2f.mul(Util.varyVector(generalDir, Util.PI/2.7f), Util.randInRange(.02f*magnitude, .08f*magnitude)), Util.randInRange(-400f, 400f), Util.randInRange(0.004f, 0.008f), color, Util.randInRange(.3f, .7f), 3);
		}
	}
	
	public static void spawnBuckShot(Entity shooter, float vel, float degreespread, float damage, int amount){
		for(int i = 0; i < amount; i++){
		Vector2f unitdir = Util.facing(shooter);
		Entity shot = new Entity(Vector2f.add(Util.approxParticleOffset(unitdir, shooter), shooter.position));
		shot.setDamage(damage);
		new SimpleMovementComponent(shot, Vector2f.mul(Util.varyVector(unitdir,  Util.toRad(degreespread)), vel), damage);
		CollisionComponent cc = new CollisionComponent(shot, Util.REGULAR_POLYGONS[0], CollisionID.PLAYER_PROJECTILE, CollisionID.ENEMY);
		new OnCollisionComponent(shot){

			@Override
			public void notifyAction() {
				entity.kill();			
			}
			
		};
		//cc.setHitboxDraw(true);
		new SpriteComponent(shot, 64, 0f, GameData.TEX_BUCKSHOT);
		new OffscreenKillComponent(shot);
		shot.setScale(new Vector2f(.015f,.015f));
		}		
	}
	
	
}
