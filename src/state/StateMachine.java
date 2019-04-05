package state;

import org.jsfml.graphics.Color;
import org.jsfml.system.Vector2f;

import engine.component.SpriteComponent;
import engine.component.TextComponent;
import engine.entity.Entity;
import engine.entity.Player;
import engine.entity.SpawnFactory;
import engine.gui.Button;
import game.Game;
import game.GameData;

/**A class that specifies the different states of the program.
 * @author Kieran
 *
 */
public class StateMachine {
	public enum State{
		MENU, GAME, INSTRUCTIONS, GAME_OVER;
	}

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
		}
	}


	/**
	 * Construct all the required menu entities
	 */
	public void MenuEntities(){

		Color sel = new Color(16,62,229,255);
		Color unsel = new Color(3,10,38,180);
		float buttfps = 12f;
		Vector2f buttscale = new Vector2f(.35f,.08f);
		Button playbutton = new Button(new Vector2f(0f, .3f), GameData.TEX_PLAY_TEXT, GameData.TEXT_WIDTH, buttfps, State.MENU){
			@Override
			public void buttonAction() {
				setCurrentState(State.GAME);
			}
		};

		playbutton.setScale(buttscale);
		playbutton.setSelectedColor(sel);
		playbutton.setUnselectedColor(unsel);

		Button quitbutton = new Button(new Vector2f(0f, -.1f), GameData.TEX_QUIT_TEXT, GameData.TEXT_WIDTH, buttfps, State.MENU){
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
		
		Entity hs_text = new Entity(new Vector2f(-.5f, -.8f));
		new SpriteComponent(hs_text, GameData.TEXT_WIDTH, buttfps, GameData.TEX_HIGH_SCORE_TEXT);
		hs_text.setScale(buttscale);
		
		Entity hs = new Entity(new Vector2f(.5f, -.73f));
		TextComponent hstxt = new TextComponent(hs, Color.BLACK, GameData.FONT_CALIBRI);
		hstxt.setText(Long.toString(Game.getHighScore()));
		hs.setScale(new Vector2f(.25f,.25f));

	}

	/**
	 * Construct all the required game entities
	 */
	public void GameEntities(){
		Game.setCurrentPlayer(new Player(new Vector2f(0f, -.85f)));
		for(float x = -.5f; x <= .5f; x+=.25f)
			SpawnFactory.spawnWaveEnemy(new Vector2f(x, .8f));

	}

	/**
	 * Construct all the required instruction page entities
	 */
	public void InstructEntities(){
		Entity instr = new Entity(Vector2f.ZERO);
		SpriteComponent spr1 = new SpriteComponent(instr, GameData.INSTRUCTION_PAGE_WIDTH, 0f, GameData.TEX_INSTRUCTIONS_PAGE);
		spr1.setColor(Color.WHITE);
		
		Entity mouse = new Entity(new Vector2f(.3f, -.7f));
		SpriteComponent spr2 = new SpriteComponent(mouse, GameData.MOVING_MOUSE_WIDTH, 10f, GameData.TEX_MOVING_MOUSE);
		spr2.setColor(Color.WHITE);
		mouse.setScale(new Vector2f(.2f,.2f));
		
		Color sel = new Color(16,62,229,255);
		Color unsel = new Color(3,10,38,180);
		float buttfps = 12f;
		Vector2f buttscale = new Vector2f(.35f,.15f);
		Button menubutton = new Button(new Vector2f(-.6f, -.8f), GameData.TEX_PLAY_TEXT, GameData.TEXT_WIDTH, buttfps, State.INSTRUCTIONS){
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
		Button menubutton = new Button(new Vector2f(0f, -.8f), GameData.TEX_PLAY_TEXT, GameData.TEXT_WIDTH, buttfps, State.GAME_OVER){
			@Override
			public void buttonAction() {
				setCurrentState(State.MENU);
			}
		};
		
		menubutton.setScale(buttscale);
		menubutton.setSelectedColor(sel);
		menubutton.setUnselectedColor(unsel);
	}
}
