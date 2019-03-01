package game;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundBuffer;
import org.jsfml.audio.SoundSource;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.Image;
import org.jsfml.graphics.Texture;
import org.jsfml.graphics.TextureCreationException;
import org.jsfml.system.Vector2f;

/**A class that contains the game data, including hitbox data, fonts, sound, music and textures. 
 * @author Marco
 *
 */
public class GameData {

	//fonts
	public static final Font FONT_CALIBRI = loadFont("Calibri");

	//sounds
	public static final SoundBuffer SOUND_LAZELIEN = loadSound("lazelien");
	public static final SoundBuffer SOUND_PEEG = loadSound("peeg"); 
	public static final SoundBuffer SOUND_BASS = loadSound("bass");


	/**Load a font from the font folder.
	 * @param name The name without the extension. Expects a .ttf file.
	 * @return The font
	 */
	public static Font loadFont(String name){
		Font f = new Font();
		try {
			f.loadFromFile(Paths.get("font\\" + name + ".ttf"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return f;
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
		Path path = Paths.get("sprites\\" + name + ".png");
		Texture tex = new Texture();
		Image image = new Image();			
		try {
			image.loadFromFile(path);
			tex.loadFromImage(image);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TextureCreationException e) {
			e.printStackTrace();
		}	
		return tex;
	}

	public static SoundBuffer loadSound(String name){
		SoundBuffer buf = new SoundBuffer();
		try {
			buf.loadFromFile(Paths.get("sound\\" + name + ".wav"));		
			
		} catch (IOException e) {					
			e.printStackTrace();
		}
		return buf;
	}

}
