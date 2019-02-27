package engine.entity;

import org.jsfml.graphics.Color;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.event.MouseWheelEvent;

import engine.component.CollisionComponent;
import engine.component.ComplexMovementComponent;
import engine.component.ConvexPolygonComponent;
import engine.component.KeyboardMoveComponent;
import engine.component.MouseMoveControlComponent;
import engine.component.MovementComponent;
import engine.component.SpriteComponent;

public class Player extends Entity {
	
	private ComplexMovementComponent movement;
	private SpriteComponent sprite;
	private CollisionComponent collision;
	private ConvexPolygonComponent hitbox;
	private KeyboardMoveComponent keys;
	private MouseMoveControlComponent mouse;
	private float accelmag = 3.5f;
	
	private Vector2f maxvel = new Vector2f(1f,1f);

	public Player(Vector2f position) {
		super(position);	
		create();
		this.setScale(new Vector2f(.1f,.1f));
	}
	
	private void create(){
		movement = new ComplexMovementComponent(this, Vector2f.ZERO, Vector2f.ZERO, 0f, 0f);
		movement.setVelocityClamp(maxvel);
		
		keys = new KeyboardMoveComponent(this,Key.W, Key.D, Key.A, Key.D, null,null,movement){
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
		
		hitbox = new ConvexPolygonComponent(this, new Vector2f[]{new Vector2f(0f,1f), new Vector2f(1f,.75f), new Vector2f(1f,-1f), new Vector2f(-1f,-1f), new Vector2f(-1f,.75f)});
		hitbox.setColor(Color.RED);
		
		mouse = new MouseMoveControlComponent(this){

			@Override
			public void onRightMousePress(Vector2f worldpos) {
				System.out.println("RIGHT MOUSE PRESS: " + worldpos);
			}

			@Override
			public void onLeftMousePress(Vector2f worldpos) {
				System.out.println("LEFT MOUSE PRESS: " + worldpos);
			}

			@Override
			public void onRightMouseRelease(Vector2f worldpos) {
				System.out.println("RIGHT MOUSE RELEASE: " + worldpos);
			}

			@Override
			public void onLeftMouseRelease(Vector2f worldpos) {
				System.out.println("LEFT MOUSE RELEASE: " + worldpos);
				
			}

			@Override
			public void onMouseMove(Vector2f worldpos) {
				
			}
			
		};
		
		
	
			
		
	}

}
