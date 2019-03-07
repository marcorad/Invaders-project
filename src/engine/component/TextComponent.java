package engine.component;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.Text;
import org.jsfml.graphics.TextStyle;
import org.jsfml.system.Vector2f;

import engine.entity.Entity;
import engine.graphics.GraphicsHandler;
import engine.input.EventHandler;
import game.Game;

/**A graphical component that contains text
 * @author Marco
 *
 */
public class TextComponent extends ColorComponent {

	private Text text;	
	private Vector2f normalisedscale;
	
	/**Constrcuts a text component.
	 * @param entity The active entity
	 * @param color The color
	 * @param font The font
	 */
	public TextComponent(Entity entity, Color color, Font font) {
		super(entity, color);
		text = new Text("", font, Game.HEIGHT);	
		text.setStyle(TextStyle.REGULAR);
		text.setColor(color);
		text.setPosition(entity.getPosition());
		//normalise the scale occording to height and invert
		normalisedscale = new Vector2f(1.f/(float)Game.HEIGHT,-1.f/(float)Game.HEIGHT);
		onScaleUpdate();
	}
	
	@Override
	protected void colorUpdate() {
		text.setColor(color);;
	}
	
	@Override
	public void draw(GraphicsHandler graphics) {
		graphics.drawToRenderTexture(text);
	}

	@Override
	public void onPositionUpdate() {
		text.setPosition(entity.getPosition());
	}

	@Override
	public void onRotationUpdate() {
		text.setRotation(entity.getTheta());
	}


	@Override
	public void onScaleUpdate() {
		text.setScale(Vector2f.componentwiseMul(normalisedscale, entity.getScale()));
	}

	/**
	 * Realligns the centre of the text
	 */
	private void reallignOrigin(){
		FloatRect r = text.getLocalBounds();
		text.setOrigin(r.width/2.f, r.height/2.f);
		r = text.getGlobalBounds();		
	}

	/**
	 * @param s The text
	 */
	public void setText(String s){
		text.setString(s);	
		reallignOrigin();
	}

}
