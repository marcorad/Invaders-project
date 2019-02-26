package game;

import org.jsfml.graphics.Font;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;

/**A class that contains the game data, including hitbox data, fonts, sound, music and textures. 
 * @author Marco
 *
 */
public class GameData {
	public static final Font CALIBRI_FONT;
	
	//load all the data
	static{
		//fonts
		CALIBRI_FONT = loadFont("Calibri");
		
		//hitbox
		
		//tectures
		
		//sounds
		
		//music
	}

	/**Load a font from the font folder.
	 * @param name The name without the extension. Expects a .ttf file.
	 * @return The font
	 */
	public static Font loadFont(String name){
		return null;
	}
	
	/**Load hitbox data from the hitboxdata folder.
	 * @param name The name without extension
	 * @return An array containing points of multiple hitboxes ([points][hitbox number])
	 */
	public static Vector2f[][] loadHitboxData(String name){
		return null;
	}
	
	/**Load a texture from the sprites folder
	 * @param name The name without the extension. Expects a .png file.
	 * @return The texture
	 */
	public static Texture loadTexture(String name){
		return null;
	}
	
}
