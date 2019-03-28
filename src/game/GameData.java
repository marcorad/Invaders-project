package game;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundBuffer;
import org.jsfml.audio.SoundSource;
import org.jsfml.audio.SoundSource.Status;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.Image;
import org.jsfml.graphics.Texture;
import org.jsfml.graphics.TextureCreationException;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import util.Util;

/**A class that contains the game data, including hitbox data, fonts, sound, music and textures. 
 * @author Marco
 *
 */
public class GameData {
	
	
	//fonts
	public static final Font FONT_CALIBRI = loadFont("Calibri");

	//sounds
	public static final Sound SOUND_LAZELIEN = loadSound("lazelien");
	public static final Sound SOUND_PEEG = loadSound("peeg"); 
	public static final Sound SOUND_BASS = loadSound("bass");

	static{
		SOUND_PEEG.setVolume(30f);
	}

	//images
	public static final Texture TEX_GAME_BACKGROUND = loadTexture("gamebg");
	public static final Texture TEX_PLAYER = loadTexture("player");
	public static final Texture TEX_BUCKSHOT = loadTexture("buckshot");
	public static final Texture TEX_EXAMPLE_ENEMY = loadTexture("example enemy");
	public static final Texture TEX_TEST_BUTTON = loadTexture("testbutton");
	public static final Texture TEX_BENNY_THE_FEESH = loadTexture("benny the feesh");
	
	
	public static final Texture TEX_RAIL_SLUG = loadTexture("Projectile Rail Slug");
	public static final Vector2f[] HB_RAIL_SLUG = loadHitboxData("Projectile Rail Slug");
	
	public static final Texture TEX_ROCKET = loadTexture("Projectile Rocket");
	public static final Vector2f[] HB_ROCKET = loadHitboxData("Projectile Rocket");
	
	public static final Texture TEX_BULLET = loadTexture("Projectile Bullet");
	public static final Vector2f[] HB_BULLET = loadHitboxData("Projectile Bullet");
	
	public static final Texture TEX_SHIELD = loadTexture("Shield");
	public static final Vector2f[] HB_SHIELD = loadHitboxData("Shield");
	
	public static final Vector2f[] HB_BENNY = loadHitboxData("benny the feesh");
	

	static{
		TEX_EXAMPLE_ENEMY.setSmooth(true);
		//TEX_TEST_BUTTON.setSmooth(true);
		TEX_PLAYER.setSmooth(true);
		//TEX_GAME_BACKGROUND.setSmooth(true);
		TEX_BENNY_THE_FEESH.setSmooth(true);
	}

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

	/**Load a sound with a name from the sound folder
	 * @param name The name without the extension. Expects a .wav file.
	 * @return The object buffering the sound data
	 */
	public static Sound loadSound(String name){
		SoundBuffer buf = new SoundBuffer();
		try {
			buf.loadFromFile(Paths.get("sound\\" + name + ".wav"));		

		} catch (IOException e) {					
			e.printStackTrace();
		}
		return new Sound(buf);
	}

	public static Texture loadBlurryAnimation(String name, int w, int num_between_frames){
		Path path = Paths.get("sprites\\" + name + ".png");
		Image original = new Image();		
		Texture tex = new Texture();
		try {			
			original.loadFromFile(path);
			Image smoothimage = new Image();	
			Vector2i origsize = original.getSize();
			int numframes  = origsize.x/w;
			smoothimage.create(origsize.x*(num_between_frames + 1), origsize.y);
			Vector2i newsize = smoothimage.getSize();

			for(int currframe = 0; currframe < numframes; currframe++){
				for(int x = 0; x < w; x++){
					for(int y = 0; y < origsize.y; y++){					

						Color pixcurr = original.getPixel(currframe*w + x, y);
						Color pixnext = original.getPixel(((currframe+1)*w + x)%origsize.x, y);

						int smoothbeginx = x+w*currframe*(num_between_frames+1);
						int smoothendx = x+(w*(currframe+1))*(num_between_frames+1);
						if(currframe == 1) System.out.println(smoothbeginx + " " + smoothendx);
						float lerpfactor = 0f;	
						for(int smoothx = smoothbeginx; smoothx < smoothendx; smoothx += w ){
							smoothimage.setPixel(smoothx, y, Util.lerpColor(lerpfactor, pixcurr, pixnext));
							lerpfactor += 1f/(num_between_frames+1);
						}

					}
				}
			}

			tex.loadFromImage(smoothimage);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TextureCreationException e) {
			e.printStackTrace();
		}



		return tex;
	}
	
	/**Load hitbox data from the hitboxdata folder.
	 * @param name The name without extension
	 * @return An array containing points of multiple hitboxes ([points][hitbox number])
	 */
	public static Vector2f[] loadHitboxData(String name){
		DataInputStream dis;
		try {
			dis = new DataInputStream(new FileInputStream("hbdata\\"+name+".hbd"));
			int size = dis.readInt();
			Vector2f[] hb = new Vector2f[size];
			for(int i = 0; i < size; i++){
				hb[i] = new Vector2f(dis.readFloat(), dis.readFloat());
			}
			dis.close();
			return hb;
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void playSound(Sound s){
		if(s.getStatus() != Status.PLAYING){
			s.play();
		}
		
	}

}
