package engine.entity;

import java.util.Vector;

import org.jsfml.audio.Sound;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;

import engine.component.AutoFireComponent;
import engine.component.CollisionComponent;
import engine.component.CollisionID;
import engine.component.ColorOscillationComponent;
import engine.component.ComplexMovementComponent;
import engine.component.ConvexPolygonComponent;
import engine.component.CyclingModifierComponent;
import engine.component.DamageFlashComponent;
import engine.component.DisplayType;
import engine.component.HealthBarComponent;
import engine.component.KillOnCollisionComponent;
import engine.component.MovementOscComponent;
import engine.component.NotifierComponent;
import engine.component.OffscreenRemoveComponent;
import engine.component.OnCollisionComponent;
import engine.component.OnDeathComponent;
import engine.component.ParticleTrailComponent;
import engine.component.ScaleOscComponent;
import engine.component.SelfDestructComponent;
import engine.component.SimpleMovementComponent;
import engine.component.SpriteComponent;
import engine.entity.EnemyDrop.DropType;
import engine.entity.Weapon.WeaponID;
import game.Game;
import game.GameData;
import levelgen.LevelGen;
import levelgen.SpawnSpecs.EnemyProperties;
import state.StateMachine.State;
import util.Oscillator;
import util.Oscillator.OscType;
import util.Util;

/**
 * A class that contains all the code necessary to spawn specific entities with specific behavioural components.
 * This class contains MANY outdated and debug methods, which are kept for reference and testing (and for LOLs).
 */
public class SpawnFactory {

	/**OLD TEST METHOD
	 * @param enem
	 * @param damage
	 * @return
	 */
	public static Weapon createDefaultEnemyWeapon(Entity enem, float damage){
		Weapon w = new Weapon(2f, 1f, WeaponID.ENEMY_WEAPON, enem){
			@Override
			public void spawnProjectiles(Vector2f pos) {

				spawnBasicEnemyBullet(this.shooter, this.damage);
			}};			
			return w;
	}

	/**Attach weapon 1
	 * @param enem The enemy to attach to
	 * @param damage Projectile damage
	 */
	public static void attatchEnemyWeapon1(Entity enem, float damage){
		Weapon w = new Weapon(damage, 6.2f, WeaponID.ENEMY_WEAPON, enem, GameData.SOUND_ENEMY_PROJECTILES[1]){
			@Override
			public void spawnProjectiles(Vector2f pos) {
				pos = Vector2f.sub(pos, new Vector2f(0f, shooter.getScale().y));
				spawnEnemyBullet1(pos, this.damage);
			}
		};	
		w.randomiseTimeOffset();
		new AutoFireComponent(enem, w);			
	}

	/**Attach weapon 2
	 * @param enem The enemy to attach to
	 * @param damage Projectile damage
	 */
	public static void attatchEnemyWeapon2(Entity enem, float damage){
		Weapon w = new Weapon(damage, 8f, WeaponID.ENEMY_WEAPON, enem,GameData.SOUND_ENEMY_PROJECTILES[0]){
			@Override
			public void spawnProjectiles(Vector2f pos) {
				pos = Vector2f.sub(pos, new Vector2f(0f, shooter.getScale().y));
				spawnEnemyBullet2(pos, this.damage);
			}
		};		
		w.randomiseTimeOffset();
		new AutoFireComponent(enem, w);			
	}

	/**Spawn bullet 1
	 * @param pos The position
	 * @param damage Its damage
	 */
	public static void spawnEnemyBullet1(Vector2f pos, float damage){
		spawnEnemyProjectile(pos, GameData.TEX_STARRY_PROJECTILE, GameData.HB_STARRY_PROJECTILE, .048f, null, damage, Color.WHITE);
	}

	/**Spawn bullet 2
	 * @param pos The position
	 * @param damage Its damage
	 */
	public static void spawnEnemyBullet2(Vector2f pos, float damage){
		spawnEnemyProjectile(pos, GameData.TEX_CROSS_PROJECTILE, GameData.HB_CROSS_PROJECTILE, .048f, null, damage, Color.WHITE);
	}

	/**Spawn the base enemy projectile. </br>
	 * Base properties include: </br>
	 * A sprite</br>
	 * Offscreen removal</br>
	 * Collision</br>
	 * Simple movement</br>
	 * Death on collision </br>
	 * @param pos Position
	 * @param tex Sprite texture
	 * @param hb Hitbox data
	 * @param scale Scale
	 * @param firesound Sound when fired
	 * @param damage Damage
	 * @param color Colour
	 */
	private static void spawnEnemyProjectile(Vector2f pos, Texture tex, Vector2f[] hb, float scale, Sound firesound, float damage, Color color){
		Entity proj = new Entity(pos);
		proj.setScale(new Vector2f(scale, scale));
		proj.setDamage(damage);
		SpriteComponent sc = new SpriteComponent(proj, GameData.PROJECTILE_WIDTH, 13f, tex);
		sc.setColor(color);
		new OffscreenRemoveComponent(proj);
		new CollisionComponent(proj, hb, CollisionID.ENEMY_PROJECTILE, CollisionID.PLAYER, CollisionID.SHIELD);
		if (firesound != null) {
			GameData.playSound(firesound);
		}
		new SimpleMovementComponent(proj, new Vector2f(0f,-.38f), Util.randInRange(-360f, 360f));
		new KillOnCollisionComponent(proj);
	}


	/**OLD TEST METHOD
	 * @param enem
	 * @param damage
	 */
	public static void spawnBasicEnemyBullet(Entity enem, float damage){
		Entity bullet = new Entity(enem.getPosition());
		bullet.setScale(new Vector2f(.02f, .02f));
		bullet.setDamage(damage);
		new SimpleMovementComponent(bullet, new Vector2f(0f, -1f), 0);
		new KillOnCollisionComponent(bullet);
		attachMandatoryProjectileComponents(bullet, Util.REGULAR_POLYGONS[0], CollisionID.ENEMY_PROJECTILE, CollisionID.PLAYER, CollisionID.SHIELD);
	}

	/**OLD TEST METHOD
	 * @param projectile
	 * @param hitbox
	 * @param thisid
	 * @param ids
	 */
	public static void attachMandatoryProjectileComponents(Entity projectile,  Vector2f[] hitbox, CollisionID thisid, CollisionID... ids){
		new OffscreenRemoveComponent(projectile);
		CollisionComponent cc = new CollisionComponent(projectile, hitbox, thisid, ids);
		cc.setHitboxDraw(true);
	}


	/**Spawn a particle that is a regular n-gon, if particles are enabled. You can press "I" in-game to disable, which can help performance.
	 * @param pos The position in the world
	 * @param vel The velocity
	 * @param angularvel The rotation rate
	 * @param scale The scale 
	 * @param color The colour
	 * @param lifetime The lifetime of the particle
	 * @param n The number of points in the particle
	 */
	public static void spawnParticle(Vector2f pos, Vector2f vel, float angularvel, float scale, Color color, float lifetime, int n){
		if(Game.particlesEnabled()){
			Entity p = new Entity(pos);
			p.setScale(new Vector2f(scale, scale));		
			new SimpleMovementComponent(p, vel, angularvel);
			new SelfDestructComponent(p, lifetime);
			Vector2f[] pts;
			if(n<10 && n>2){
				pts = Util.REGULAR_POLYGONS[n-3];
			} else {
				pts = Util.getRegularPoly(n);
			}
			ConvexPolygonComponent shape = new ConvexPolygonComponent(p, pts, color, DisplayType.FILL);
			ColorOscillationComponent  c = new ColorOscillationComponent(p, color, Color.TRANSPARENT, .5f/lifetime, OscType.TRIANGLE);
			c.addComponent(shape);		
		}
	}



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
		new OffscreenRemoveComponent(proj);
		new CollisionComponent(proj, hb, CollisionID.PLAYER_PROJECTILE, CollisionID.ENEMY);
		if (firesound != null) {
			GameData.playSound(firesound);
		}
		return proj;
	}


	/**
	 * Discreet probabilities of each power up drop
	 */
	private final static float POWER_UP_PROBS[] = new float[]{.015f, .0135f, .014f, .0138f, .024f, .024f, .024f, .024f}; //heal, shot, damage, shield, machine, rocket, poison, dart. Will return -1 if nothing is spawned.

	/**Spawns a base enemy</br>
	 * Base properties include:</br>
	 * Explosion on death</br>
	 * A chance to spawn an EnemyDrop on death</br>
	 * Addition to the player score on death</br>
	 * Collision</br>
	 * A sprite</br>
	 * Game over once it reaches the bottom</br>
	 * Damage flash</br>
	 * Health bar</br>
	 * Hit sound</br> 
	 * 
	 * @param pos Position
	 * @param tex Sprite texture
	 * @param col Sprite colour
	 * @param hb Hitbox data
	 * @param hitsound Hit sound
	 * @param scale Scale
	 * @param health Health
	 * @param score Score to add on death
	 * @return A base enemy
	 */
	public static Entity createBaseEnemy(Vector2f pos, Texture tex, Color col, Vector2f[] hb, Sound hitsound, float scale, float health, int score){
		Entity enem = new Entity(pos);
		enem.setScale(new Vector2f(scale,scale));
		enem.setMaxHealth(health);;
		enem.healFully();

		new OnDeathComponent(enem){

			private int deathScore = score;

			@Override
			public void notifyAction() {
				spawnExplosionParticles(entity, .1f, .5f, entity.getSpriteColour());

				int i = Util.discreetProb(POWER_UP_PROBS);
				switch(i){
				case 0: 
					new EnemyDrop(entity.getPosition(), DropType.HEAL_UPGRADE);
					break;
				case 1: 
					new EnemyDrop(entity.getPosition(), DropType.SHOT_UPGRADE);
					break;
				case 2: 
					new EnemyDrop(entity.getPosition(), DropType.DAMAGE_UPGRADE);
					break;
				case 3: 
					new EnemyDrop(entity.getPosition(), DropType.SHIELD_UPGRADE);
					break;
				case 4:
					new EnemyDrop(entity.getPosition(), DropType.MACHINEGUN_WEAPON);
					break;
				case 5:
					new EnemyDrop(entity.getPosition(), DropType.ROCKET_WEAPON);
					break;
				case 6:
					new EnemyDrop(entity.getPosition(), DropType.POISON_WEAPON);
					break;
				case 7:
					new EnemyDrop(entity.getPosition(), DropType.DART_WEAPON);
					break;
				}
				Game.incNumberOfEnemiesKilled();
				Game.addToCurrentPlayerScore(deathScore);
			}			
		};

		SpriteComponent sprite = new SpriteComponent(enem, GameData.ENEMY_WIDTH, 12.5f, tex);
		sprite.setColor(col);
		new CollisionComponent(enem, hb, CollisionID.ENEMY, CollisionID.PLAYER_PROJECTILE, CollisionID.PLAYER);		
		new DamageFlashComponent(enem, sprite);		
		new HealthBarComponent(enem);

		new OnCollisionComponent(enem){
			@Override
			public void notifyAction() {
				if(entity.getCollidingEntities().contains(Game.getCurrentPlayer())) {
					Game.stateMachine.setCurrentState(State.GAME_OVER);
				} else {
					GameData.playSound(hitsound);
				}
			}
		};

		new NotifierComponent(enem){
			@Override
			public boolean notifyCondition() {
				return entity.getPosition().y < -1.06f;
			}
			@Override
			public void notifyAction() {
				Game.stateMachine.setCurrentState(State.GAME_OVER);
			}				
		};

		Game.incNumberOfEnemiesOnScreen();

		return enem;
	}

	/** Calculate time required to reach a point with constant speed. Speed assumes right as positive.
	 * @param start Start point
	 * @param end End point
	 * @param vel Speed of travel
	 * @return The time to get from start to end
	 */
	private static float calcTravelTime(float start, float end, float vel){
		return (end - start) / vel;
	}

	/**Spawn enemy 1. </br>
	 * This enemy moves in a square pattern and spawns in groups of 2 to 4.</br>
	 * It has low health, medium speed</br>
	 * Utilises the cycle components extensively</br>
	 * Has enemy weapon 1</br>
	 * @param pos The spawn position of the enemy closest to the screen
	 */
	public static void spawnEnemy1(Vector2f pos){
		EnemyProperties prop = LevelGen.ENEMY_SPAWN_SPECS[0].getProperties();
		int num = Util.randInRange(2, 5);	
		if(num > Game.getNumberOfEnemiesLeftToSpawn()){
			num = Game.getNumberOfEnemiesLeftToSpawn();
		}

		float vel_x = 0.2f; //moving from the left
		float spacing = -.25f; //centre to centre spacing of enemies, assumed spawned on the left
		if(pos.x > 0) //moving from the right, spawning on the right
		{
			vel_x = -vel_x;
			spacing = -spacing;
		}
		float t1,t2,t3; //times for movement t1 is first line-up, t2 line-up of regular movement, t3 down time

		t1 = calcTravelTime(pos.x, Util.sgn(-spacing) * .85f, vel_x);	
		t2 =  Util.abs((2*.85f - (num-1)*Util.abs(spacing))/vel_x); 
		t3 = 0.2f/Util.abs(vel_x);

		for(int i = 0; i < num; i++){		
			Entity enem = createBaseEnemy(new Vector2f(pos.x + i * spacing, pos.y), GameData.TEX_ENEMY_WAVE, new Color(5,36,130), GameData.HB_ENEMY_WAVE, GameData.SOUND_ENEMY_HITS[0], .08f, prop.health , 10);
			new ScaleOscComponent(enem, new Oscillator(Util.randInRange(0.9f, 1.1f), Util.randInRange(0.009f, 0.011f), 0f, Util.randInRange(-Util.PI, Util.PI), OscType.SINE), new Vector2f(1f,1f));
			new MovementOscComponent(enem, new Oscillator(Util.randInRange(0.9f, 1.1f), Util.randInRange(0.0013f, 0.016f), 0f, Util.randInRange(-Util.PI, Util.PI), OscType.SINE), new Vector2f(0f,1f));
			//new AutoFireComponent(enem, createDefaultEnemyWeapon(enem, 1f));		
			CyclingModifierComponent cycle1 = new CyclingModifierComponent(enem);	
			cycle1.addToCycle(t1, new SimpleMovementComponent(enem, new Vector2f(vel_x, 0f), 0f));
			cycle1.addToCycle(-1f); //disable		
			CyclingModifierComponent cycle2 = new CyclingModifierComponent(enem);	
			SimpleMovementComponent d = new SimpleMovementComponent(enem, new Vector2f(0f, -Util.abs(vel_x)), 0f);
			d.setEnabled(false); //must initially be disabled, until cycle 3 enables cycle2 which will enable this component
			cycle2.addToCycle(t3, d); //down
			cycle2.addToCycle(t2, new SimpleMovementComponent(enem, new Vector2f(-vel_x, 0f), 0f)); //opposite to direction
			cycle2.addToCycle(t3, new SimpleMovementComponent(enem, new Vector2f(0f, -Util.abs(vel_x)), 0f)); //down
			cycle2.addToCycle(t2, new SimpleMovementComponent(enem, new Vector2f(vel_x, 0f), 0f)); //initial direction direction		
			CyclingModifierComponent cycle3 = new CyclingModifierComponent(enem);
			cycle3.addToCycle(t1); //wait for cycle1 to complete
			cycle3.addToCycle(-1f, cycle2); //continue with cycle2 permanently			

			attatchEnemyWeapon1(enem, prop.damage);

		}
	}

	/**Spawn enemy 2. </br>
	 * This enemy moves to a random point at the bottom of the screen, oscillating in a direction perpendicular to path to end point.</br>
	 * Medium health with slow speed.
	 * @param pos The spawn position
	 */
	public static void spawnEnemy2(Vector2f pos){
		EnemyProperties prop = LevelGen.ENEMY_SPAWN_SPECS[1].getProperties();
		Entity enem = createBaseEnemy(pos, GameData.TEX_ENEMY_VIRUS, new Color(234,42,208), GameData.HB_ENEMY_VIRUS, GameData.SOUND_ENEMY_HITS[1], .1f, prop.health, 30);
		Vector2f target = new Vector2f(Util.randInRange(-.7f, .7f), Util.randInRange(-1f, -.8f));
		Vector2f vel = Vector2f.sub(target, pos);
		vel = Util.normalise(vel);
		Vector2f normal = Util.getNormal(Vector2f.ZERO, vel);
		float speed = .07f;
		new SimpleMovementComponent(enem, Vector2f.mul(vel, speed), Util.randInRange(-360f, 360f));
		new MovementOscComponent(enem, new Oscillator(Util.randInRange(0.5f, 0.8f), Util.randInRange(.07f, .12f), 0f, Util.randInRange(-Util.PI, Util.PI), OscType.SINE), normal);

	}

	/**Spawn enemy 3.</br>
	 * This enemy is very slow and moves downward in a straight line.</br>
	 * It has high health, considered to be the "tank"
	 *  
	 * @param pos The spawn position
	 * @return The enemy, which can be further augmented for more interesting motion.
	 */
	public static Entity spawnEnemy3(Vector2f pos){
		EnemyProperties prop = LevelGen.ENEMY_SPAWN_SPECS[2].getProperties();
		Entity enem = createBaseEnemy(pos, GameData.TEX_ENEMY_BEAR, new Color(100,30,10), GameData.HB_ENEMY_BEAR, GameData.SOUND_ENEMY_HITS[2], .14f, prop.health, 80);
		float speed = .053f;
		new SimpleMovementComponent(enem, new Vector2f(0f, -speed), 0f);
		new ScaleOscComponent(enem, new Oscillator(.3f, .01f, 0f, 0f, OscType.SINE), Util.unitVectorWithRotation(40f));
		return enem;
	}

	/**Spawn enemy 4. </br>
	 * This enemy moves diagonally, altering direction when it reaches the edge of the screen. It is similar to enemy 1.</br>
	 * It has enemy weapon 2</br>
	 * Medium health, medium speed
	 * @param pos The spawn position
	 */
	public static void spawnEnemy4(Vector2f pos){
		EnemyProperties prop = LevelGen.ENEMY_SPAWN_SPECS[3].getProperties();
		float vel_x = 0.2f; //moving from the left
		float vel_y = -.03f;
		if(pos.x > 0)
		 {
			vel_x = -vel_x; //moving from the right
		}
		float t1,t2; //times for movement t1 is first line-up, t2 line-up of regular movement, t3 down time
		if(vel_x > 0){ //moving right
			t1 = calcTravelTime(pos.x, .85f, vel_x);						
		} else {
			t1 = calcTravelTime(pos.x, -.85f, vel_x);	
		}
		t2 =  Util.abs(2*.85f/vel_x);

		Entity enem = createBaseEnemy(new Vector2f(pos.x, pos.y), GameData.TEX_ENEMY_SUNNY, new Color(200,220,10), GameData.HB_ENEMY_SUNNY, GameData.SOUND_ENEMY_HITS[3], .08f, prop.health, 50);
		//new AutoFireComponent(enem, createDefaultEnemyWeapon(enem, 1f));		
		CyclingModifierComponent cycle1 = new CyclingModifierComponent(enem);	
		cycle1.addToCycle(t1, new SimpleMovementComponent(enem, new Vector2f(vel_x, vel_y), 0f));
		cycle1.addToCycle(-1f); //disable		
		CyclingModifierComponent cycle2 = new CyclingModifierComponent(enem);	
		SimpleMovementComponent opp = new SimpleMovementComponent(enem, new Vector2f(-vel_x, vel_y), 0f);
		SimpleMovementComponent approach = new SimpleMovementComponent(enem, new Vector2f(vel_x, vel_y), 0f);
		opp.setEnabled(false);
		cycle2.addToCycle(t2, opp); //opposite to direction
		cycle2.addToCycle(t2, approach); //initial direction direction		
		CyclingModifierComponent cycle3 = new CyclingModifierComponent(enem);
		cycle3.addToCycle(t1); //wait for cycle1 to complete
		cycle3.addToCycle(-1f, cycle2); //continue with cycle2 permanently
		attatchEnemyWeapon2(enem,prop.damage);

	}


	/**Spawn enemy 5.</br>
	 *  This enemy is exactly like enemy 3, with added circular motion.
	 * @param pos The spawn position
	 */
	public static void spawnEnemy5(Vector2f pos){
		Entity enem = spawnEnemy3(pos);
		float freq = Util.randInRange(.2f, .5f);
		float amp = Util.randInRange(.05f, .2f);
		new MovementOscComponent(enem, new Oscillator(freq, amp, 0f, 0f, OscType.SINE), new Vector2f(1f,0f));
		new MovementOscComponent(enem, new Oscillator(freq, amp, 0f, Util.PI/2, OscType.SINE), new Vector2f(0f,1f));

	}


	/**Spawn enemy 6.</br>
	 *  This enemy moves in a straight line downwards.</br>
	 *  It is fast moving with medium health.</br>
	 *  Considered a very high threat (saying this with experience).</br>
	 *  Has a particle trail.
	 * @param pos Position
	 */
	public static void spawnEnemy6(Vector2f pos){
		EnemyProperties prop = LevelGen.ENEMY_SPAWN_SPECS[5].getProperties();
		Entity enem = createBaseEnemy(new Vector2f(pos.x, pos.y), GameData.TEX_ENEMY_BAT, new Color(20,20,20), GameData.HB_ENEMY_BAT, GameData.SOUND_ENEMY_HITS[4], .065f, prop.health, 100);
		float vel_y = -.2f;
		new SimpleMovementComponent(enem, new Vector2f(0f, vel_y), 0f);
		ParticleTrailComponent trail = new ParticleTrailComponent(enem, 0f, .5f, 10f,  new Color(20,20,20), 2, 1f);
		trail.setColorVary(80);
		trail.setRandomVel(.1f);
		trail.setScaleDamp(1f);
		trail.setScaleClamp(.008f);
	}


	/**Spawn the machine gun's bullet. </br>
	 * Dies on collision</br>
	 * Fast moving, for rapid fire	 * 
	 * @param player The firing player
	 * @param pos The position
	 * @param damage The damage
	 */
	public static void spawnMachineGunBullet(Entity player, Vector2f pos, float damage){
		Vector2f facing = Util.facing(player);
		Entity bullet = createBasePlayerProjectile(player, pos, GameData.TEX_BULLET, GameData.HB_BULLET, .03f, null, damage, Color.BLACK);
		new KillOnCollisionComponent(bullet);
		new SimpleMovementComponent(bullet, Vector2f.mul(facing, 2.1f), 0);
		spawnGunPowder(bullet.getPosition(), facing, 3f, Color.YELLOW);
		((Player)player).applyKnockBack(.2f, .1f);

	}

	/**Spawn the poison gun's blob (probably the coolest and most developed object in the game, for whatever reason)</br>
	 * Dies on collision</br>
	 * Spawns a poison cloud on death</br>
	 * Slow moving</br>
	 * @param player The firing player
	 * @param pos The position
	 * @param damage The damage
	 */
	public static void spawnPoisonBlob(Entity player, Vector2f pos, float damage){
		Vector2f facing = Util.facing(player);
		Entity blob = createBasePlayerProjectile(player, pos, GameData.TEX_POISON, GameData.HB_POISON, .04f, null, damage, new Color(30,150,70,180));
		new KillOnCollisionComponent(blob);
		new OnCollisionComponent(blob){
			@Override
			public void notifyAction() {
				//System.out.println(entity.getPosition());
				new PoisonCloud(entity.getPosition(), .2f, entity.getDamage());
			}};
			new SimpleMovementComponent(blob, Vector2f.mul(facing, 1.1f), Util.randInRange(-300f, 300f));
			new ScaleOscComponent(blob, new Oscillator(Util.randInRange(1f, 2f), .01f, 0f, 0f, OscType.SINE), Util.unitVectorWithRotation(45f));
			spawnGunPowder(blob.getPosition(), facing, 5f,new Color(30,150,70,180));
			((Player)player).applyKnockBack(.15f, .1f);
	}

	/**Spawn an explosion that also spawns an entity with a hitbox that exisits and does damage for a single frame.
	 * @param src The entity this explosion is sourced from
	 */
	public static void spawnDamagingExplosion(Entity src){
		Entity expl = new Entity(src.getPosition());
		float r = .3f;
		expl.setScale(new Vector2f(r,r));
		expl.setDamage(src.getDamage());

		new CollisionComponent(expl, Util.REGULAR_POLYGONS[7], CollisionID.PLAYER_PROJECTILE, CollisionID.ENEMY);
		new SelfDestructComponent(expl, .0001f);

		spawnExplosionParticles(src, r/4f, 1f, new Color(200,200,70, 200));
	}

	/**Spawn explosion particles
	 * @param src The entity the explosions is sourced from
 	 * @param spread The spread
	 * @param speedDamp The particle damping speed
	 * @param color The main colour
	 */
	public static void spawnExplosionParticles(Entity src,float spread, float speedDamp, Color color){		
		int numparticles = Util.randInRange(20, 35);

		for(int i = 0; i < numparticles; i++){
			Vector2f pos = Vector2f.add(src.position, Vector2f.mul(Util.randomUnitVector(0, 360f), Util.randInRange(0f, spread)));
			Vector2f vel = Vector2f.mul(Util.randomUnitVector(0, 360f), speedDamp*Util.randInRange(-1f, 1f));
			float angular = Util.randInRange(-1000f, 1000f);
			spawnParticle(pos, vel, angular, Util.randInRange(.007f, .015f), color, Util.randInRange(.4f, .8f), 3);
		}
	}

	/**Spawn a shield
	 * @param p The player spawning
	 * @param health The shield's health
	 */
	public static void spawnShield(Player p, float health){
		Shield s = new Shield(p);
		s.setMaxHealth(health);
		s.healFully();
	}

	/**Spawn a dart for the dart gun weapon</br>
	 * Does piercing damage each frame
	 * @param player The firing player
	 * @param pos The position
	 * @param damage The damage
	 */
	public static void spawnDart(Entity player,Vector2f pos, float damage){
		Vector2f facing = Util.facing(player);
		Entity slug = createBasePlayerProjectile(player, pos, GameData.TEX_DART, GameData.HB_DART, .04f, null, damage, Color.BLACK);
		new SimpleMovementComponent(slug, Vector2f.mul(facing, 1.5f), 0);
		spawnGunPowder(slug.getPosition(), facing, 6f, Color.CYAN);
		((Player)player).applyKnockBack(.3f, .1f);
	}

	/**Spawn a rocket for the rocket launcher weapon</br>
	 * Has an acceleration instead of velocity</br>
	 * Spawns an explosion on death that damages nearby enemies</br>
	 * Also damages the colliding enemy</br>
	 * @param player The firing player
	 * @param pos The position
	 * @param damage The damage
	 */
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

	/**OLD TEST METHOD
	 * @param pos
	 * @param vel
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

	/**Spawns gunpowder effect when a weapon is fired.
	 * @param pos Position
	 * @param generalDir General firing direction
	 * @param magnitude Magnitude of the effect
	 * @param color Main colour of the effect
	 */
	public static void spawnGunPowder(Vector2f pos, Vector2f generalDir, float magnitude, Color color ){
		int amount = (int)(magnitude*3.5f);
		for(int i = 0; i < amount; i++){
			spawnParticle(pos, Vector2f.mul(Util.varyVector(generalDir, Util.PI/1.4f), Util.randInRange(.06f*magnitude, .08f*magnitude)), Util.randInRange(-400f, 400f), Util.randInRange(0.004f, 0.008f), color, Util.randInRange(.1f, .4f), 3);
		}
	}

	/**Spawn a number that consists of number sprites
	 * @param left_centre The leftmost digit's centre
	 * @param scale The scale of the sprites
	 * @param num The number to convert to sprites
	 */
	public static void spawnNumber(Vector2f left_centre, float scale, int num, Color c){
		Vector<Integer> numbers = new Vector<>();
		if(num == 0){
			numbers.add(0);
		} else{
			int mod = 1000000000; //try for max decimal digit for the max value of an int
			boolean encountered_digit = false;
			while( mod >= 1){
				int dec_place = num/mod;
				if(dec_place != 0){ //detect when the first digit is encountered
					encountered_digit = true;
				}
				if(encountered_digit){
					numbers.add(dec_place);
				}
				num = num - dec_place*mod;
				mod /= 10;
			}
		}

		for(int i = 0; i < numbers.size(); i++){
			Entity n = new Entity(new Vector2f(left_centre.x + i*( scale*1.2f), left_centre.y));
			n.setScale(new Vector2f(scale,scale));
			new SpriteComponent(n, GameData.NUMBER_WIDTH, 0f, GameData.TEX_NUMBERS_TEXT[numbers.get(i)]).setColor(c);;

		}

	}

	//OLD TEST CODE
	//	/**Target practise and testing
	//	 * @param pos The position in the world
	//	 */
	//	public static void spawnTestEnemy(Vector2f pos){
	//		Entity enem = new Entity(pos);
	//		enem.setScale(new Vector2f(.1f,.1f));
	//		enem.setMaxHealth(4f);
	//		enem.healFully();
	//		CollisionComponent cc = new CollisionComponent(enem, GameData.HB_BENNY, CollisionID.ENEMY, CollisionID.PLAYER_PROJECTILE);
	//		new HealthBarComponent(enem);
	//		CyclingModifierComponent cycle = new CyclingModifierComponent(enem);
	//		new SimpleMovementComponent(enem, new Vector2f(0f, -.1f), 0f);
	//		cycle.addToCycle( .5f/.3f,new MovementOscComponent(enem, new Oscillator(.3f, .4f, 0f, 0f, OscType.SINE), new Vector2f(.1f,.4f)));
	//		cycle.addToCycle(.5f, new SimpleMovementComponent(enem, new Vector2f(.8f, -.5f), 0f));
	//		cycle.addToCycle(.5f, new SimpleMovementComponent(enem, new Vector2f(-.8f, .5f), -720f));
	//		cycle.addToCycle(-1f);		
	//		cycle.addToCycle(1f, new SimpleMovementComponent(enem, Vector2f.ZERO, 360f));
	//		cycle.addToCycle(.5f);
	//		cycle.addToCycle(1f, new SimpleMovementComponent(enem, Vector2f.ZERO, -360f));
	//		cycle.addToCycle(.5f);
	//		new MovementOscComponent(enem, new Oscillator(1f, .03f, 0f, Util.randInRange(0f, 2*Util.PI), OscType.SINE), new Vector2f(0f,1f));		
	//		SpriteComponent sprc = new SpriteComponent(enem, 64, 13f, GameData.TEX_BENNY_THE_FEESH);	
	//		sprc.setColor(Color.BLACK);
	//
	//		new AutoFireComponent(enem, createDefaultEnemyWeapon(enem, 1f));
	//		new CollisionSoundComponent(enem, GameData.SOUND_PEEG);
	//		new DamageFlashComponent(enem, sprc);
	//		new ScaleOscComponent(enem, new Oscillator(Util.randInRange(1f, 2f), .02f, 0f, 0f, OscType.TRIANGLE), new Vector2f(1f,1f));
	//	}

	//	/** Spawn a gunpowder effect used when shooting and projectile collisions
	//	 * @param pos The position in the world
	//	 * @param generalDir The general direction of the gunpowder
	//	 * @param magnitude The magnitude of the explosion
	//	 * @param color The main colour of the explosion
	//	 */

	//
	//	public static void spawnBuckShot(Entity shooter, float vel, float degreespread, float damage, int amount){
	//		Vector2f unitdir = Util.facing(shooter);
	//		Vector2f pos = Vector2f.add(Util.approxParticleOffset(unitdir, shooter), shooter.position);
	//		spawnGunPowder(pos, unitdir, 6f, Color.BLACK);
	//
	//		for(int i = 0; i < amount; i++){		
	//			Entity shot = new Entity(pos);
	//			shot.setDamage(damage);
	//			new SimpleMovementComponent(shot, Vector2f.mul(Util.varyVector(unitdir,  Util.toRad(degreespread)), vel), damage);
	//			CollisionComponent cc = new CollisionComponent(shot, Util.REGULAR_POLYGONS[0], CollisionID.PLAYER_PROJECTILE, CollisionID.ENEMY);
	//			new OnCollisionComponent(shot){
	//				@Override
	//				public void notifyAction() {
	//					entity.kill();			
	//				}			
	//			};
	//			//cc.setHitboxDraw(true);
	//			new SpriteComponent(shot, 64, 0f, GameData.TEX_BUCKSHOT);
	//			new OffscreenKillComponent(shot);
	//			shot.setScale(new Vector2f(.015f,.015f));
	//		}		
	//	}


}
