package engine.entity;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;

import engine.component.CollisionComponent;
import engine.component.CollisionID;
import engine.component.KillOnCollisionComponent;
import engine.component.OffscreenRemoveComponent;
import engine.component.OnCollisionComponent;
import engine.component.ScaleOscComponent;
import engine.component.SimpleMovementComponent;
import engine.component.SpriteComponent;
import game.Game;
import game.GameData;
import util.Oscillator;
import util.Oscillator.OscType;

/**
 * An entity dropped by an enemy on death. May be a power-up or a weapon.
 */
public class EnemyDrop extends Entity {

	/**
	 * The type of drop.
	 */
	public enum DropType{
		HEAL_UPGRADE, SHOT_UPGRADE, DAMAGE_UPGRADE, SHIELD_UPGRADE,
		DART_WEAPON, POISON_WEAPON, ROCKET_WEAPON, MACHINEGUN_WEAPON;
	}

	private DropType type;

	/**
	 * @param position The position of the drop
	 * @param type The type of drop
	 */
	public EnemyDrop(Vector2f position, DropType type) {
		super(position);
		this.type = type;
		new CollisionComponent(this, GameData.HB_POWERUP, CollisionID.POWER_UP, CollisionID.PLAYER);
		new OnCollisionComponent(this){

			@Override
			public void notifyAction() {
				Player p = Game.getCurrentPlayer();

				switch(((EnemyDrop)entity).getType()){
				case DAMAGE_UPGRADE: p.UpgradeCurrentWeaponDamage(0.2f); //add .3 of current damage to the current weapon
				break;
				case HEAL_UPGRADE: p.heal(1.5f);
				break;
				case SHIELD_UPGRADE: p.upgradeShieldRecharge(1.07f); // increase current rate by 7%
				break;
				case SHOT_UPGRADE: p.UpgradeCurrentWeaponShot();
				break;
				case DART_WEAPON: p.addWeapon(p.DARTGUN);
					break;
				case MACHINEGUN_WEAPON:  p.addWeapon(p.MACHINEGUN);
					break;
				case POISON_WEAPON:  p.addWeapon(p.POISON);
					break;
				case ROCKET_WEAPON:  p.addWeapon(p.ROCKET);
					break;			
				}	

				Game.addToCurrentPlayerScore(100);

			}};

			new OffscreenRemoveComponent(this);
			new KillOnCollisionComponent(this);
			new SimpleMovementComponent(this, new Vector2f(0f, -0.45f), 0f);
			float scale = .08f;
			this.setScale(new Vector2f(0,scale));
			new ScaleOscComponent(this, new Oscillator(.5f, scale, 0f, 0f, OscType.SINE), new Vector2f(1.0f,0f));
			Texture t = null;
			float fps = 10f;
			int w = 0;
			switch(type){
			case DAMAGE_UPGRADE: 
				t = GameData.TEX_DAMAGE_POWER_UP; fps = 2f; 
				w = GameData.POWER_UP_WIDTH;
				break;
			case HEAL_UPGRADE: 
				t = GameData.TEX_HEAL_POWER_UP;
				w = GameData.POWER_UP_WIDTH;
				break;
			case SHIELD_UPGRADE: 
				t = GameData.TEX_SHIELD_POWER_UP;
				w = GameData.POWER_UP_WIDTH;
				break;
			case SHOT_UPGRADE: 
				t = GameData.TEX_SHOT_POWER_UP;
				w = GameData.POWER_UP_WIDTH;
				break;
			case DART_WEAPON: 
				t = GameData.TEX_DARTGUN_ICON;
				w = GameData.WEAPON_ICON_WIDTH;
				break;
			case MACHINEGUN_WEAPON: 
				t = GameData.TEX_MACHINEGUN_ICON;
				w = GameData.WEAPON_ICON_WIDTH;
				break;
			case POISON_WEAPON: 
				t = GameData.TEX_POISON_ICON;
				w = GameData.WEAPON_ICON_WIDTH;
				break;
			case ROCKET_WEAPON: 
				t = GameData.TEX_ROCKET_ICON;
				w = GameData.WEAPON_ICON_WIDTH;
				break;		
			}
			SpriteComponent sc = new SpriteComponent(this, w , fps, t);
			sc.setColor(Color.WHITE);
	}

	/**
	 * @return The type of drop
	 */
	public DropType getType() {
		return type;
	}	

}
