package engine.entity;

import java.util.Vector;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.event.MouseWheelEvent;

import engine.component.CollisionComponent;
import engine.component.CollisionID;
import engine.component.ComplexMovementComponent;
import engine.component.KeyboardMoveComponent;
import engine.component.MouseMoveControlComponent;
import engine.component.OnCollisionComponent;
import engine.component.OnDeathComponent;
import engine.component.ParticleTrailComponent;
import engine.component.SpriteComponent;
import engine.entity.Weapon.WeaponID;
import engine.gui.Bar;
import game.Game;
import game.GameData;
import state.StateMachine.State;
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


	public final Weapon DARTGUN = new Weapon(.8f, .8f, WeaponID.DARTGUN, this, GameData.SOUND_DART_GUN){
		@Override
		public void spawnProjectiles(Vector2f pos) {
			SpawnFactory.spawnDart(this.shooter, pos, this.damage);
		}

	};

	public final Weapon MACHINEGUN = new Weapon(1.2f, .2f, WeaponID.MACHINEGUN, this, GameData.SOUND_MACHINE_GUN){
		@Override
		public void spawnProjectiles(Vector2f pos) {
			SpawnFactory.spawnMachineGunBullet(shooter, pos, damage);
		}		

	};

	public final Weapon ROCKET = new Weapon(3.5f, 1.2f, WeaponID.ROCKET, this, GameData.SOUND_ROCKET){
		@Override
		public void spawnProjectiles(Vector2f pos) {			
			SpawnFactory.spawnRocket(shooter, pos, damage);
		}
	};


	public final Weapon POISON = new Weapon(.05f, 1f, WeaponID.POISON, this, GameData.SOUND_POISON_GUN){
		@Override
		public void spawnProjectiles(Vector2f pos) {			
			SpawnFactory.spawnPoisonBlob(shooter, pos, damage);
		}
	};

	private Vector<Weapon> heldWeapons;
	private Vector<WeaponIcon> weaponIcons;

	protected static Vector2f currentmouse = Vector2f.ZERO;

	private ComplexMovementComponent movement;
	private SpriteComponent sprite;
	private CollisionComponent collision;
	private KeyboardMoveComponent keys;
	private MouseMoveControlComponent mouse;
	private float accelmag = 5.5f;
	private boolean mousePressed = false;

	private float maxvel = 1f;

	private Weapon currentWeapon;
	private int currentWeaponIndex = 0;

	/**Construct a player at a certain position
	 * @param position
	 */
	public Player(Vector2f position) {	
		super(position);
		create();
		this.setScale(new Vector2f(.05f,.05f));		
	}

	public void switchWeapon(int numIncrement){
		weaponIcons.elementAt(currentWeaponIndex).setSelected(false);
		currentWeaponIndex += numIncrement;		
		if(currentWeaponIndex < 0){
			currentWeaponIndex = heldWeapons.size() - 1;
		} else {
			currentWeaponIndex %= heldWeapons.size();
		}
		currentWeapon = heldWeapons.elementAt(currentWeaponIndex);
		weaponIcons.elementAt(currentWeaponIndex).setSelected(true);
	}

	/**Increases the current weapon damage by adding a fraction of its current damage to the damage
	 * @param factor The fraction added to the damage
	 */
	public void UpgradeCurrentWeaponDamage(float factor){
		currentWeapon.increaseDamage(currentWeapon.getDamage()*factor);
	}

	/**Increases the current weapon fire rate by subtracting a fraction of its current reload time from the reload time
	 * @param factor The fraction subtracted from the reload time
	 */
	public void UpgradeCurrentWeaponFireRate(float factor){
		currentWeapon.decreasedReloadTime(currentWeapon.getReloadtime()*factor);
	}


	/**Gives the player a weapon. If the player already has the weapon, then add an extra shot to that weapon.
	 * @param w
	 */
	public void addWeapon(Weapon w){
		if(!heldWeapons.contains(w)){		
			Vector2f pos = new Vector2f(.27f+heldWeapons.size()*.2f,-0.912f);
			heldWeapons.add(w);		
			Texture tex = null;

			if(w.ID == WeaponID.POISON){
				tex = GameData.TEX_POISON_ICON;
			} else if(w.ID == WeaponID.MACHINEGUN){
				tex = GameData.TEX_MACHINEGUN_ICON;
			} else if(w.ID == WeaponID.ROCKET){
				tex = GameData.TEX_ROCKET_ICON;
			} else if(w.ID == WeaponID.DARTGUN){
				tex = GameData.TEX_DARTGUN_ICON;
			}		
			weaponIcons.add(new WeaponIcon(pos, tex));	
		} else {
			w.addShot();
		}
	}

	/**Heal the player by a specific amount.
	 * @param h The amount to heal
	 */
	public void heal(float h){
		setHealth(this.health + h);
	}


	/**
	 * Adds an extra shot to the current weapon held. Max of 5 shots at once.
	 */
	public void UpgradeCurrentWeaponShot(){		
		currentWeapon.addShot();
	}

	private void create(){

		this.setMaxHealth(10f);
		this.healFully();

		heldWeapons = new Vector<>();
		weaponIcons = new Vector<>();
		addWeapon(MACHINEGUN); //default start
		switchWeapon(0);		

		GUI_Health = new Bar(.75f, .06f, new Color(255,0,0,150), new Color(0,255,0,150), .01f);
		GUI_Health.setPosition(new Vector2f(-.6f, -.866f));
		GUI_Health.setAsGUIElement(true);

		GUI_Shield = new Bar(.75f, .06f, new Color(255,255,0,150), new Color(0,255,255,150), .01f);
		GUI_Shield.setPosition(new Vector2f(-.6f, -.95f));
		GUI_Shield.setAsGUIElement(true);

		movement = new ComplexMovementComponent(this, Vector2f.ZERO, Vector2f.ZERO, 0f, 0f);
		movement.setSpeedClamp(maxvel);
		this.setMinPosition(new Vector2f(-.9f, -.9f));
		this.setMaxPosition(new Vector2f(.9f, -.55f));	
		//currentWeapon.addShot();
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
			public void special1Pressed() {
				//switchWeapon(-1);
			}

			@Override
			public void special1Released() {}

			@Override
			public void special2Pressed() {
				//switchWeapon(1);
			}

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
					GameData.playSound(GameData.SOUND_SHIELD);
				}
			}

			@Override
			public void onLeftMousePress(Vector2f worldpos) {
				mousePressed = true;
			}

			@Override
			public void onRightMouseRelease(Vector2f worldpos) {
			}

			@Override
			public void onLeftMouseRelease(Vector2f worldpos) {
				//System.out.println("RELEASED");
				mousePressed = false;

			}

			@Override
			public void onMouseMove(Vector2f worldpos) {
				currentmouse = worldpos;				
			}

			@Override
			public void onMouseWheelMoved(MouseWheelEvent mwe) {
				if(mwe.delta > 0){
					switchWeapon(-1);
				} else if (mwe.delta < 0){
					switchWeapon(1);
				}
			}
		};

		ParticleTrailComponent trail = new ParticleTrailComponent(this, .5f, .4f, 30f, new Color(255, 50, 50, 255), 2, .5f);
		sprite = new SpriteComponent(this, 128, 12f, GameData.TEX_PLAYER);

		trail.setColorVary(50);
		trail.setRandomVel(.35f);

		collision = new CollisionComponent(this, GameData.HB_PLAYER, CollisionID.PLAYER, CollisionID.ENEMY_PROJECTILE, CollisionID.ENEMY);

		new OnDeathComponent(this){
			@Override
			public void notifyAction() {
				Game.stateMachine.setCurrentState(State.GAME_OVER);				
			}
		};

		new OnCollisionComponent(this){
			@Override
			public void notifyAction() {					
				Game.graphics.damageTakenEffect();
				GameData.playSound(GameData.SOUND_PLAYER_HIT);
			}
		};
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
		if(reloadbar != null) {
			reloadbar.setScale(Vector2f.mul(scale, 1.05f));
		}
	}

	@Override
	public void setPosition(Vector2f position) {
		super.setPosition(position);
		if(reloadbar != null) {
			reloadbar.setPosition(Vector2f.add(this.position, new Vector2f(0f, this.getScale().y + 0.02f)));
		}
	}

	public void applyKnockBack(float speed, float time){
		movement.applyKnockback(Vector2f.mul(Util.facing(this),-speed), time);
	}

	/**Upgrade the shield recharge rate by multiplying it by a factor.
	 * @param factor The factor to multiply with.
	 */
	public void upgradeShieldRecharge(float factor){
		shieldRechargeRate *= factor; 
	}




}
