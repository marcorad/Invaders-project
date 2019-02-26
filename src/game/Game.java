package game;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Random;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.TextureCreationException;
import org.jsfml.system.Clock;
import org.jsfml.system.Vector2f;
import org.jsfml.window.ContextActivationException;
import org.jsfml.window.VideoMode;
import org.jsfml.window.Window;
import org.jsfml.window.event.MouseButtonEvent;
import org.jsfml.window.event.MouseEvent;
import org.jsfml.window.event.MouseWheelEvent;
import org.jsfml.window.Keyboard.Key;

import com.flowpowered.noise.Noise;
import com.flowpowered.noise.NoiseQuality;

import engine.component.CollisionComponent;
import engine.component.ComplexMovementComponent;
import engine.component.ConvexPolygonComponent;
import engine.component.DisplayType;
import engine.component.KeyboardMoveComponent;
import engine.component.MovementComponent;
import engine.component.MovementOscComponent;
import engine.component.RectangleComponent;
import engine.component.SimpleMovementComponent;
import engine.component.SpriteComponent;
import engine.component.TextComponent;
import engine.entity.Entity;
import engine.entity.EntityManager;
import engine.entity.SpawnFactory;
import engine.graphics.GraphicsHandler;
import engine.input.EventHandler;
import engine.input.MouseListener;
import util.Oscillator;
import util.Oscillator.OscType;
import util.Util;

public class Game implements MouseListener {

	public final static int WIDTH = 800, HEIGHT = 800;
	public String name;
	public static final int FRAMERATE = 1000;
	public static GraphicsHandler graphics;
	public static EntityManager entitymanager;


	private static EventHandler eventhandler;
	private static RenderWindow window;

	static{
		window = new RenderWindow();

		window.create(new VideoMode( WIDTH , HEIGHT), "", Window.TITLEBAR | Window.CLOSE);
		try {
			window.setActive();
		} catch (ContextActivationException e) {
			e.printStackTrace();
		}
		window.setKeyRepeatEnabled(false);
		window.setVerticalSyncEnabled(true); //VSync can can impact performance
		//Limit the framerate
		window.setFramerateLimit(FRAMERATE);
		
		graphics = new GraphicsHandler(window);
		eventhandler = new EventHandler(window);
		entitymanager = new EntityManager();
		
		Entity.setEnvironment(entitymanager, graphics);
	}

	public Game(String title){

		name = title;

		//Create the window

		Clock elapsed_time = new Clock();	
		Clock loop_time = new Clock();	
		float t = 0.0f, dt; //total time using double for extra precision

		
		
		eventhandler.attachMouseListener(this);

		Entity e = new Entity( new Vector2f(.0f,.0f));
		Entity e2 = new Entity(new Vector2f(.0f,.0f));
		Entity heart = new Entity(new Vector2f(.0f,.0f));
		Entity text = new Entity( new Vector2f(.0f,.0f));		
		Font font  = new Font();
		try {
			font.loadFromFile(Paths.get("font\\baab.otf"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		TextComponent tc = new TextComponent(text, Color.CYAN,font);

		text.addComponent(tc);
		tc.setText("IT  FUCKIN WORKS!");

		Oscillator osc = Util.randomOscillator(.2f, 1f, .3f, .5f, .0f, OscType.SINE) ;
		text.addComponent(				
				new MovementOscComponent(text, osc, Util.getVectorFromPolar(1.f, 45f))
				);

		heart.addComponent(new MovementOscComponent(heart, 
				osc , Util.getVectorFromPolar(1.f, 45f)));

		try {
			heart.addComponent(new SpriteComponent(heart,32,4.f, "sprites\\demo.png"));
		} catch (IOException | TextureCreationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ComplexMovementComponent smc = new ComplexMovementComponent(e, Vector2f.ZERO, Vector2f.ZERO, .0f, .0f);
		smc.setVelocityClamp(new Vector2f(1f, 1f));
		e.setMaxPosition(new Vector2f(1f,1f));
		e.setMinPosition(new Vector2f(-1f,-1f));
		KeyboardMoveComponent kmc = new KeyboardMoveComponent(eventhandler, e, Key.W,  Key.S,  Key.A,  Key.D,  null,  null, smc){
						
			@Override
			public void onDirection(Vector2f unitdir) {	
				ComplexMovementComponent m = (ComplexMovementComponent)this.movement;		
				m.setAccel(Vector2f.mul(unitdir, 2f));
				if(unitdir.x == 0f && unitdir.y == 0f){
					m.decelerate(2f);		
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




			Vector2f pts[] = {new Vector2f(0f,1f), new Vector2f(1f,.0f),new Vector2f(.5f,-1f),new Vector2f(-.5f,-1f),new Vector2f(-1f,.0f)};
			CollisionComponent c1 = new CollisionComponent( e, pts);	
			CollisionComponent c2 = new CollisionComponent( e2, pts);	
			ConvexPolygonComponent s1 = new ConvexPolygonComponent(e, 
					pts,Color.BLACK, DisplayType.FILL );
			ConvexPolygonComponent s2 = new ConvexPolygonComponent(e2, 
					pts,Color.BLACK, DisplayType.FILL ); 

			e.addComponent(kmc);
			e.addComponent(smc);
			e.addComponent(c1);
			e.addComponent(s1);
			e2.addComponent(s2);
			e.setScale(new Vector2f(.3f,.3f));
			e2.addComponent(c2);
			e2.setPosition(new Vector2f(.5f,.5f));
			e2.setScale(new Vector2f(.2f,.2f));

			Oscillator o = new Oscillator(0.5f, 0.1f, 0.2f, (float) Math.PI, Oscillator.OscType.SINE);

			//Main loop
			while(window.isOpen()) {
				loop_time.restart();
				dt = elapsed_time.restart().asSeconds(); //change in time
				t += dt; //keep track of total time
				e.addToRotation((15.f*dt));
				float s = o.get(dt);
				//e.addToScale(new Vector2f(.01f*dt,.01f*dt));
				//e2.addToScale(new Vector2f(dt,dt));
				heart.setScale(new Vector2f(s,s));
				text.setScale(new Vector2f(.17f*s,.17f*s));
				float x, y;
				x = (float)( 0.2*(2.0*Noise.valueCoherentNoise3D(5.0*t, 0, 0.0, 0, NoiseQuality.STANDARD)-1.0));
				y = (float)( 0.2*(2.0*Noise.valueCoherentNoise3D(0.0, 5.0*t, 0.0, 0, NoiseQuality.STANDARD)-1.0));
				//e2.setPosition(new Vector2f(x,y));
				//e2.increaseRotation((float) (-180.0*dt));

				float frate = (1.0f / dt);

				window.setTitle(name + " - " + String.valueOf(frate));		


				updateGame(dt, t);

				graphics.clear();

				entitymanager.drawEntities();

				//			float scale = o.get((float)t);
				//			System.out.println(scale);
				//			Vector2f rsize = new Vector2f(scale,scale);
				//			RectangleShape  rect = new RectangleShape(rsize);
				//			rect.setOrigin(Vector2f.mul(rsize, .5f));
				//			rect.setPosition(new Vector2f(0.0f,0.0f));
				//			rect.setFillColor(Color.WHITE);
				//			
				//			
				//			graphics.drawToRenderTexture(rect);
				graphics.display();			
				eventhandler.handleEvents();
				//System.out.println(loop_time.getElapsedTime().asSeconds());
				//System.out.println(heart.getInstantaneousVelocity());

			}
	}


	public void updateGame(float dt, float t){
		entitymanager.update(dt, t);
	}

	public static float incCol(float color, float dt, float mag){
		color += dt*mag;
		if(color >= 255.0f){
			color -= 255.0f;
		}
		return color;
	}



	public static void main(String[] args) {
		new Game("Test");
	}


	@Override
	public void onMousePress(MouseButtonEvent mbe) {
		Color c = Util.randomColor();
		for(int i = 0; i < 50; i++){
			Vector2f v = new Vector2f(0f,-.1f);
			float dev = .1f;
			v = Vector2f.add(v, new Vector2f(Util.randInRange(-dev, dev),Util.randInRange(-dev, dev)));
			SpawnFactory.spawnParticle(graphics.toWorldSpace(mbe.position), v,
					Util.randInRange(-180f, 180f), Util.randInRange(.01f, .04f),c, Util.randInRange(1f,5f), Util.randInRange(3, 7));
		}
	}




	@Override
	public void onMouseRelease(MouseButtonEvent mbe) {
		// TODO Auto-generated method stub

	}


	@Override
	public void onMouseWheelMoved(MouseWheelEvent mwe) {
		// TODO Auto-generated method stub

	}


	@Override
	public void onMouseMoved(MouseEvent me) {
		// TODO Auto-generated method stub

	}


	@Override
	public boolean isUseless() {
		// TODO Auto-generated method stub
		return false;
	}

}
