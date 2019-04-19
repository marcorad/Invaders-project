package state;

import org.jsfml.graphics.Color;
import org.jsfml.system.Vector2f;

import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;

import engine.component.ColorOscillationComponent;
import engine.component.SpriteComponent;
import engine.component.TextComponent;
import engine.entity.Entity;
import engine.entity.Player;
import engine.entity.EnemyDrop;
import engine.entity.EnemyDrop.DropType;
import engine.entity.SpawnFactory;
import engine.graphics.GraphicsHandler;
import engine.gui.Button;
import game.Game;
import game.GameData;
import levelgen.LevelGen;
import util.Oscillator.OscType;

/**A class that specifies the different states of the program.
 * @author Kieran
 *
 */
public class StateMachine {
	public enum State{
		MENU, GAME, INSTRUCTIONS, GAME_OVER, CHOOSE_GAME;
	}
	
	private static int SHORT_GAME = 50, LONG_GAME = 150, ENDLESS_GAME = Integer.MAX_VALUE;

	private State currentState;

	public StateMachine(){
		setCurrentState(State.MENU); //initial state
	}

	public State getCurrentState(){
		return currentState;
	}

	/**Set the current state. This also removes all current entities and resets the state.
	 * @param currentState The state to switch to
	 */
	public void setCurrentState(State currentState){

		Game.entitymanager.clearEntities();
		this.currentState = currentState;
		switch(currentState){
		case MENU: {
			//populate 
			MenuEntities();
			//System.out.println("MENU state was chosen.");
			break;
		}
		case GAME: {
			//populate 
			GameEntities();
			//System.out.println("GAME state was chosen.");
			break;
		}
		case INSTRUCTIONS: {
			//populate 
			InstructEntities();
			//System.out.println("INSTRUCTIONS state was chosen.");
			break;
		}
		case GAME_OVER: {
			//populate 
			GameOverEntities();
			//System.out.println("GAME_OVER state was chosen.");
			break;
		}
		case CHOOSE_GAME: {
			//populate 
			ChooseGameEntities();
			//System.out.println("GAME_OVER state was chosen.");
			break;
		}
		}
	}


	/**
	 * Construct all the required menu entities
	 */
	public void MenuEntities(){

		Color sel = new Color(16,62,229,255);
		Color unsel = new Color(3,10,38,180);
		float buttfps = 0f;
		Vector2f buttscale = new Vector2f(.35f,.08f);
		Button playbutton = new Button(new Vector2f(0f, .3f), GameData.TEX_PLAY_GAME_TEXT, GameData.TEXT_WIDTH, buttfps, State.MENU){
			@Override
			public void buttonAction() {
				setCurrentState(State.CHOOSE_GAME);
			}
		};

		playbutton.setScale(buttscale);
		playbutton.setSelectedColor(sel);
		playbutton.setUnselectedColor(unsel);

		Button quitbutton = new Button(new Vector2f(0f, -.5f), GameData.TEX_QUIT_TEXT, GameData.TEXT_WIDTH, buttfps, State.MENU){
			@Override
			public void buttonAction() {
				Game.quit();
			}
		};

		quitbutton.setScale(buttscale);
		quitbutton.setSelectedColor(sel);
		quitbutton.setUnselectedColor(unsel);

		Button instrbutton = new Button(new Vector2f(0f, .1f), GameData.TEX_INSTRUCTIONS_TEXT, GameData.TEXT_WIDTH, buttfps, State.MENU){
			@Override
			public void buttonAction() {
				setCurrentState(State.INSTRUCTIONS);
			}
		};

		instrbutton.setScale(buttscale);
		instrbutton.setSelectedColor(sel);
		instrbutton.setUnselectedColor(unsel);

		Button musicbutton = new Button(new Vector2f(-.2f, -.1f), GameData.TEX_MUSIC_TEXT, GameData.TEXT_WIDTH, buttfps, State.MENU){
			@Override
			public void buttonAction() {
				Game.toggleMusicEnabled();
			}
		};

		musicbutton.setScale(buttscale);
		musicbutton.setSelectedColor(sel);
		musicbutton.setUnselectedColor(unsel);

		Button soundbutton = new Button(new Vector2f(-.2f, -.3f), GameData.TEX_SOUND_TEXT, GameData.TEXT_WIDTH, buttfps, State.MENU){
			@Override
			public void buttonAction() {
				Game.toggleSoundEnabled();
			}
		};

		soundbutton.setScale(buttscale);
		soundbutton.setSelectedColor(sel);
		soundbutton.setUnselectedColor(unsel);

		Color hscol = new Color(10,30,150,255);

		Entity hs_text = new Entity(new Vector2f(-.5f, .8f));
		SpriteComponent hs_spr = new SpriteComponent(hs_text, GameData.TEXT_WIDTH, buttfps, GameData.TEX_HIGH_SCORE_TEXT);
		hs_text.setScale(buttscale);
		ColorOscillationComponent hs_cosc = new ColorOscillationComponent(hs_text, Color.RED, Color.GREEN, 0.5f, OscType.SINE);
		hs_cosc.addComponent(hs_spr);

		//		Entity hs = new Entity(new Vector2f(.5f, -.73f));
		//		TextComponent hstxt = new TextComponent(hs, Color.BLACK, GameData.FONT_CALIBRI);
		//		hstxt.setText(Long.toString(Game.getHighScore()));
		//		hs.setScale(new Vector2f(.25f,.25f));

		SpawnFactory.spawnNumber(new Vector2f(.1f, .8f), .1f, 125865, hscol);

		Entity musicOn = new Entity(new Vector2f(.3f, -.1f)){
			@Override
			public void draw() {
				if(Game.isMusicEnabled())
					super.draw();
			}
		};
		SpriteComponent monsc = new SpriteComponent(musicOn, GameData.TEXT_WIDTH, 0f, GameData.TEX_ON_TEXT);
		musicOn.setScale(buttscale);
		monsc.setColor(unsel);

		Entity musicOff = new Entity(new Vector2f(.3f, -.1f)){
			@Override
			public void draw() {
				if(!Game.isMusicEnabled())
					super.draw();
			}
		};
		SpriteComponent moffsc = new SpriteComponent(musicOff, GameData.TEXT_WIDTH, 0f, GameData.TEX_OFF_TEXT);
		musicOff.setScale(buttscale);
		moffsc.setColor(unsel);

		Entity soundOn = new Entity(new Vector2f(.35f, -.3f)){
			@Override
			public void draw() {
				if(Game.isSoundEnabled())
					super.draw();
			}
		};
		SpriteComponent sonsc = new SpriteComponent(soundOn, GameData.TEXT_WIDTH, 0f, GameData.TEX_ON_TEXT);
		soundOn.setScale(buttscale);
		sonsc.setColor(unsel);

		Entity soundOff = new Entity(new Vector2f(.35f, -.3f)){
			@Override
			public void draw() {
				if(!Game.isSoundEnabled())
					super.draw();
			}
		};
		SpriteComponent soffsc = new SpriteComponent(soundOff, GameData.TEXT_WIDTH, 0f, GameData.TEX_OFF_TEXT);
		soundOff.setScale(buttscale);
		soffsc.setColor(unsel);
		//SpawnFactory.SpawnEnemy6(new Vector2f(0f,1.2f));

	}

	/**
	 * Construct all the required game entities
	 */
	public void GameEntities(){
		Game.setCurrentPlayer(new Player(new Vector2f(0f, -.85f))); //reset player
		LevelGen.reset();//reset level manager
		Game.resetGameNumbers(); //reset enemy kill count
		
		
		LevelGen.ENEMY_SPAWN_SPECS[0].spawn(new Vector2f(1.2f, .85f));
		LevelGen.ENEMY_SPAWN_SPECS[0].spawn(new Vector2f(-1.2f, .75f));		
		
		//create score and enemy tracking
		Vector2f scale = new Vector2f(.08f,.1f);
		
		Entity score = new Entity(new Vector2f(-.75f, .95f));
		score.setScale(scale);
		new TextComponent(score, new Color(0,0,0), GameData.FONT_VIDEO){

			@Override
			public void draw(GraphicsHandler graphics) {
				this.setText("score " + Game.getCurrentPlayerScore());
				super.draw(graphics);
			}
			};
			
			if(Game.getGameLength() != Integer.MAX_VALUE){
			Entity enemleft = new Entity(new Vector2f(.65f, .95f));
			enemleft.setScale(scale);
			new TextComponent(enemleft, new Color(150,30,0), GameData.FONT_VIDEO){

				@Override
				public void draw(GraphicsHandler graphics) {
					this.setText( Game.getNumberOfEnemiesLeftToKill() + " enemies left");
					super.draw(graphics);
				}};
			}
		
		Game.music.setNextMusic(GameData.MUSIC_GAME);

	}

	/**
	 * Construct all the required instruction page entities
	 */
	public void InstructEntities(){
		Entity instr = new Entity(Vector2f.ZERO);
		SpriteComponent spr1 = new SpriteComponent(instr, GameData.INSTRUCTION_PAGE_WIDTH, 0f, GameData.TEX_INSTRUCTIONS_PAGE);
		spr1.setColor(Color.WHITE);

		Entity mouse = new Entity(new Vector2f(-.12f, -.76f));
		SpriteComponent spr2 = new SpriteComponent(mouse, GameData.MOVING_MOUSE_WIDTH, 10f, GameData.TEX_MOVING_MOUSE);
		spr2.setColor(Color.WHITE);
		mouse.setScale(new Vector2f(.15f,.15f));

		Entity scroll = new Entity(new Vector2f(-.7f, -.8f));
		SpriteComponent spr3 = new SpriteComponent(scroll, GameData.MOVING_MOUSE_WIDTH, 10f, GameData.TEX_SCROLLING_MOUSE);
		spr3.setColor(Color.WHITE);
		scroll.setScale(new Vector2f(.15f,.15f));

		Color sel = new Color(16,62,229,255);
		Color unsel = new Color(3,10,38,180);
		float buttfps = 12f;
		Vector2f buttscale = new Vector2f(.35f,.15f);
		Button menubutton = new Button(new Vector2f(.6f, -.8f), GameData.TEX_MAIN_MENU_TEXT, GameData.TEXT_WIDTH, buttfps, State.INSTRUCTIONS){
			@Override
			public void buttonAction() {
				setCurrentState(State.MENU);
			}
		};

		menubutton.setScale(buttscale);
		menubutton.setSelectedColor(sel);
		menubutton.setUnselectedColor(unsel);
		
		
	}
	
	
	/**
	 * Construct all the required game choice entities over entities
	 */
	public void ChooseGameEntities(){
		Color unsel = new Color(3,10,38,180);
		float buttfps = 0f;
		Vector2f buttscale = new Vector2f(.35f,.1f);
		Button shortgame = new Button(new Vector2f(0f, .2f), GameData.TEX_SHORT_TEXT, GameData.TEXT_WIDTH, buttfps, State.CHOOSE_GAME){
			@Override
			public void buttonAction() {
				Game.setGameLength(SHORT_GAME);
				setCurrentState(State.GAME);
			}
		};
		
		shortgame.setScale(buttscale);
		shortgame.setSelectedColor(new Color(20,215,20));
		shortgame.setUnselectedColor(unsel);
		
		Button longgame = new Button(new Vector2f(0f, -.1f), GameData.TEX_LONG_TEXT, GameData.TEXT_WIDTH, buttfps, State.CHOOSE_GAME){
			@Override
			public void buttonAction() {
				Game.setGameLength(LONG_GAME);
				setCurrentState(State.GAME);
			}
		};
		
		longgame.setScale(buttscale);
		longgame.setSelectedColor(new Color(10,20,215));
		longgame.setUnselectedColor(unsel);
		
		Button endlessgame = new Button(new Vector2f(0f, -.4f), GameData.TEX_ENDLESS_TEXT, GameData.TEXT_WIDTH, buttfps, State.CHOOSE_GAME){
			@Override
			public void buttonAction() {
				Game.setGameLength(ENDLESS_GAME);
				setCurrentState(State.GAME);
			}
		};
		
		endlessgame.setScale(buttscale);
		endlessgame.setSelectedColor(new Color(215,10,20));
		endlessgame.setUnselectedColor(unsel);
		
		Entity title = new Entity(new Vector2f(0f,.7f));
		title.setScale(new Vector2f(.45f,.2f));
		SpriteComponent sc = new SpriteComponent(title, GameData.TEXT_WIDTH, 0f, GameData.TEX_CHOOSE_GAME_TEXT);
		sc.setColor(new Color(124,23,156));
		
	
	}

	/**
	 * Construct all the required game over entities
	 */
	public void GameOverEntities(){
		Entity go_text = new Entity(new Vector2f(0f, .5f));
		new SpriteComponent(go_text, GameData.TEXT_WIDTH, 15f, GameData.TEX_GAME_OVER_TEXT);
		go_text.setScale(new Vector2f(.3f,.3f));

		Color sel = new Color(16,62,229,255);
		Color unsel = new Color(3,10,38,180);
		float buttfps = 12f;
		Vector2f buttscale = new Vector2f(.35f,.15f);
		Button menubutton = new Button(new Vector2f(0f, -.8f), GameData.TEX_MAIN_MENU_TEXT, GameData.TEXT_WIDTH, buttfps, State.GAME_OVER){
			@Override
			public void buttonAction() {
				setCurrentState(State.MENU);
				Game.music.setNextMusic(GameData.MUSIC_INTRO);
			}
		};

		menubutton.setScale(buttscale);
		menubutton.setSelectedColor(sel);
		menubutton.setUnselectedColor(unsel);
		
		Button playbutton = new Button(new Vector2f(0f, -.5f), GameData.TEX_PLAY_GAME_TEXT, GameData.TEXT_WIDTH, buttfps, State.GAME_OVER){
			@Override
			public void buttonAction() {
				setCurrentState(State.GAME); //keep same game length
			}
		};

		playbutton.setScale(buttscale);
		playbutton.setSelectedColor(sel);
		playbutton.setUnselectedColor(unsel);
		

		Entity score = new Entity(new Vector2f(0f, 0f));
		score.setScale(new Vector2f(.2f,.14f));
		TextComponent tc = new TextComponent(score, new Color(0,0,0), GameData.FONT_VIDEO);	
		ColorOscillationComponent cosc = new ColorOscillationComponent(score, Color.BLUE, Color.MAGENTA, .3f, OscType.SINE);
		cosc.addComponent(tc);
		tc.setText("FINAL SCORE : " + Game.getCurrentPlayerScore());

		Game.music.setNextMusic(GameData.MUSIC_GAME_OVER);
	}
}
