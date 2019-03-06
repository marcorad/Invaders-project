package engine.entity;

import util.Util;

/**A class that soecifies the behaviour of a weapon
 * @author Marco
 *
 */
public abstract class Weapon {



	/**Identifies a weapon type
	 * @author Marco
	 *
	 */
	public enum WeaponID{
		SHOTGUN, RAILGUN, MACHINEGUN, ROCKET;
	}

	public final WeaponID ID;
	protected float damage, reloadtime, timeSinceReload;
	protected int numShots = 1;
	public static final float MINIMUM_RELOAD_TIME = .2f;
	private boolean firing = false;
	protected Player player;

	/**
	 * @param damage The damage per projectile
	 * @param reload The time between shots while this weapon is firing
	 * @param ID This weapon's ID
	 */
	public Weapon(float damage, float reload, WeaponID ID, Player p){
		this.damage = damage;
		reloadtime = reload;
		timeSinceReload = reload;
		this.ID = ID;
		this.player = p;
	}

	/**
	 * Increase the number of shots the weapon fires by 1
	 */
	public void addShot(){
		numShots++;
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

	public void update(float dt){
		timeSinceReload += dt;
		if(firing){			
			if(timeSinceReload >= reloadtime){
				timeSinceReload -= reloadtime;
				spawnProjectiles();
			}
		} else{
			timeSinceReload = Util.clamp(timeSinceReload, 0f, reloadtime);
		}
	}

	public abstract void spawnProjectiles();

	public void setFiring(boolean fire){
		firing = fire;
	}
	
	public float getTimeSinceLastReload(){
		return timeSinceReload;
	}
	
	public float getReloadTime(){
		return reloadtime;
	}
	
	public boolean isReloading(){
		return (timeSinceReload < reloadtime);
	}


}
