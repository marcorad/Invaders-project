package engine.component;

import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.event.KeyEvent;

import engine.entity.Entity;
import engine.graphics.GraphicsHandler;
import engine.input.EventHandler;
import engine.input.KeyListener;

/**
 * Specifies a game-focused type of keyboard listener.
 * @author Marco
 *
 */
public abstract class KeyboardMoveComponent  extends Component implements KeyListener {
	protected Key upKey, downKey, leftKey, rightKey, specialKey1, specialKey2;	
	protected MovementComponent movement; //the movement component to control

	private boolean dirs[] = {false, false, false, false}; //UDLR

	/**
	 * @param eventhandler The event handler
	 * @param entity The active entity
	 * @param upKey Up
	 * @param downKey Down
	 * @param leftKey Left
	 * @param rightKey Right
	 * @param specialKey1 Special 1
	 * @param specialKey2 Special 2
	 * @param movement The movement component to be modified according to user specification
	 */
	public KeyboardMoveComponent(Entity entity,
			Key upKey, Key downKey, Key leftKey, Key rightKey, Key specialKey1, Key specialKey2,
			MovementComponent movement) {
		super (entity);
		this.upKey = upKey;
		this.downKey = downKey;
		this.leftKey = leftKey;
		this.rightKey = rightKey;
		this.specialKey1 = specialKey1;
		this.specialKey2 = specialKey2;
		this.movement = movement;		
	    Entity.eventhandler.attachKeyListener(this);
	}
	/**
	 * @return Down
	 */
	public Key getDownKey() {
		return downKey;
	}
	/**
	 * @return Left
	 */
	public Key getLeftKey() {
		return leftKey;
	}
	/**
	 * @return Right
	 */
	public Key getRightKey() {
		return rightKey;
	}
	/**
	 * @return Special 1
	 */
	public Key getSpecialKey1() {
		return specialKey1;
	}

	/**
	 * @return Special 2
	 */
	public Key getSpecialKey2() {
		return specialKey2;
	}

	/**
	 * @return Up
	 */
	public Key getUpKey() {
		return upKey;
	}

	@Override
	public boolean isUseless() {
		return entity.shouldBeRemoved();
	}


	/**User specified method when a key press is triggered
	 * @param dir The current directions of the keys pressed
	 */
	public abstract void onDirection(Vector2f dir);

	@Override
	public void onKeyPress(KeyEvent ke) {

		boolean pressed = false;
		if(ke.key == upKey){
			dirs[0] = true;
			pressed = true;
		} else if(ke.key == downKey){
			dirs[1] = true;
			pressed = true;
		} else if(ke.key == leftKey){
			dirs[2] = true;
			pressed = true;
		} else if(ke.key == rightKey){
			dirs[3] = true;
			pressed = true;
		} else if(ke.key == specialKey1){
			special1Pressed();
		} else if(ke.key == specialKey2){
			special2Pressed();
		}

		if(pressed)
			sendDir();
	}

	@Override
	public void onKeyRelease(KeyEvent ke) {

		boolean pressed = false;
		if(ke.key == upKey){
			dirs[0] = false;
			pressed = true;
		} else if(ke.key == downKey){
			dirs[1] = false;
			pressed = true;
		} else if(ke.key == leftKey){
			dirs[2] = false;
			pressed = true;
		} else if(ke.key == rightKey){
			dirs[3] = false;
			pressed = true;
		} else if(ke.key == specialKey1){
			special1Released();
		} else if(ke.key == specialKey2){
			special2Released();
		}
		
		if(pressed)
			sendDir();
	}

	private void sendDir(){
		float x = .0f, y = .0f;
		y += dirs[0]?  1f : .0f;
		y += dirs[1]?  -1f : .0f;
		x += dirs[2]?  -1f : .0f;
		x += dirs[3]?  1f : .0f;
		Vector2f dir = new Vector2f(x,y);
		onDirection(dir);
	}

	public void setDownKey(Key downKey) {
		this.downKey = downKey;
	}

	public void setLeftKey(Key leftKey) {
		this.leftKey = leftKey;
	}

	public void setRightKey(Key rightKey) {
		this.rightKey = rightKey;
	}

	public void setSpecialKey1(Key specialKey) {
		this.specialKey1 = specialKey;
	}

	public void setSpecialKey2(Key specialKey2) {
		this.specialKey2 = specialKey2;
	}

	public void setUpKey(Key upKey) {
		this.upKey = upKey;
	}

	/**
	 * Called when the special 1 key is pressed
	 */
	public abstract void special1Pressed();
	
	/**
	 * Called when the special 1 key is released
	 */
	public abstract void special1Released();

	/**
	 * Called when the special 2 key is pressed
	 */
	public abstract void special2Pressed();

	/**
	 * Called when the special 2 key is released
	 */
	public abstract void special2Released(); 	

}
