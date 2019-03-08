package engine.gui;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;


import engine.component.MouseMoveControlComponent;
import engine.component.SpriteComponent;
import engine.entity.Entity;
import engine.input.EventHandler;

public abstract class Button extends Entity {
	
	private SpriteComponent sprite;
	private boolean mouse_inside = false;
	private Color hoverColor = new Color(255,255,255,255);

	public Button(Vector2f position, Texture tex, int width, float fps) {
		super(position);
		sprite = new SpriteComponent(this, width, fps, tex);
		new MouseMoveControlComponent(this){
			@Override
			public void onRightMousePress(Vector2f worldpos) {}
			@Override
			public void onLeftMousePress(Vector2f worldpos) {
				Button b = (Button)entity;
				if(b.isMouseInside()){
					b.buttonAction();
				}
			}
			@Override
			public void onRightMouseRelease(Vector2f worldpos) {}
			@Override
			public void onLeftMouseRelease(Vector2f worldpos) {}
			@Override
			public void onMouseMove(Vector2f worldpos) {
				mouse_inside = sprite.pointInside(EventHandler.getCurrentMouseWorldPosition());						
			}			
		};
	}
	
	public abstract void buttonAction();
	
	public void setHoverColor(Color c){
		hoverColor = c;
	}
	
	public Color getHoverColor(){
		return hoverColor;
	}

	@Override
	public void draw() {
		sprite.setColor(mouse_inside? hoverColor : new Color(255,255,255,100));
		super.draw();
	}
	
	public boolean isMouseInside(){
		return mouse_inside;
	}
	
	

}
