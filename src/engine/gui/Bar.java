package engine.gui;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.system.Vector2f;

import engine.entity.Entity;
import util.Util;

public class Bar {
	private Vector2f dimensions;
	private RectangleShape frame;
	private RectangleShape bar;
	private Color full, empty;
	
	
	public Bar(float width, float height, Color empty, Color full) {
		this.empty = empty;
		this.full = full;
		this.dimensions = new Vector2f(width, height);
		frame = new RectangleShape(dimensions);
		frame.setOrigin(Vector2f.mul(dimensions, .5f));		
		frame.setFillColor(Color.TRANSPARENT);
		frame.setOutlineColor(new Color(0,0,0,190));
		frame.setOutlineThickness(.05f);		
		bar = new RectangleShape(dimensions);
		bar.setOrigin(Vector2f.mul(dimensions, .5f));
	}
	
	public void setPosition(Vector2f pos){
		frame.setPosition(pos);
		bar.setPosition(pos);
	}
	
	public void setScale(Vector2f scale){
		frame.setScale(scale);
		bar.setScale(scale);
	}
	
	public void draw(float value, float maxvalue){
		float factor = (value/maxvalue);
		Vector2f size = new Vector2f(dimensions.x *  factor, dimensions.y);		
		bar.setSize(size);
		bar.setFillColor(Util.lerpColor(factor, empty, full));
	    Entity.graphics.drawToRenderTexture(bar);
		Entity.graphics.drawToRenderTexture(frame);
	}
	
	public void setFrameColor(Color f){
		frame.setOutlineColor(f);
	}
	
}
