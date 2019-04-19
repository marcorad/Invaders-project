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
import levelgen.LevelGen;
import music.MusicController;
import state.StateMachine;
import state.StateMachine.State;
import util.Benchmarker;

public class Game implements MouseListener, KeyListener {

	public static int WIDTH = 700, HEIGHT = 700;
	public String name;
	public static final int FRAMERATE = 1000;
	public static GraphicsHandler graphics;
	public static EntityManager entitymanager;
	public static MusicController music;
	private boolean paused = false;
	private static boolean soundEnabled=false;
	private static boolean musicEnabled=false;	

	private static int gameLength = 0;
	private static int numEnemiesKilled = 0;
	private static int currentPlayerScore = 0;
	private static int numEnemies = 0;
	private static int totalEnemiesSpawned = 0;

	public static int getNumberOfEnemiesOnScreen(){
		return numEnemies;
	}

	public static void incNumberOfEnemiesOnScreen(){
		totalEnemiesSpawned++;
		numEnemies++;
	}

	public static void incNumberOfEnemiesKilled(){
		numEnemiesKilled++;
		numEnemies--;
	}

	public static void resetGameNumbers(){
		numEnemiesKilled = 0;
		currentPlayerScore = 0;
		numEnemies = 0;
		totalEnemiesSpawned = 0;
	}

	public static void addToCurrentPlayerScore(int amount){
		currentPlayerScore += amount;
	}

	public static int getCurrentPlayerScore(){
		return currentPlayerScore;
	}

	public static int getNumberOfEnemiesLeftToSpawn(){
		return gameLength - totalEnemiesSpawned;
	}
	
	public static int getNumberOfEnemiesLeftToKill(){
		return gameLength - numEnemiesKilled;
	}


	public static int getNumberOfEnemiesKilled(){
		return numEnemiesKilled;
	}


	public static int getGameLength() {
		return gameLength;
	}


	public static void setGameLength(int gameLength) {
		Game.gameLength = gameLength;
	}


	public static boolean isSoundEnabled() {
		return soundEnabled;
	}


	public static boolean isMusicEnabled() {
		return musicEnabled;
	}

	public static void toggleMusicEnabled(){
		musicEnabled = !musicEnabled;
	}

	public static void toggleSoundEnabled(){
		soundEnabled = !soundEnabled;
	}


	public static EventHandler eventhandler;
	public static RenderWindow window;
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
		//WIDTH = window.getSize().x;
		//HEIGHT = window.getSize().y;
		window.create(new VideoMode(WIDTH, HEIGHT), "Invaders" , Window.CLOSE);
		try {
			window.setActive();
		} catch (ContextActivationException e) {
			e.printStackTrace();
		}
		window.setKeyRepeatEnabled(false);
		window.setVerticalSyncEnabled(true); //VSync can impact performance
		//Limit the framerate

		graphics = new GraphicsHandler(window);
		eventhandler = new EventHandler(window);
		entitymanager = new EntityManager();
		Entity.setEnvironment(entitymanager, graphics, eventhandler);

		stateMachine = new StateMachine();
		music = new MusicController();

	}

	private static float t = 0.0f, dt = 0f; 

	public Game(String title){	

		name = title;	
		//load the data first
		GameData.load();			
		init();
		music.setInititialMusic(GameData.MUSIC_INTRO);	
		music.start();

		eventhandler.attachMouseListener(this);
		eventhandler.attachKeyListener(this);



		//			SpawnFactory.spawnTestEnemy(new Vector2f(0f, .8f));
		//			SpawnFactory.spawnTestEnemy(new Vector2f(0f, .6f));
		//			SpawnFactory.spawnTestEnemy(new Vector2f(.2f, .8f));
		//			SpawnFactory.spawnTestEnemy(new Vector2f(.2f, .6f));
		//			SpawnFactory.spawnTestEnemy(new Vector2f(-.2f, .8f));
		//			SpawnFactory.spawnTestEnemy(new Vector2f(-.2f, .6f));

		Benchmarker bm = new Benchmarker("Total update time", 60);
		Benchmarker bmg = new Benchmarker("Draw time", 60);
		//bm.setAsMainBenchmarker();


		Clock elapsed_time = new Clock();
		float wait_time = 1f; //just so that the window has some time to be created
		//Main loop
		while(window.isOpen()) {
			graphics.clear();

			dt = elapsed_time.restart().asSeconds(); //change in time
			t += dt; //keep track of total time

			if(t >= wait_time){
				bm.startMeasurement();
				updateGame(dt, t);	
				bm.stopMeasurement();
				
				bmg.startMeasurement();
				entitymanager.drawEntities();
				eventhandler.handleEvents();
				graphics.update(dt, t);
				bmg.stopMeasurement();

			}			
			graphics.display();	

		}
	}

	public static float getTotalElapsedTime(){
		return t;
	}



	public void updateGame(float dt, float t){
		if(!paused){
			entitymanager.update(dt, t);
			if(stateMachine.getCurrentState() == State.GAME) 
				LevelGen.update(dt);
		}
	}


	public static void main(String[] args) {
		new Game("");
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



	@Override
	public void onKeyPress(KeyEvent ke) {
		//ESC and q will close the game
		if(ke.key == Key.Q) window.close();
		else if(ke.key == Key.P) paused = !paused;
		else if(ke.key == Key.H) entitymanager.toggleHitboxDraw();
	}


	@Override
	public void onKeyRelease(KeyEvent ke) {}

}
