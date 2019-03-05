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
		
		Entity.setEnvironment(entitymanager, graphics, eventhandler);
	}

	public Game(String title){		name = title;
		
		Clock elapsed_time = new Clock();	
		Clock loop_time = new Clock();	
		float t = 0.0f, dt; //total time using double for extra precision		
		eventhandler.attachMouseListener(this);
		new Player(new Vector2f(0f,-.8f));
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

}
