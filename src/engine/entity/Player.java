package engine.entity;

import org.jsfml.graphics.Color;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.event.MouseWheelEvent;

import engine.component.CollisionComponent;
import engine.component.ComplexMovementComponent;
import engine.component.Component;
import engine.component.ConvexPolygonComponent;
import engine.component.HealthBarComponent;
import engine.component.KeyboardMoveComponent;
import engine.component.MouseMoveControlComponent;
import engine.component.MovementComponent;
import engine.component.ParticleTrailComponent;
import engine.component.SpriteComponent;
import engine.component.UpdateableComponent;
import engine.entity.Weapon.WeaponID;
import engine.gui.Bar;
import game.GameData;
import util.Util;

/**A class that specifically describes a player entity, since its behaviour is complex.
 * @author Marco
 *
 */
public class Player extends Entity {

	private Bar reloadbar;

	private Bar GUI_Health, GUI_Shield;

	private float shieldrecharge = 10f;
	private float shieldRechargeMax = 10f;
	private float shieldRechargeRate = 4f;


	public final Weapon SHOTGUN = new Weapon(.5f, 1f, WeaponID.SHOTGUN, this){
		@Override
		public void spawnProjectiles(Vector2f pos) {
			Player p = (Player)shooter;
			p.applyKnockBack(1.5f, .07f);
			SpawnFactory.spawnBuckShot(shooter, 4f, 8f, .1f, 6*this.numShots);			
		}
	};

	public final Weapon RAILGUN = new Weapon(.4f, 1.4f, WeaponID.RAILGUN, this){
		@Override
		public void spawnProjectiles(Vector2f pos) {
			SpawnFactory.spawnRailSlug(this.shooter, pos, this.damage);
		}

	};

	public final Weapon MACHINEGUN = new Weapon(.8f, .2f, WeaponID.SHOTGUN, this){
		@Override
		public void spawnProjectiles(Vector2f pos) {
			SpawnFactory.spawnMachineGunBullet(shooter, pos, damage);
		}		

	};

	public final Weapon ROCKET = new Weapon(1f, 1f, WeaponID.SHOTGUN, this){
		@Override
		public void spawnProjectiles(Vector2f pos) {			
			SpawnFactory.spawnRocket(shooter, pos, damage);
		}
	};

	protected static Vector2f currentmouse = Vector2f.ZERO;

	private ComplexMovementComponent movement;
	private SpriteComponent sprite;
	private CollisionComponent collision;
	private ConvexPolygonComponent hitbox;
	private KeyboardMoveComponent keys;
	private MouseMoveControlComponent mouse;
	private float accelmag = 4.5f;
	private boolean mousePressed = false;

	private float maxvel = 1f;

	private Weapon currentWeapon = ROCKET;

	/**Construct a player at a certain position
	 * @param position
	 */
	public Player(Vector2f position) {	
		super(position);
		create();
		this.setScale(new Vector2f(.05f,.05f));		
	}

	private void create(){

		GUI_Health = new Bar(.75f, .06f, new Color(255,0,0,100), new Color(0,255,0,100), .01f);
		GUI_Health.setPosition(new Vector2f(-.6f, -.866f));
		GUI_Health.setAsGUIElement(true);

		GUI_Shield = new Bar(.75f, .06f, new Color(255,255,0,100), new Color(0,255,255,100), .01f);
		GUI_Shield.setPosition(new Vector2f(-.6f, -.95f));
		GUI_Shield.setAsGUIElement(true);

		movement = new ComplexMovementComponent(this, Vector2f.ZERO, Vector2f.ZERO, 0f, 0f);
		movement.setSpeedClamp(maxvel);
		this.setMinPosition(new Vector2f(-.75f, -.81f));
		this.setMaxPosition(new Vector2f(.64f, -.5f));	
		currentWeapon.addShot();
		//currentWeapon.addShot();
		//currentWeapon.addShot();

		reloadbar = new Bar(1f, .3f, new Color(255, 30, 100, 200), new Color(0, 30, 255, 200));
		reloadbar.setFrameColor(new Color(0, 30, 200, 200));

		keys = new KeyboardMoveComponent(this,Key.W, Key.S, Key.A, Key.D, null,null,movement){
			@Override
			public void onDirection(Vector2f dir) {
				ComplexMovementComponent c = (ComplexMovementComponent)movement;
				c.setAccel(Vector2f.mul(dir, accelmag));
				if(dir.x == 0f){
					c.decelerateX(accelmag);
				}
				if(dir.y == 0f){
					c.decelerateY(accelmag);
				}
			}

			@Override
			public void special1Pressed() {}

			@Override
			public void special1Released() {}

			@Override
			public void special2Pressed() {}

			@Override
			public void special2Released() {}

		};	

		//hitbox = new ConvexPolygonComponent(this, new Vector2f[]{new Vector2f(0f,.9f), new Vector2f(.8f,.65f), new Vector2f(.8f,-1f), new Vector2f(-.8f,-1f), new Vector2f(-.8f,.65f)});
		//hitbox.setColor(Color.RED);

		mouse = new MouseMoveControlComponent(this){
			@Override
			public void onRightMousePress(Vector2f worldpos) {
				if(shieldrecharge == shieldRechargeMax){
					SpawnFactory.spawnShield((Player)this.entity, 10f);
					shieldrecharge = 0;
				}
			}

			@Override
			public void onLeftMousePress(Vector2f worldpos) {
				mousePressed = true;
			}

			@Override
			public void onRightMouseRelease(Vector2f worldpos) {
				System.out.println("RIGHT MOUSE RELEASE: " + worldpos);
			}

			@Override
			public void onLeftMouseRelease(Vector2f worldpos) {
				mousePressed = false;

			}

			@Override
			public void onMouseMove(Vector2f worldpos) {
				currentmouse = worldpos;				
			}

		};

		ParticleTrailComponent trail = new ParticleTrailComponent(this, .5f, .4f, 30f, new Color(255, 50, 50, 255), 2, .5f);
		sprite = new SpriteComponent(this, 128, 12f, GameData.TEX_PLAYER);

		trail.setColorVary(50);
		trail.setRandomVel(.35f);
	}




	@Override
	public void setTheta(float theta) {
		//System.out.println(theta);
		if(theta > -270f && theta < -180f){
			theta = 90f;
		} else if(theta > -180f && theta < -90f){
			theta = -90f;
		}
		super.setTheta(theta);

	}

	@Override
	public void update(float dt, float t){
		shieldrecharge += shieldRechargeRate * dt;
		shieldrecharge = Util.clamp(shieldrecharge, 0f, shieldRechargeMax);
		Util.pointEntityInDirection(this,Vector2f.sub(currentmouse, position));
		currentWeapon.setFiring(mousePressed);
		currentWeapon.update(dt);		
		super.update(dt, t);
	}

	@Override
	public void draw() {
		super.draw();
		if(currentWeapon.isReloading()){
			reloadbar.draw(currentWeapon.getTimeSinceLastReload(), currentWeapon.getReloadTime());
		}
		GUI_Health.draw(this.health, this.maxHealth);
		GUI_Shield.draw(shieldrecharge, shieldRechargeMax);
	}

	@Override
	public void setScale(Vector2f scale) {
		super.setScale(scale);
		if(reloadbar != null)reloadbar.setScale(Vector2f.mul(scale, 1.05f));
	}

	@Override
	public void setPosition(Vector2f position) {
		super.setPosition(position);
		if(reloadbar != null)reloadbar.setPosition(Vector2f.add(this.position, new Vector2f(0f, this.getScale().y + 0.02f)));
	}

	public void applyKnockBack(float speed, float time){
		movement.applyKnockback(Vector2f.mul(Util.facing(this),-speed), time);
	}




}
