package engine.entity;

import org.jsfml.graphics.Color;
import org.jsfml.system.Vector2f;

import engine.component.*;
import engine.graphics.GraphicsHandler;
import game.Game;
import util.Oscillator;
import util.Oscillator.OscType;
import util.Util;

public class SpawnFactory {
	

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
	
	public static void spawnTestProjectile(Vector2f pos, Vector2f vel){
		Entity p = new Entity(pos);
		p.setDamage(1f);
		p.setScale(new Vector2f(.05f,.05f));
		Util.pointEntityInDirection(p, vel);
		Vector2f[] pts = new Vector2f[]{new Vector2f(0f,1f),new Vector2f(.5f,-1f),new Vector2f(-.5f,-1f)};
		new CollisionComponent(p, pts, CollisionID.PLAYER_PROJECTILE, CollisionID.ENEMY);
		new ConvexPolygonComponent(p, pts , Color.BLACK, DisplayType.FILL);
		new SimpleMovementComponent(p, vel, 0);
		new SelfDestructComponent(p, 3f);
		ParticleTrailComponent trail = new ParticleTrailComponent(p, 1f, .1f, 30f, Color.BLACK, 2, .25f);
		new OnCollisionComponent(p){
			@Override
			public void notifyAction() {
				entity.setHealth(0f);
			}			
		};
		trail.setScaleDamp(.04f);
		trail.setRandomVel(.8f);
	}
	
	public static void spawnTestEnemy(Vector2f pos){
		Entity enem = new Entity(pos);
		enem.setScale(new Vector2f(.1f,.1f));
		enem.setMaxHealth(10f);
		enem.healFully();
		CollisionComponent cc = new CollisionComponent(enem, Util.REGULAR_POLYGONS[7], CollisionID.ENEMY, CollisionID.PLAYER_PROJECTILE);
		cc.setHitboxDraw(true);	
		new HealthBarComponent(enem);
		new MovementOscComponent(enem, new Oscillator(.5f, .4f, 0f, 0f, OscType.SINE), new Vector2f(1f,0f));
	}
}
