package engine.entity;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;

import engine.component.SpriteComponent;
import game.GameData;

public class WeaponIcon extends Entity {
	
	private boolean selected;
	private SpriteComponent sprite;

	public WeaponIcon(Vector2f position, Texture icon) {
		super(position);
		this.setScale(new Vector2f(.08f,.08f));
		sprite = new SpriteComponent(this, GameData.WEAPON_ICON_WIDTH, 0f, icon);
		setSelected(false);
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
		if(this.selected){
			sprite.setColor(new Color(255,255,255,225));
		} else {
			sprite.setColor(new Color(75,75,75,50));
		}
	}	
	

}
