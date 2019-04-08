package engine.entity;

import java.util.Vector;

import org.jsfml.audio.Sound;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;

import engine.component.*;
import engine.entity.PowerUp.PowerUpType;
import engine.entity.Weapon.WeaponID;
import engine.graphics.GraphicsHandler;
import game.Game;
import game.GameData;
import state.StateMachine.State;
import util.Oscillator;
import util.Oscillator.OscType;
import util.Util;

/**A class that contains all the code necessary to spawn specific entities with specific behavioural components.
 * @author Marco
 *
 */
public class SpawnFactory {

	public static Weapon createDefaultEnemyWeapon(Entity enem, float damage){
		Weapon w = new Weapon(2f, 1f, WeaponID.ENEMY_WEAPON, enem){
			@Override
			public void spawnProjectiles(Vector2f pos) {
				spawnBasicEnemyBullet(this.shooter, this.damage);
			}};			
			return w;
	}


	public static void spawnBasicEnemyBullet(Entity enem, float damage){
		Entity bullet = new Entity(enem.getPosition());
		bullet.setScale(new Vector2f(.02f, .02f));
		bullet.setDamage(damage);
		new SimpleMovementComponent(bullet, new Vector2f(0f, -1f), 0);
		new KillOnCollisionComponent(bullet);
		attachMandatoryProjectileComponents(bullet, Util.REGULAR_POLYGONS[0], CollisionID.ENEMY_PROJECTILE, CollisionID.PLAYER, CollisionID.SHIELD);
	}

	public static void attachMandatoryProjectileComponents(Entity projectile,  Vector2f[] hitbox, CollisionID thisid, CollisionID... ids){
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


	/**Spawns a projectile in the direction the player is facing with all required base properties.</br>
	 * Base properties include: </br>
	 * A sprite with a base colour (adding a color oscillator can change the colour)</br>
	 * A hitbox</br>
	 * Automatic off-screen removal</br>
	 * A sound to play when fired</br>
	 * A damage to deal</br></br>
	 * 
	 * The projectile does not have any associated motion. Motion should be handled outside this method.
	 * The projectile does not get removed on collision. This should be handled outside this method.
	 * 
	 * @param player The player firing the projectile
	 * @param tex The sprite texture
	 * @param pos The position of the projectile
	 * @param hb The hitbox
	 * @param scale The scale
	 * @param firesound The sound to be played on fire
	 * @param damage The damage this projectile deals per frame
	 * @param color The base colour
	 * @return The created projectile entity
	 */
	private static Entity createBasePlayerProjectile(Entity player, Vector2f pos, Texture tex, Vector2f[] hb, float scale, Sound firesound, float damage, Color color){
		Entity proj = new Entity(pos, player.getTheta());
		proj.setScale(new Vector2f(scale, scale));
		proj.setDamage(damage);
		SpriteComponent sc = new SpriteComponent(proj, 128, 13f, tex);
		sc.setColor(color);
		new OffscreenKillComponent(proj);
		new CollisionComponent(proj, hb, CollisionID.PLAYER_PROJECTILE, CollisionID.ENEMY);
		if (firesound != null)
			GameData.playSound(firesound);
		return proj;
	}

	
	private final static float POWER_UP_PROBS[] = new float[]{.25f,.25f,.25f,.25f}; //heal, shot, damage, shield. Will return -1 if nothing is spawned.
	
	public static Entity createBaseEnemy(Vector2f pos, Texture tex, Color col, Vector2f[] hb, Sound hitsound, float scale, float health){
		Entity enem = new Entity(pos);
		enem.setScale(new Vector2f(scale,scale));
		enem.setMaxHealth(health);;
		enem.healFully();
		
		new OnDeathComponent(enem){
			@Override
			public void notifyAction() {
				spawnExplosionParticles(entity, .1f, .5f, entity.getSpriteColour());				
			}			
		};
		SpriteComponent sprite = new SpriteComponent(enem, 64, 15f, tex);
		sprite.setColor(col);
		new CollisionComponent(enem, hb, CollisionID.ENEMY, CollisionID.PLAYER_PROJECTILE, CollisionID.PLAYER);		
		new DamageFlashComponent(enem, sprite);		
		new HealthBarComponent(enem);
		
		new OnDeathComponent(enem){ //spawn powerups based off chance

			@Override
			public void notifyAction() {
				int i = Util.discreetProb(POWER_UP_PROBS);
				switch(i){
				case 0: new PowerUp(entity.getPosition(), PowerUpType.HEAL);
					break;
				case 1: new PowerUp(entity.getPosition(), PowerUpType.SHOT);
					break;
				case 2: new PowerUp(entity.getPosition(), PowerUpType.DAMAGE);
					break;
				case 3: new PowerUp(entity.getPosition(), PowerUpType.SHIELD);
					break;
				}
			}};
			
		new OnCollisionComponent(enem){
			@Override
			public void notifyAction() {
				if(entity.getCollidingEntities().contains(Game.getCurrentPlayer())) Game.stateMachine.setCurrentState(State.GAME_OVER);
			}};
			
			new NotifierComponent(enem){
				@Override
				public boolean notifyCondition() {
					return entity.getPosition().y < -1.03f;
				}
				@Override
				public void notifyAction() {
					Game.stateMachine.setCurrentState(State.GAME_OVER);
				}				
			};
		
		return enem;
	}

	public static void spawnWaveEnemy(Vector2f pos){
		Entity wave = createBaseEnemy(pos, GameData.TEX_ENEMY_WAVE, new Color(5,36,130), GameData.HB_ENEMY_WAVE, null, .1f, 10f);
		new ScaleOscComponent(wave, new Oscillator(Util.randInRange(0.9f, 1.1f), Util.randInRange(0.009f, 0.011f), 0f, Util.randInRange(-Util.PI, Util.PI), OscType.SINE), new Vector2f(1f,1f));
		new MovementOscComponent(wave, new Oscillator(Util.randInRange(0.9f, 1.1f), Util.randInRange(0.0013f, 0.016f), 0f, Util.randInRange(-Util.PI, Util.PI), OscType.SINE), new Vector2f(0f,1f));
		new AutoFireComponent(wave, createDefaultEnemyWeapon(wave, 1f));
		new SimpleMovementComponent(wave, new Vector2f(0, -0.2f), 0);
		
	}


	public static void spawnMachineGunBullet(Entity player, Vector2f pos, float damage){
		Vector2f facing = Util.facing(player);
		Entity bullet = createBasePlayerProjectile(player, pos, GameData.TEX_BULLET, GameData.HB_BULLET, .03f, null, damage, Color.BLACK);
		new KillOnCollisionComponent(bullet);
		new SimpleMovementComponent(bullet, Vector2f.mul(facing, 2.1f), 0);
		spawnGunPowder(bullet.getPosition(), facing, 3f, Color.YELLOW);
		((Player)player).applyKnockBack(.2f, .1f);

	}

	public static void spawnPoisonBlob(Entity player, Vector2f pos, float damage){
		Vector2f facing = Util.facing(player);
		Entity blob = createBasePlayerProjectile(player, pos, GameData.TEX_POISON, GameData.HB_POISON, .04f, null, damage, new Color(30,150,70,180));
		new KillOnCollisionComponent(blob);
		new OnCollisionComponent(blob){
			@Override
			public void notifyAction() {
				System.out.println(entity.getPosition());
				new PoisonCloud(entity.getPosition(), .2f, entity.getDamage());
			}};
			new SimpleMovementComponent(blob, Vector2f.mul(facing, 1.1f), Util.randInRange(-300f, 300f));
			new ScaleOscComponent(blob, new Oscillator(Util.randInRange(1f, 2f), .01f, 0f, 0f, OscType.SINE), Util.unitVectorWithRotation(45f));
			spawnGunPowder(blob.getPosition(), facing, 5f,new Color(30,150,70,180));
			((Player)player).applyKnockBack(.15f, .1f);
	}

	public static void spawnDamagingExplosion(Entity src){
		Entity expl = new Entity(src.getPosition());
		float r = .3f;
		expl.setScale(new Vector2f(r,r));
		expl.setDamage(src.getDamage());

		new CollisionComponent(expl, Util.REGULAR_POLYGONS[7], CollisionID.PLAYER_PROJECTILE, CollisionID.ENEMY);
		new SelfDestructComponent(expl, .0001f);

		spawnExplosionParticles(src, r/4f, 1f, new Color(200,200,70, 200));
	}

	public static void spawnExplosionParticles(Entity src,float spread, float speedDamp, Color color){		
		int numparticles = Util.randInRange(30, 50);

		for(int i = 0; i < numparticles; i++){
			Vector2f pos = Vector2f.add(src.position, Vector2f.mul(Util.randomUnitVector(0, 360f), Util.randInRange(0f, spread)));
			Vector2f vel = Vector2f.mul(Util.randomUnitVector(0, 360f), speedDamp*Util.randInRange(-1f, 1f));
			float angular = Util.randInRange(-1000f, 1000f);
			spawnParticle(pos, vel, angular, Util.randInRange(.007f, .015f), color, Util.randInRange(.4f, .8f), 3);
		}
	}



	public static void spawnShield(Player p, float health){
		Shield s = new Shield(p);
		s.setMaxHealth(health);
		s.healFully();
	}

	public static void spawnDart(Entity player,Vector2f pos, float damage){
		Vector2f facing = Util.facing(player);
		Entity slug = createBasePlayerProjectile(player, pos, GameData.TEX_DART, GameData.HB_DART, .04f, null, damage, Color.BLACK);
		new SimpleMovementComponent(slug, Vector2f.mul(facing, 1.5f), 0);
		spawnGunPowder(slug.getPosition(), facing, 6f, Color.CYAN);
		((Player)player).applyKnockBack(.3f, .1f);
	}

	public static void spawnRocket(Entity player,Vector2f pos, float damage){
		Entity rocket = createBasePlayerProjectile(player, pos, GameData.TEX_ROCKET, GameData.HB_ROCKET, .03f, null, damage, new Color(200, 100, 30, 255));
		ComplexMovementComponent cmc = new ComplexMovementComponent(rocket, Vector2f.mul(Util.facing(player), 0f), Vector2f.mul(Util.facing(player), 1.2f), 0f, 0f);
		cmc.setSpeedClamp(10f);
		ParticleTrailComponent pt = new ParticleTrailComponent(rocket, 0, 0.3f, 25f, Color.RED, 2, .5f);
		pt.setScaleClamp(.008f);
		pt.setRandomVel(.5f);
		pt.setScaleDamp(3f);
		new KillOnCollisionComponent(rocket);
		new OnCollisionComponent(rocket){
			@Override
			public void notifyAction() {
				SpawnFactory.spawnDamagingExplosion(entity);
			}};			
		
	}

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
		enem.setScale(new Vector2f(.1f,.1f));
		enem.setMaxHealth(4f);
		enem.healFully();
		CollisionComponent cc = new CollisionComponent(enem, GameData.HB_BENNY, CollisionID.ENEMY, CollisionID.PLAYER_PROJECTILE);
		new HealthBarComponent(enem);
		CyclingModifierComponent cycle = new CyclingModifierComponent(enem);
		new SimpleMovementComponent(enem, new Vector2f(0f, -.1f), 0f);
		cycle.addToCycle( .5f/.3f,new MovementOscComponent(enem, new Oscillator(.3f, .4f, 0f, 0f, OscType.SINE), new Vector2f(.1f,.4f)));
		cycle.addToCycle(.5f, new SimpleMovementComponent(enem, new Vector2f(.8f, -.5f), 0f));
		cycle.addToCycle(.5f, new SimpleMovementComponent(enem, new Vector2f(-.8f, .5f), -720f));
		cycle.addToCycle(-1f);		
		cycle.addToCycle(1f, new SimpleMovementComponent(enem, Vector2f.ZERO, 360f));
		cycle.addToCycle(.5f);
		cycle.addToCycle(1f, new SimpleMovementComponent(enem, Vector2f.ZERO, -360f));
		cycle.addToCycle(.5f);
		new MovementOscComponent(enem, new Oscillator(1f, .03f, 0f, Util.randInRange(0f, 2*Util.PI), OscType.SINE), new Vector2f(0f,1f));		
		SpriteComponent sprc = new SpriteComponent(enem, 64, 13f, GameData.TEX_BENNY_THE_FEESH);	
		sprc.setColor(Color.BLACK);

		new AutoFireComponent(enem, createDefaultEnemyWeapon(enem, 1f));
		new CollisionSoundComponent(enem, GameData.SOUND_PEEG);
		new DamageFlashComponent(enem, sprc);
		new ScaleOscComponent(enem, new Oscillator(Util.randInRange(1f, 2f), .02f, 0f, 0f, OscType.TRIANGLE), new Vector2f(1f,1f));
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
			spawnParticle(pos, Vector2f.mul(Util.varyVector(generalDir, Util.PI/1.4f), Util.randInRange(.06f*magnitude, .08f*magnitude)), Util.randInRange(-400f, 400f), Util.randInRange(0.004f, 0.008f), color, Util.randInRange(.1f, .4f), 3);
		}
	}

	public static void spawnBuckShot(Entity shooter, float vel, float degreespread, float damage, int amount){
		Vector2f unitdir = Util.facing(shooter);
		Vector2f pos = Vector2f.add(Util.approxParticleOffset(unitdir, shooter), shooter.position);
		spawnGunPowder(pos, unitdir, 6f, Color.BLACK);

		for(int i = 0; i < amount; i++){		
			Entity shot = new Entity(pos);
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
