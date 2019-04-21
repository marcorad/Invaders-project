package engine.entity;

import org.jsfml.audio.Sound;
import org.jsfml.system.Vector2f;

import game.GameData;
import util.Util;

/**
 * Specifies the behaviour of a weapon. It has a damage, and a reload time. It has a fire enable, which allows for auto fire.
 *
 */
public abstract class Weapon {

	private Sound sound;


	/**Identifies a weapon type
	 * @author Marco
	 *
	 */
	public enum WeaponID{
		POISON, DARTGUN, MACHINEGUN, ROCKET, ENEMY_WEAPON;
	}

	public final WeaponID ID;
	protected float damage, reloadtime, timeSinceReload;
	protected int numShots = 1;
	public static final float MINIMUM_RELOAD_TIME = .2f;
	private boolean firing = false;
	protected Entity shooter;

	/**
	 * @param damage The damage per projectile
	 * @param reload The time between shots while this weapon is firing
	 * @param ID This weapon's ID
	 * @param p The shooter
	 */
	public Weapon(float damage, float reload, WeaponID ID, Entity p){
		this.damage = damage;
		reloadtime = reload;
		timeSinceReload = reload;
		this.ID = ID;
		this.shooter = p;
	}
	
	public Weapon(float damage, float reload, WeaponID ID, Entity p, Sound s){
		this.damage = damage;
		reloadtime = reload;
		timeSinceReload = reload;
		this.ID = ID;
		this.shooter = p;
		sound = s;
	}

	/**Set the fire sound
	 * @param s The sound
	 */
	public void setSound(Sound s){
		this.sound = s;
	}

	/**
	 * Increase the number of shots the weapon fires by 1. Max of 5 shots.
	 * This also reduces the damage done by 30%, since two shots are now fired.
	 */
	public void addShot(){
		if (numShots <= 5){
			damage *= .7f;
			numShots++;
		}
	}

	/**Increase the damage per projectile
	 * @param amount The increase amount
	 */
	public void increaseDamage(float amount){
		damage += amount;
	}

	/**Decrease the reload time by a certain amount, capped at the minimum reload time
	 * @param amount Amount of time to decrease by
	 */
	public void decreasedReloadTime(float amount){
		reloadtime -= amount;
		if(reloadtime < MINIMUM_RELOAD_TIME){
			reloadtime = MINIMUM_RELOAD_TIME;
		}
	}	

	/**
	 * @return The weapon damage
	 */
	public float getDamage() {
		return damage;
	}

	/**
	 * @return Time it takes to reload
	 */
	public float getReloadtime() {
		return reloadtime;
	}

	/**Update this weapon
	 * @param dt Change in time
	 */
	public void update(float dt){
		timeSinceReload += dt;
		if(firing){			
			if(timeSinceReload >= reloadtime){
				timeSinceReload -= reloadtime;

				Vector2f locs[] = getSpawnLocations(shooter, numShots, 120f/numShots);

				for(int i = 0; i < numShots; i++){				
					spawnProjectiles(locs[i]);
				}


				if(sound != null){
					GameData.playSound(sound);
				}
			}
		} else{
			timeSinceReload = Util.clamp(timeSinceReload, 0f, reloadtime);
		}
	}

	/** Spawn the a projectile at the given position, determined by the number of shots
	 * @param pos The position
	 */
	public abstract void spawnProjectiles(Vector2f pos);

	/**Allow this weapon to start firing
	 * @param fire Whether it fires
	 */
	public void setFiring(boolean fire){
		firing = fire;
	}

	/**
	 * @return The that has passed since last reload
	 */
	public float getTimeSinceLastReload(){
		return timeSinceReload;
	}

	/**
	 * @return The time it takes to reload
	 */
	public float getReloadTime(){
		return reloadtime;
	}

	/**
	 * @return Whether this weapon is currently reloading
	 */
	public boolean isReloading(){
		return timeSinceReload < reloadtime;
	}
	
	/**
	 * Randomise this time since this weapon has last reloaded. This allows for more chaotic enemy fire patterns.
	 */
	public void randomiseTimeOffset(){
		timeSinceReload = Util.randInRange(0f, reloadtime);
	}


	/** Get the spawn locations for the fired projetiles based of the number of shots for a weapon
	 * @param p The firing entity
	 * @param n The number of shots
	 * @param degreeSeperate The seperation of the shots in degrees
	 * @return The loactions
	 */
	public static Vector2f[] getSpawnLocations(Entity p, int n, float degreeSeperate){
		Vector2f[] locs = new Vector2f[n];
		Vector2f maindir = Util.approxSpawnOffset(Util.facing(p), p);
		float angle;

		if(n%2 == 1) {
			int k = (n-1)/2;
			angle = -k*degreeSeperate;
		} else {
			int k = n/2;
			angle = -k*degreeSeperate+degreeSeperate/2f;
		}


		for(int i = 0; i < n; i++){
			locs[i] = Util.rotateVector(maindir, angle);
			locs[i] = Vector2f.add(p.getPosition(), locs[i]);
			angle+=degreeSeperate;
		}

		return locs;
	}


}
