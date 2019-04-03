package State;

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
		currentState = State.MENU;
	}
	
	public State getCurrentState(){
		return currentState;
	}
	
	public void setCurrentState(State currentState){
		//clearEntities(); needs to be implemented in EntityManager.java
		this.currentState = currentState;
		switch(currentState){
			case MENU: {
				//populate MenuEntities();
				System.out.println("MENU state was chosen.");
				break;
			}
			case GAME: {
				//populate GameEntities();
				System.out.println("GAME state was chosen.");
				break;
			}
			case INSTRUCTIONS: {
				//populate InstructEntities();
				System.out.println("INSTRUCTIONS state was chosen.");
				break;
			}
			case GAME_OVER: {
				//populate GameOverEntities();
				System.out.println("GAME_OVER state was chosen.");
				break;
			}
		}
	}
	
	public static void main(String[] args) {
		StateMachine a = new StateMachine();
		a.setCurrentState(State.MENU);
	}
}
