package game;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jsfml.audio.Music;
import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundBuffer;
import org.jsfml.audio.SoundSource.Status;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.Image;
import org.jsfml.graphics.Texture;
import org.jsfml.graphics.TextureCreationException;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import util.Util;

/**
 * A class that contains the game data, including hitbox data, fonts, sound, music and textures. 
 */
public class GameData {

	private static String P_ENT = "entity//";
	private static String P_INSTR = "instructions//";
	private static String P_PU = "power up//";
	private static String P_PROJ = "projectile//";
	private static String P_TXT = "text//";
	private static String P_WEAP = "weapon//";
	private static String P_MUS = "music//";
	private static String P_SND_PPROJ = "player projectile//";
	private static String P_SND_EPROJ = "enemy projectile//";
	private static String P_SND_EHIT = "enemy hit//";
	private static String P_SND_PL = "player//";

	public static final int TEXT_WIDTH = 472;
	public static final int ENEMY_WIDTH = 64;
	public static final int PLAYER_WIDTH = 128;
	public static final int SHIELD_WIDTH = 128;
	public static final int INSTRUCTION_PAGE_WIDTH = 2048;
	public static final int MOVING_MOUSE_WIDTH = 128;
	public static final int ENEMY_PROJECTILE_WIDTH = 256;
	public static final int POWER_UP_WIDTH = 256;
	public static final int PROJECTILE_WIDTH = 128;
	public static final int NUMBER_WIDTH = 64;
	public static final int WEAPON_ICON_WIDTH = 64;


	//fonts
	public static Font FONT_VIDEO;//Video by Lucas de Groot, Alexander Lange. Obtained from: https://www.1001freefonts.com/new-fonts-2.php


	//sounds
	public static Sound SOUND_MACHINE_GUN;
	public static Sound SOUND_POISON_GUN;
	public static Sound SOUND_ROCKET;
	public static Sound SOUND_DART_GUN;
	
	public static Sound SOUND_ENEMY_PROJECTILES[] = new Sound[2];
	public static Sound SOUND_ENEMY_HITS[] = new Sound[6];
	
	public static Sound SOUND_PLAYER_HIT;
	public static Sound SOUND_SHIELD;


	//music
	public static Music MUSIC_GAME;
	public static Music MUSIC_INTRO;
	public static Music MUSIC_GAME_OVER;


	//textures and hitboxes	
	public static Texture TEX_PLAYER;
	public static Vector2f[] HB_PLAYER;

	public static Texture TEX_DART;
	public static Vector2f[] HB_DART;

	public static Texture TEX_ROCKET;
	public static Vector2f[] HB_ROCKET;

	public static Texture TEX_BULLET;
	public static Vector2f[] HB_BULLET;

	public static Texture TEX_SHIELD;
	public static Vector2f[] HB_SHIELD;

	public static Texture TEX_POISON;
	public static Vector2f[] HB_POISON;
	
	public static Texture TEX_CROSS_PROJECTILE;
	public static Vector2f[] HB_CROSS_PROJECTILE;
	
	public static Texture TEX_STARRY_PROJECTILE;
	public static Vector2f[] HB_STARRY_PROJECTILE;

	public static Vector2f[] HB_POWERUP;


	public static Texture TEX_ENEMY_BAT;
	public static Texture TEX_ENEMY_BEAR;
	public static Texture TEX_ENEMY_FROGGY;
	public static Texture TEX_ENEMY_WAVE;
	public static Texture TEX_ENEMY_VIRUS;
	public static Texture TEX_ENEMY_SUNNY;

	public static Vector2f[] HB_ENEMY_BAT;
	public static Vector2f[] HB_ENEMY_BEAR;
	public static Vector2f[] HB_ENEMY_FROGGY;
	public static Vector2f[] HB_ENEMY_WAVE;
	public static Vector2f[] HB_ENEMY_VIRUS;
	public static Vector2f[] HB_ENEMY_SUNNY;


	public static Texture TEX_POISON_ICON;
	public static Texture TEX_MACHINEGUN_ICON;
	public static Texture TEX_DARTGUN_ICON;
	public static Texture TEX_ROCKET_ICON;


	public static Texture TEX_EXIT_TEXT;
	public static Texture TEX_GAME_OVER_TEXT;
	public static Texture TEX_INSTRUCTIONS_TEXT;
	public static Texture TEX_MAIN_MENU_TEXT;
	public static Texture TEX_MUSIC_TEXT;
	public static Texture TEX_ON_TEXT;
	public static Texture TEX_OFF_TEXT;
	public static Texture TEX_PLAY_GAME_TEXT;
	public static Texture TEX_QUIT_TEXT;
	public static Texture TEX_SOUND_TEXT;
	public static Texture TEX_HIGH_SCORE_TEXT;
	public static Texture TEX_NEXT_PAGE_TEXT;
	public static Texture TEX_PREV_PAGE_TEXT;

	public static Texture TEX_INSTRUCTIONS_PAGE;
	public static Texture TEX_MOVING_MOUSE;
	public static Texture TEX_SCROLLING_MOUSE;

	public static Texture TEX_DAMAGE_POWER_UP;
	public static Texture TEX_HEAL_POWER_UP;
	public static Texture TEX_SHIELD_POWER_UP;
	public static Texture TEX_SHOT_POWER_UP;

	public static Texture TEX_CHOOSE_GAME_TEXT;
	public static Texture TEX_SHORT_TEXT;
	public static Texture TEX_LONG_TEXT;
	public static Texture TEX_ENDLESS_TEXT;

	public static Texture[] TEX_NUMBERS_TEXT = new Texture[10];

	/**
	 * Loads all the data required for the game.</br> 
	 * THIS WAS VERY TEDIOUS TO CODE XD
	 */
	public static void load(){
		FONT_VIDEO = loadFont("video");

		TEX_ENEMY_BAT = loadTexture(P_ENT + "bat");     
		TEX_ENEMY_BEAR = loadTexture(P_ENT + "bear");     
		TEX_ENEMY_FROGGY = loadTexture(P_ENT + "froggy");   
		TEX_ENEMY_WAVE = loadTexture(P_ENT + "wave");   
		TEX_ENEMY_VIRUS = loadTexture(P_ENT + "virus");    
		TEX_ENEMY_SUNNY = loadTexture(P_ENT + "sunny");    

		HB_ENEMY_BAT = loadHitboxData("Enemy Bat");  
		HB_ENEMY_BEAR = loadHitboxData("Enemy Bear");    
		HB_ENEMY_FROGGY = loadHitboxData("Enemy Froggy"); 
		HB_ENEMY_WAVE  = loadHitboxData("Enemy Wave");    
		HB_ENEMY_VIRUS = loadHitboxData("Enemy Virus");  
		HB_ENEMY_SUNNY = loadHitboxData("Enemy Sunny");

		MUSIC_GAME = loadMusic(P_MUS + "leave my space");
		MUSIC_GAME.setLoop(true);
		MUSIC_INTRO = loadMusic(P_MUS + "the beginning");
		MUSIC_INTRO.setLoop(true);
		MUSIC_GAME_OVER = loadMusic(P_MUS + "it's over");
		MUSIC_GAME_OVER.setLoop(true);
		
		TEX_CROSS_PROJECTILE      = loadTexture(P_PROJ + "cross");     
		HB_CROSS_PROJECTILE  	  =	loadHitboxData("cross");                                
		TEX_STARRY_PROJECTILE     = loadTexture(P_PROJ + "starry");    
		HB_STARRY_PROJECTILE      = loadHitboxData("starry");
		
		TEX_PLAYER = loadTexture(P_ENT +"player");
		HB_PLAYER = loadHitboxData("player");


		TEX_INSTRUCTIONS_PAGE = loadTexture(P_INSTR + "instructions page");
		TEX_MOVING_MOUSE = loadTexture(P_INSTR + "mouse");
		TEX_SCROLLING_MOUSE  = loadTexture(P_INSTR + "scroll");

		TEX_DART = loadTexture(P_PROJ + "dart");
		HB_DART = loadHitboxData("Projectile Dart");

		TEX_ROCKET = loadTexture(P_PROJ + "rocket");
		HB_ROCKET = loadHitboxData("Projectile Rocket");

		TEX_BULLET = loadTexture(P_PROJ + "bullet");
		HB_BULLET = loadHitboxData("Projectile Bullet");

		TEX_POISON= loadTexture(P_PROJ + "poison");
		HB_POISON = loadHitboxData("Projectile Poison");

		TEX_SHIELD = loadTexture(P_ENT +"shield");
		HB_SHIELD = loadHitboxData("Shield");

		TEX_POISON_ICON = loadTexture(P_WEAP +"poison");
		TEX_DARTGUN_ICON = loadTexture(P_WEAP +"dart");
		TEX_ROCKET_ICON = loadTexture(P_WEAP +"rocket");
		TEX_MACHINEGUN_ICON = loadTexture(P_WEAP +"machinegun");

		HB_POWERUP = loadHitboxData("Temp powerup");

		TEX_EXIT_TEXT            = loadTexture(P_TXT + "exit");
		TEX_GAME_OVER_TEXT       = loadTexture(P_TXT + "game over");
		TEX_INSTRUCTIONS_TEXT    = loadTexture(P_TXT + "instructions");
		TEX_MAIN_MENU_TEXT       = loadTexture(P_TXT + "main menu");
		TEX_MUSIC_TEXT           = loadTexture(P_TXT + "music");
		TEX_ON_TEXT              = loadTexture(P_TXT + "on small");
		TEX_OFF_TEXT             = loadTexture(P_TXT + "off small");
		TEX_PLAY_GAME_TEXT       = loadTexture(P_TXT + "play game");
		TEX_QUIT_TEXT            = loadTexture(P_TXT + "quit");
		TEX_SOUND_TEXT           = loadTexture(P_TXT + "sound");
		TEX_HIGH_SCORE_TEXT      = loadTexture(P_TXT + "high score");
		
		TEX_NEXT_PAGE_TEXT  = loadTexture(P_TXT + "next page");
		TEX_PREV_PAGE_TEXT  = loadTexture(P_TXT + "previous page");

		TEX_CHOOSE_GAME_TEXT     = loadTexture(P_TXT + "choose game");
		TEX_SHORT_TEXT           = loadTexture(P_TXT + "short");
		TEX_LONG_TEXT            = loadTexture(P_TXT + "long");
		TEX_ENDLESS_TEXT         = loadTexture(P_TXT + "endless");

		TEX_DAMAGE_POWER_UP      = loadTexture(P_PU + "damage");
		TEX_HEAL_POWER_UP        = loadTexture(P_PU + "heal");
		TEX_SHIELD_POWER_UP      = loadTexture(P_PU + "shield");
		TEX_SHOT_POWER_UP        = loadTexture(P_PU + "shot");		
		
		SOUND_MACHINE_GUN = loadSound(P_SND_PPROJ + "machine", 50f);
		SOUND_POISON_GUN = loadSound(P_SND_PPROJ + "poison", 30f);
		SOUND_ROCKET = loadSound(P_SND_PPROJ + "rocket", 30f);
		SOUND_DART_GUN = loadSound(P_SND_PPROJ + "dart", 80f);
		SOUND_ENEMY_PROJECTILES[0] = loadSound(P_SND_EPROJ + "1", 40f);
		SOUND_ENEMY_PROJECTILES[1] = loadSound(P_SND_EPROJ + "2", 40f);
		
		SOUND_PLAYER_HIT = loadSound(P_SND_PL + "hit", 50f);
		SOUND_SHIELD = loadSound(P_SND_PL + "shield", 50f);

		for(int i = 0; i < SOUND_ENEMY_HITS.length; i++){
			SOUND_ENEMY_HITS[i] = loadSound(P_SND_EHIT + (i+1), 25f);
		}


		for(int i = 0; i < 10; i++){
			TEX_NUMBERS_TEXT[i] = loadTexture(P_TXT + i);
		}

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


	/**Load a sound from the sound folder.
	 * @param name The name without the extension. Expects a .wav file.
	 * @param baseVolume
	 * @return
	 */
	public static Sound loadSound(String name, float baseVolume){
		SoundBuffer buf = new SoundBuffer();
		try {
			buf.loadFromFile(Paths.get("sound\\" + name + ".wav"));				

		} catch (IOException e) {					
			e.printStackTrace();
		}
		Sound s =  new Sound(buf);
		s.setVolume(baseVolume);
		return s;
	}

	/**JUST AN EXPERIMENT. NOT USED.
	 * @param name
	 * @param w
	 * @param num_between_frames
	 * @return
	 */
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
			for(int currframe = 0; currframe < numframes; currframe++){
				for(int x = 0; x < w; x++){
					for(int y = 0; y < origsize.y; y++){					

						Color pixcurr = original.getPixel(currframe*w + x, y);
						Color pixnext = original.getPixel(((currframe+1)*w + x)%origsize.x, y);

						int smoothbeginx = x+w*currframe*(num_between_frames+1);
						int smoothendx = x+w*(currframe+1)*(num_between_frames+1);
						if(currframe == 1) {
							System.out.println(smoothbeginx + " " + smoothendx);
						}
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
	 * @param name The name without the extension. Expects a .hbd file.
	 * @return An array containing points of a hitbox
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

	/**Play a sound. Slightly randomises pitch. Only plays if the sound is not playing and if the sound is enabled.
	 * @param s The sound to play.
	 */
	public static void playSound(Sound s){
		if(s.getStatus() != Status.PLAYING && Game.isSoundEnabled()){
			s.setPitch(Util.randInRange(.98f, 1.02f));
			s.play();
		}

	}

	/**Load music from the sound folder.
	 * @param name The name without the extension. Expects a .flac file.
	 * @return The music
	 */
	public static Music loadMusic(String name){
		Music m = new Music();
		try {
			m.openFromFile(Paths.get("sound//" + name + ".flac"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return m;

	}

}
