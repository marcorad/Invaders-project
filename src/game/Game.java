package game;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Random;

import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundBuffer;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.TextureCreationException;
import org.jsfml.system.Clock;
import org.jsfml.system.Vector2f;
import org.jsfml.window.ContextActivationException;
import org.jsfml.window.VideoMode;
import org.jsfml.window.Window;
import org.jsfml.window.event.KeyEvent;
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
import engine.entity.Player;
import engine.entity.SpawnFactory;
import engine.graphics.GraphicsHandler;
import engine.gui.Button;
import engine.input.EventHandler;
import engine.input.KeyListener;
import engine.input.MouseListener;
import util.Oscillator;
import util.Oscillator.OscType;
import util.Util;

public class Game implements MouseListener, KeyListener {

	public final static int WIDTH = 800, HEIGHT = 800;
	public String name;
	public static final int FRAMERATE = 1000;
	public static GraphicsHandler graphics;
	public static EntityManager entitymanager;
	private boolean paused = false;


	private static EventHandler eventhandler;
	private static RenderWindow window;

	static{
		window = new RenderWindow();

		//window.create(VideoMode.getDesktopMode(), "", Window.FULLSCREEN);
		window.create(new VideoMode(WIDTH, HEIGHT), "", Window.CLOSE);
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

		Entity.setEnvironment(entitymanager, graphics, eventhandler);
	}

	public Game(String title){		name = title;

	Clock elapsed_time = new Clock();	
	Clock loop_time = new Clock();	
	float t = 0.0f, dt; //total time using double for extra precision		
	eventhandler.attachMouseListener(this);
	eventhandler.attachKeyListener(this);
	new Player(new Vector2f(0f,-.8f));
	Button b1 = new Button(new Vector2f(.8f,.8f), GameData.TEX_TEST_BUTTON, 128, 10f){
		@Override
		public void buttonAction() {
			System.out.println("Button 1 pressed");
		}};

		b1.setHoverColor(new Color(0,255,255,255));
		b1.setScale(new Vector2f(.2f,.2f));

		Button b2 = new Button(new Vector2f(.8f,.4f), GameData.TEX_TEST_BUTTON, 128, 10f){
			@Override
			public void buttonAction() {
				System.out.println("Button 2 pressed");
			}};

			b2.setHoverColor(new Color(0,255,255,255));
			b2.setScale(new Vector2f(.2f,.2f));

			SpawnFactory.spawnTestEnemy(new Vector2f(0f, .8f));
			SpawnFactory.spawnTestEnemy(new Vector2f(0f, .6f));
			SpawnFactory.spawnTestEnemy(new Vector2f(.2f, .8f));
			SpawnFactory.spawnTestEnemy(new Vector2f(.2f, .6f));
			SpawnFactory.spawnTestEnemy(new Vector2f(-.2f, .8f));
			SpawnFactory.spawnTestEnemy(new Vector2f(-.2f, .6f));
			graphics.setBackground(GameData.TEX_GAME_BACKGROUND);


			//Main loop
			while(window.isOpen()) {
				loop_time.restart();
				dt = elapsed_time.restart().asSeconds(); //change in time
				t += dt; //keep track of total time

				float frate = (1.0f / dt);

				window.setTitle(name + " - " + String.valueOf(frate));

				updateGame(dt, t);

				graphics.clear();

				entitymanager.drawEntities();

				graphics.display();			
				eventhandler.handleEvents();

			}
	}


	public void updateGame(float dt, float t){
		if(!paused)
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
	}

	@Override
	public void onMouseRelease(MouseButtonEvent mbe) {}


	@Override
	public void onMouseWheelMoved(MouseWheelEvent mwe) {}


	@Override
	public void onMouseMoved(MouseEvent me) {}


	@Override
	public boolean isUseless() {
		return false;
	}

	public static void playSound(SoundBuffer buf, float volume){
		Sound s = new Sound(buf);
		s.setVolume(volume);
		s.play();
	}


	@Override
	public void onKeyPress(KeyEvent ke) {
		if(ke.key == Key.ESCAPE) window.close(); 
		else if(ke.key == Key.P) paused = !paused;
	}


	@Override
	public void onKeyRelease(KeyEvent ke) {
		// TODO Auto-generated method stub

	}

}
