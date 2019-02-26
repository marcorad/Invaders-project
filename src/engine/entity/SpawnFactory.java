package engine.entity;

import org.jsfml.graphics.Color;
import org.jsfml.system.Vector2f;

import engine.component.*;
import engine.graphics.GraphicsHandler;
import game.Game;
import util.Oscillator.OscType;
import util.Util;

public class SpawnFactory {
	

	public static void spawnParticle(Vector2f pos, Vector2f vel, float angularvel, float scale, Color color, float lifetime, int n){
		Entity p = new Entity(pos);
		p.setScale(new Vector2f(scale, scale));		
		p.addComponent(new SimpleMovementComponent(p, vel, angularvel));
		p.addComponent(new SelfDestructComponent(p, lifetime));
		Vector2f[] pts;
		if(n<10 && n>2){
			pts = Util.REGULAR_POLYGONS[n-3];
		}
		else pts = Util.getRegularPoly(n);
		ConvexPolygonComponent shape = new ConvexPolygonComponent(p, pts, color, DisplayType.FILL);
		ColorOscillationComponent  c = new ColorOscillationComponent(p, color, Color.TRANSPARENT, .5f/lifetime, OscType.TRIANGLE);
		c.addComponent(shape);
		
		p.addComponent(shape);
		p.addComponent(c);
		
	}
}
