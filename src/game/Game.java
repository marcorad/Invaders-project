package game;

import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundBuffer;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Clock;
import org.jsfml.system.Vector2f;
import org.jsfml.window.ContextActivationException;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.VideoMode;
import org.jsfml.window.Window;
import org.jsfml.window.event.KeyEvent;
import org.jsfml.window.event.MouseButtonEvent;
import org.jsfml.window.event.MouseEvent;
import org.jsfml.window.event.MouseWheelEvent;

import engine.entity.Entity;
import engine.entity.EntityManager;
import engine.entity.Player;
import engine.entity.SpawnFactory;
import engine.graphics.GraphicsHandler;
import engine.input.EventHandler;
import engine.input.KeyListener;
import engine.input.MouseListener;
import state.StateMachine;

public class Game implements MouseListener, KeyListener {

	public final static int WIDTH = 700, HEIGHT = 700;
	public String name;
	public static final int FRAMERATE = 1000;
	public static GraphicsHandler graphics;
	public static EntityManager entitymanager;
	private boolean paused = false;


	public static EventHandler eventhandler;
	private static RenderWindow window;
	private static Player currentPlayer;
	public static StateMachine stateMachine;
	
	private static long highScore = 1000;	
	

	public static long getHighScore() {
		return highScore;
	}

	public static void setHighScore(long highScore) {
		Game.highScore = highScore;
	}

	private static void init(){
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

		stateMachine = new StateMachine();
	}
	
	private static float t = 0.0f, dt; 

	public Game(String title){	

		//load the data first
		GameData.load();
		//only create and initialise after game data has been loaded
		init();

		name = title;


		Clock loop_time = new Clock();			
		eventhandler.attachMouseListener(this);
		eventhandler.attachKeyListener(this);



		//			SpawnFactory.spawnTestEnemy(new Vector2f(0f, .8f));
		//			SpawnFactory.spawnTestEnemy(new Vector2f(0f, .6f));
		//			SpawnFactory.spawnTestEnemy(new Vector2f(.2f, .8f));
		//			SpawnFactory.spawnTestEnemy(new Vector2f(.2f, .6f));
		//			SpawnFactory.spawnTestEnemy(new Vector2f(-.2f, .8f));
		//			SpawnFactory.spawnTestEnemy(new Vector2f(-.2f, .6f));

		graphics.setBackground(GameData.TEX_GAME_BACKGROUND);



		Clock elapsed_time = new Clock();
		float wait_time = 1f; //just so that the window has some time to be created
		//Main loop
		while(window.isOpen()) {
			graphics.clear();

			dt = elapsed_time.restart().asSeconds(); //change in time
			t += dt; //keep track of total time

			if(t >= wait_time){
				loop_time.restart();
				updateGame(dt, t);				
				entitymanager.drawEntities();
				eventhandler.handleEvents();
				long lt = loop_time.restart().asMicroseconds();				
				//window.setTitle(name + " - " + lt);
			}
			graphics.display();	

		}
	}
	
	public static float getTotalElapsedTime(){
		return t;
	}



	public void updateGame(float dt, float t){
		if(!paused)
			entitymanager.update(dt, t);
	}


	public static void main(String[] args) {
		new Game("Test");
	}

	public static void quit(){
		window.close();
	}


	/**Get the current player in the game. This method can be used to modify the player's stats.
	 * @return The current player
	 */
	public static Player getCurrentPlayer() {
		return currentPlayer;
	}

	/**Set a new current player in the game. This should happen on every new game, s.t. the player is reset.
	 * @param p The new player
	 */
	public static void setCurrentPlayer(Player p) {
		currentPlayer = p;
	}

	@Override
	public void onMousePress(MouseButtonEvent mbe) {}

	@Override
	public void onMouseRelease(MouseButtonEvent mbe) {}


	@Override
	public void onMouseWheelMoved(MouseWheelEvent mwe) {

	}


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
		//ESC and q will close the game
		if(ke.key == Key.ESCAPE) window.close();
		else if(ke.key == Key.P) paused = !paused;
		else if(ke.key == Key.H) entitymanager.toggleHitboxDraw();
	}


	@Override
	public void onKeyRelease(KeyEvent ke) {}

}
