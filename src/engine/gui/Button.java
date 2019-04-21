package engine.gui;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;

import engine.component.MouseMoveControlComponent;
import engine.component.SpriteComponent;
import engine.entity.Entity;
import engine.input.EventHandler;
import game.Game;
import game.GameData;
import state.StateMachine.State;

/**
 * A GUI button element.
 */
public abstract class Button extends Entity {
	
	private SpriteComponent sprite;
	private boolean mouse_inside;
	private Color selectedColor = new Color(0,0,0,255), unselectedColor= new Color(0,0,0,150);
	private State activeState;
	private MouseMoveControlComponent mmc ;

	/**Creates a button that has a specified sprite
	 * @param position Centre of the button
	 * @param tex Texture of the sprite
	 * @param width Width of a single frame
	 * @param fps FPS of the animation
	 * @param activeState The state in which this button is active in. Required in order to know when to remove it from listening to the mouse.
	 */
	public Button(Vector2f position, Texture tex, int width, float fps, State activeState) {
		super(position);
		this.activeState  = activeState;
		sprite = new SpriteComponent(this, width, fps, tex);
		mmc = new MouseMoveControlComponent(this){
			@Override
			public boolean isUseless() {
				return Game.stateMachine.getCurrentState() != activeState;
			}
			@Override
			public void onRightMousePress(Vector2f worldpos) {}
			@Override
			public void onLeftMousePress(Vector2f worldpos) {
				Button b = (Button)entity;
				if(b.isMouseInside()){
					b.buttonAction();
					GameData.playSound(GameData.SOUND_ENEMY_HITS[1]);
				}
			}
			@Override
			public void onRightMouseRelease(Vector2f worldpos) {}
			@Override
			public void onLeftMouseRelease(Vector2f worldpos) {}
			@Override
			public void onMouseMove(Vector2f worldpos) {
				mouse_inside = sprite.pointInside(worldpos);					
			}			
		};		
		//mmc.onMouseMove(EventHandler.getCurrentMouseWorldPosition()); //update immediately
	}
	
	public abstract void buttonAction();
	
	public void setSelectedColor(Color c){
		selectedColor = c;
	}
	
	public Color getSelectedColor(){
		return selectedColor;
	}

	@Override
	public void draw() {
		mmc.onMouseMove(EventHandler.getCurrentMouseWorldPosition());
		sprite.setColor(mouse_inside? selectedColor : unselectedColor);
		super.draw();
	}
	
	public boolean isMouseInside(){
		return mouse_inside;
	}
	
	public void setUnselectedColor(Color c){
		unselectedColor = c;
	}
	
	public Color getUnselectedColor(){
		return unselectedColor;
	}
	
	
	

}
