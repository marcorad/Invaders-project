package engine.entity;

import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundSource.Status;
import org.jsfml.system.Vector2f;

import util.Util;

/**A class that soecifies the behaviour of a weapon
 * @author Marco
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
	 */
	public Weapon(float damage, float reload, WeaponID ID, Entity p){
		this.damage = damage;
		reloadtime = reload;
		timeSinceReload = reload;
		this.ID = ID;
		this.shooter = p;
	}

	public void setSound(Sound s){
		this.sound = s;
	}



	/**
	 * Increase the number of shots the weapon fires by 1. Max of 5 shots.
	 * This also reduces the damage done by 25%, since two shots are now fired.
	 */
	public void addShot(){
		if (numShots <= 5){
			damage *= .75f;
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

	public float getDamage() {
		return damage;
	}

	public float getReloadtime() {
		return reloadtime;
	}



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
					if(sound.getStatus() != Status.PLAYING){
						sound.play();
					}
				}
			}
		} else{
			timeSinceReload = Util.clamp(timeSinceReload, 0f, reloadtime);
		}
	}

	public abstract void spawnProjectiles(Vector2f pos);

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




	public static Vector2f[] getSpawnLocations(Entity p, int n, float degreeSeperate){
		Vector2f[] locs = new Vector2f[n];
		Vector2f maindir = Util.approxParticleOffset(Util.facing(p), p);
		float angle;

		if(n%2 == 1) {
			int k = (n-1)/2;
			angle = -k*degreeSeperate;
		} else {
			int k = (n)/2;
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
