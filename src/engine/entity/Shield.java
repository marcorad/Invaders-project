package engine.entity;

import org.jsfml.graphics.Color;
import org.jsfml.system.Vector2f;

import engine.component.CollisionComponent;
import engine.component.CollisionID;
import engine.component.SpriteComponent;
import game.GameData;
import util.Util;

/**
 * Player spawnable shield that blocks enemy projectiles. The shield loses health over time.
 * The transparency of the shield is dependent on its current health.
 */
public class Shield extends Entity {
	
	private SpriteComponent sprite;

	public Shield(Player p) {
		super(Vector2f.add(Util.approxParticleOffset(Util.facing(p), p, .13f), p.getPosition()), p.getTheta());		
		super.setScale(new Vector2f(.12f, .15f));
		sprite = new SpriteComponent(this, 128, 0f, GameData.TEX_SHIELD);
		new CollisionComponent(this, GameData.HB_SHIELD, CollisionID.SHIELD, CollisionID.ENEMY_PROJECTILE);
	}

	private static float health_decay_rate = 3f;
	
	@Override
	public void update(float dt, float t) {
		this.doDamage(health_decay_rate*dt);
		sprite.setColor(Util.lerpColor(health/maxHealth, new Color(0,100,255,0),new Color(0,100,255,255)));
		super.update(dt, t);
	}
	
	

}
