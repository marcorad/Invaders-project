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

	public final Weapon SHOTGUN = new Weapon(.5f, 1f, WeaponID.SHOTGUN, this){
		@Override
		public void spawnProjectiles() {
			SpawnFactory.spawnBuckShot(shooter, 4f, 8f, .1f, 6*this.numShots);
		}
	};

	public final Weapon RAILGUN = new Weapon(.5f, 1f, WeaponID.SHOTGUN, this){
		@Override
		public void spawnProjectiles() {
			SpawnFactory.spawnBuckShot(shooter, 4f, 8f, .1f, 6*this.numShots);
		}
	};

	public final Weapon MACHINEGUN = new Weapon(.5f, 1f, WeaponID.SHOTGUN, this){
		@Override
		public void spawnProjectiles() {
			SpawnFactory.spawnBuckShot(shooter, 4f, 8f, .1f, 6*this.numShots);
		}
	};

	public final Weapon ROCKET = new Weapon(.1f, 1f, WeaponID.SHOTGUN, this){
		@Override
		public void spawnProjectiles() {
			Vector2f[] locs = Weapon.getSpawnLocations(shooter, this.numShots, 30f);
			for(int i = 0; i < numShots; i++)
				SpawnFactory.spawnTestProjectile(locs[i], Vector2f.mul(Util.facing(shooter), 3f));
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

	private Weapon currentWeapon = SHOTGUN;

	/**Construct a player at a certain position
	 * @param position
	 */
	public Player(Vector2f position) {	
		super(position);
		create();
		this.setScale(new Vector2f(.05f,.05f));		
	}

	private void create(){
		movement = new ComplexMovementComponent(this, Vector2f.ZERO, Vector2f.ZERO, 0f, 0f);
		movement.setSpeedClamp(maxvel);
		this.setMinPosition(new Vector2f(-.75f, -.81f));
		this.setMaxPosition(new Vector2f(.64f, -.5f));	
		currentWeapon.addShot();
		currentWeapon.addShot();
		currentWeapon.addShot();
		
		reloadbar = new Bar(.5f, .2f, new Color(0, 30, 200, 200), new Color(0, 30, 200, 150));
		reloadbar.setFrameColor(Color.TRANSPARENT);

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

		ParticleTrailComponent trail = new ParticleTrailComponent(this, .5f, .4f, 30f, Color.RED, 2, .5f);
		sprite = new SpriteComponent(this, 128, 0f, GameData.TEX_PLAYER);

		trail.setColorVary(255);
		trail.setRandomVel(.35f);
	}


	@Override
	public void update(float dt, float t){
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
	}

	@Override
	public void setScale(Vector2f scale) {
		super.setScale(scale);
		if(reloadbar != null)reloadbar.setScale(scale);
	}

	@Override
	public void setPosition(Vector2f position) {
		super.setPosition(position);
		if(reloadbar != null)reloadbar.setPosition(Vector2f.add(this.position, new Vector2f(0f, this.getScale().y)));
	}
	
	
	
	
}
