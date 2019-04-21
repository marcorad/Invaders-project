package state;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;

import engine.component.ColorOscillationComponent;
import engine.component.ScaleOscComponent;
import engine.component.SpriteComponent;
import engine.component.TextComponent;
import engine.entity.Entity;
import engine.entity.Player;
import engine.entity.SpawnFactory;
import engine.graphics.GraphicsHandler;
import engine.gui.Button;
import game.Game;
import game.GameData;
import highscore.HighScoreLoader;
import levelgen.LevelGen;
import util.Oscillator;
import util.Oscillator.OscType;
import util.Util;

/**
 * Specifies the different states of the program. LOTS OF HARDCODED ENTITIES.
 */
public class StateMachine {
	public enum State{
		MENU,
		GAME, 
		INSTRUCTIONS_1,
		INSTRUCTIONS_2, 
		INSTRUCTIONS_3, 
		GAME_OVER, 
		CHOOSE_GAME;
	}

	private static int SHORT_GAME = 50, LONG_GAME = 120, ENDLESS_GAME = Integer.MAX_VALUE;

	private State currentState;

	public StateMachine(){
		setCurrentState(State.MENU); //initial state
	}

	public State getCurrentState(){
		return currentState;
	}

	/**Set the current state. This also removes all current entities and creates ones needed for the state.
	 * @param currentState The state to switch to
	 */
	public void setCurrentState(State currentState){

		Game.entitymanager.clearEntities();
		this.currentState = currentState;
		switch(currentState){
		case MENU: {
			MenuEntities();
			break;
		}
		case GAME: {
			GameEntities();
			break;
		}
		case INSTRUCTIONS_1: {
			Instructtions1Entities();
			break;
		}
		case GAME_OVER: {
			HighScoreLoader.saveScoreIfGreater(Game.getCurrentPlayerScore());
			GameOverEntities();
			break;
		}
		case CHOOSE_GAME: {
			ChooseGameEntities();
			break;
		}
		case INSTRUCTIONS_2:
			Intructions2Entities();
			break;
		case INSTRUCTIONS_3:
			Intructions3Entities();
			break;
		}
	}
	
	
	/**
	 * Construct appropriate state entities.
	 */
	private void Intructions2Entities(){
		Color sel = new Color(16,62,229,255);
		Color unsel = new Color(3,10,38,180);
		float buttfps = 12f;
		Vector2f buttscale = new Vector2f(.25f,.1f);
		Button menubutton = new Button(new Vector2f(0f, -.8f), GameData.TEX_MAIN_MENU_TEXT, GameData.TEXT_WIDTH, buttfps, State.INSTRUCTIONS_2){
			@Override
			public void buttonAction() {
				setCurrentState(State.MENU);
			}
		};

		menubutton.setScale(buttscale);
		menubutton.setSelectedColor(sel);
		menubutton.setUnselectedColor(unsel);
		
		Button nextbutton = new Button(new Vector2f(.6f, -.8f), GameData.TEX_NEXT_PAGE_TEXT, GameData.TEXT_WIDTH, buttfps, State.INSTRUCTIONS_2){
			@Override
			public void buttonAction() {
				setCurrentState(State.INSTRUCTIONS_3);
			}
		};

		nextbutton.setScale(buttscale);
		nextbutton.setSelectedColor(sel);
		nextbutton.setUnselectedColor(unsel);
		
		Button prevbutton = new Button(new Vector2f(-.6f, -.8f), GameData.TEX_PREV_PAGE_TEXT, GameData.TEXT_WIDTH, buttfps, State.INSTRUCTIONS_2){
			@Override
			public void buttonAction() {
				setCurrentState(State.INSTRUCTIONS_1);
			}
		};

		prevbutton.setScale(buttscale);
		prevbutton.setSelectedColor(sel);
		prevbutton.setUnselectedColor(unsel);
		
		generateTextEntity(new Vector2f(0f,.9f), .1f, "WEAPONS AND POWER-UPS");
		float spacing = .12f;
		float x1 = .7f, x2 = -.3f, y = .7f;
		generateTextEntity(new Vector2f(0,y - 0*spacing), .06f, "POWER-UPS WILL UPGRADE YOUR CURRENT HELD WEAPON IF APPLICABLE");	
		generateTextEntity(new Vector2f(0,y - 1*spacing), .06f, "COLLECTING AN EXISTING WEAPON WILL GIVE IT AN EXTRA SHOT (MAX 5)");		
		generateTextEntity(new Vector2f(x1,y - 3*spacing), .06f, "POISON GUN");
		generateTextEntity(new Vector2f(x1,y - 5*spacing), .06f, "MACHINE GUN");
		generateTextEntity(new Vector2f(x1,y - 7*spacing), .06f, "ROCKET LAUNCHER");
		generateTextEntity(new Vector2f(x1,y - 9*spacing), .06f, "DART GUN");	
		
		float scale = .09f;
		generateIcon(new Vector2f(x1-0.4f, y - 3.3f*spacing), GameData.WEAPON_ICON_WIDTH, GameData.TEX_POISON_ICON, scale);
		generateIcon(new Vector2f(x1-0.4f, y - 5.3f*spacing), GameData.WEAPON_ICON_WIDTH, GameData.TEX_MACHINEGUN_ICON, scale);
		generateIcon(new Vector2f(x1-0.4f, y - 7.3f*spacing), GameData.WEAPON_ICON_WIDTH, GameData.TEX_ROCKET_ICON, scale);
		generateIcon(new Vector2f(x1-0.4f, y - 9.3f*spacing), GameData.WEAPON_ICON_WIDTH, GameData.TEX_DARTGUN_ICON, scale);
		
		generateIcon(new Vector2f(x2-0.4f, y - 3.3f*spacing), GameData.POWER_UP_WIDTH, GameData.TEX_DAMAGE_POWER_UP, scale);
		generateIcon(new Vector2f(x2-0.4f, y - 5.3f*spacing), GameData.POWER_UP_WIDTH, GameData.TEX_SHOT_POWER_UP, scale);
		generateIcon(new Vector2f(x2-0.4f, y - 7.3f*spacing), GameData.POWER_UP_WIDTH, GameData.TEX_HEAL_POWER_UP, scale);
		generateIcon(new Vector2f(x2-0.4f, y - 9.3f*spacing), GameData.POWER_UP_WIDTH, GameData.TEX_SHIELD_POWER_UP, scale);
		
		generateTextEntity(new Vector2f(x2,y - 3*spacing), .06f, "DAMAGE UP");
		generateTextEntity(new Vector2f(x2,y - 5*spacing), .06f, "EXTRA SHOT");
		generateTextEntity(new Vector2f(x2,y - 7*spacing), .06f, "HEAL");
		generateTextEntity(new Vector2f(x2,y - 9*spacing), .06f, "SHIELD RELOAD");
		
	}
	/**
	 * Easily construct a text entity
	 */
	private static void generateTextEntity(Vector2f pos, float scale, String text){
		 Entity t = new Entity(pos);
		 t.setScale(new Vector2f(scale,scale));
		 TextComponent txt = new TextComponent(t, Color.BLACK, GameData.FONT_VIDEO);
		 txt.setText(text);
	}
	/**
	 * Construct appropriate state entities.
	 */
	private void Intructions3Entities(){
		Color sel = new Color(16,62,229,255);
		Color unsel = new Color(3,10,38,180);
		float buttfps = 12f;
		Vector2f buttscale = new Vector2f(.25f,.1f);
		Button menubutton = new Button(new Vector2f(0f, -.8f), GameData.TEX_MAIN_MENU_TEXT, GameData.TEXT_WIDTH, buttfps, State.INSTRUCTIONS_3){
			@Override
			public void buttonAction() {
				setCurrentState(State.MENU);
			}
		};

		menubutton.setScale(buttscale);
		menubutton.setSelectedColor(sel);
		menubutton.setUnselectedColor(unsel);
		

		Button prevbutton = new Button(new Vector2f(-.6f, -.8f), GameData.TEX_PREV_PAGE_TEXT, GameData.TEXT_WIDTH, buttfps, State.INSTRUCTIONS_3){
			@Override
			public void buttonAction() {
				setCurrentState(State.INSTRUCTIONS_2);
			}
		};

		prevbutton.setScale(buttscale);
		prevbutton.setSelectedColor(sel);
		prevbutton.setUnselectedColor(unsel);
		
		generateTextEntity(new Vector2f(0f,.9f), .1f, "DEV OPTIONS AND CREDITS");
		float scale = .07f;
		float spacing = .12f, y0 = .75f;
		generateTextEntity(new Vector2f(0f,y0 - 0*spacing), scale, "PRESS H TO TOGGLE ENEMY HITBOXES");
		generateTextEntity(new Vector2f(0f,y0 - .8f*spacing), 0.8f*scale, "THESE HITBOXES WERE CREATED USING A SELF-DEVELOPED TOOL");
		generateTextEntity(new Vector2f(0f,y0 - 2*spacing), scale, "PRESS I TO TOGGLE PARTICLES");
		generateTextEntity(new Vector2f(0f,y0 - 2.8f*spacing), 0.8f*scale, "THIS CAN HELP IF YOU EXPERIENCE PERFORMANCE ISSUES");
		generateTextEntity(new Vector2f(0f,y0 - 4*spacing), scale, "PRESS C TO TOGGLE INACCURATE COLLISIONS");
		generateTextEntity(new Vector2f(0f,y0 - 4.8f*spacing), 0.8f*scale, "THIS CAN HELP PERFORMANCE, BY USING AABB I.S.O. SAT");
		generateTextEntity(new Vector2f(0f,y0 - 6*spacing), scale, "PRESS P TO TOGGLE ENTITY UPDATES");
		generateTextEntity(new Vector2f(0f,y0 - 6.8f*spacing), 0.8f*scale, "ALLOWS YOU TO INSPECT THE CURRENT FRAME");
		
		generateTextEntity(new Vector2f(0f,y0 - 8*spacing), scale, "PRESS T TO TOGGLE CONCURRENT UPDATES");
		generateTextEntity(new Vector2f(0f,y0 - 8.8f*spacing), 0.8f*scale, "CAN HANDLE MORE ENTITY UPDATES BUT IS EXPERIMENTAL");
		
		
		generateTextEntity(new Vector2f(0f,y0 - 10.0f*spacing), 0.8f*scale, "original music by marco");
		generateTextEntity(new Vector2f(0f,y0 - 10.4f*spacing), 0.8f*scale, "original art by rita");
		generateTextEntity(new Vector2f(0f,y0 - 10.8f*spacing), 0.8f*scale, "original sound fx by kieran and marco");
	}
	/**
	 * Easily construct an icon entity
	 */
	private static void generateIcon(Vector2f pos,int w, Texture icon, float scale){
		Entity e = new Entity(pos);
		new SpriteComponent(e, w, 0f, icon).setColor(Color.WHITE);;
		e.setScale(new Vector2f(0,scale));
		new ScaleOscComponent(e, new Oscillator(.5f, scale, 0, Util.randInRange(0f, 3.14f), OscType.SINE), new Vector2f(1f,0f));
	}
	/**
	 * Construct appropriate state entities.
	 */
	private void MenuEntities(){

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
				setCurrentState(State.INSTRUCTIONS_1);
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

		SpawnFactory.spawnNumber(new Vector2f(.1f, .8f), .1f, HighScoreLoader.getCurrentHighScore(), hscol);

		Entity musicOn = new Entity(new Vector2f(.3f, -.1f)){
			@Override
			public void draw() {
				if(Game.isMusicEnabled()) {
					super.draw();
				}
			}
		};
		SpriteComponent monsc = new SpriteComponent(musicOn, GameData.TEXT_WIDTH, 0f, GameData.TEX_ON_TEXT);
		musicOn.setScale(buttscale);
		monsc.setColor(unsel);

		Entity musicOff = new Entity(new Vector2f(.3f, -.1f)){
			@Override
			public void draw() {
				if(!Game.isMusicEnabled()) {
					super.draw();
				}
			}
		};
		SpriteComponent moffsc = new SpriteComponent(musicOff, GameData.TEXT_WIDTH, 0f, GameData.TEX_OFF_TEXT);
		musicOff.setScale(buttscale);
		moffsc.setColor(unsel);

		Entity soundOn = new Entity(new Vector2f(.35f, -.3f)){
			@Override
			public void draw() {
				if(Game.isSoundEnabled()) {
					super.draw();
				}
			}
		};
		SpriteComponent sonsc = new SpriteComponent(soundOn, GameData.TEXT_WIDTH, 0f, GameData.TEX_ON_TEXT);
		soundOn.setScale(buttscale);
		sonsc.setColor(unsel);

		Entity soundOff = new Entity(new Vector2f(.35f, -.3f)){
			@Override
			public void draw() {
				if(!Game.isSoundEnabled()) {
					super.draw();
				}
			}
		};
		SpriteComponent soffsc = new SpriteComponent(soundOff, GameData.TEXT_WIDTH, 0f, GameData.TEX_OFF_TEXT);
		soundOff.setScale(buttscale);
		soffsc.setColor(unsel);
		//SpawnFactory.SpawnEnemy6(new Vector2f(0f,1.2f));

	}
	/**
	 * Construct appropriate state entities.
	 */
	private void GameEntities(){
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
	 * Construct appropriate state entities.
	 */
	private void Instructtions1Entities(){
		Entity instr = new Entity(Vector2f.ZERO);
		SpriteComponent spr1 = new SpriteComponent(instr, GameData.INSTRUCTION_PAGE_WIDTH, 0f, GameData.TEX_INSTRUCTIONS_PAGE);
		spr1.setColor(Color.WHITE);

		Entity mouse = new Entity(new Vector2f(.22f, -.26f));
		SpriteComponent spr2 = new SpriteComponent(mouse, GameData.MOVING_MOUSE_WIDTH, 10f, GameData.TEX_MOVING_MOUSE);
		spr2.setColor(Color.WHITE);
		mouse.setScale(new Vector2f(.15f,.15f));

		Entity scroll = new Entity(new Vector2f(-.22f, -.3f));
		SpriteComponent spr3 = new SpriteComponent(scroll, GameData.MOVING_MOUSE_WIDTH, 10f, GameData.TEX_SCROLLING_MOUSE);
		spr3.setColor(Color.WHITE);
		scroll.setScale(new Vector2f(.15f,.15f));

		Color sel = new Color(16,62,229,255);
		Color unsel = new Color(3,10,38,180);
		float buttfps = 12f;
		Vector2f buttscale = new Vector2f(.25f,.1f);
		Button menubutton = new Button(new Vector2f(0f, -.8f), GameData.TEX_MAIN_MENU_TEXT, GameData.TEXT_WIDTH, buttfps, State.INSTRUCTIONS_1){
			@Override
			public void buttonAction() {
				setCurrentState(State.MENU);
			}
		};

		menubutton.setScale(buttscale);
		menubutton.setSelectedColor(sel);
		menubutton.setUnselectedColor(unsel);
		
		Button nextbutton = new Button(new Vector2f(.6f, -.8f), GameData.TEX_NEXT_PAGE_TEXT, GameData.TEXT_WIDTH, buttfps, State.INSTRUCTIONS_1){
			@Override
			public void buttonAction() {
				setCurrentState(State.INSTRUCTIONS_2);
			}
		};

		nextbutton.setScale(buttscale);
		nextbutton.setSelectedColor(sel);
		nextbutton.setUnselectedColor(unsel);


	}
	/**
	 * Construct appropriate state entities.
	 */
	private void ChooseGameEntities(){
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
	 * Construct appropriate state entities.
	 */
	private void GameOverEntities(){
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
