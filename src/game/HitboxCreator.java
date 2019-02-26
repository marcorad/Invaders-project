package game;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.TextureCreationException;
import org.jsfml.system.Clock;
import org.jsfml.system.Vector2f;
import org.jsfml.window.ContextActivationException;
import org.jsfml.window.Keyboard;
import org.jsfml.window.VideoMode;
import org.jsfml.window.Window;
import org.jsfml.window.event.KeyEvent;
import org.jsfml.window.event.MouseButtonEvent;
import org.jsfml.window.event.MouseEvent;
import org.jsfml.window.event.MouseWheelEvent;

import engine.component.ConvexPolygonComponent;
import engine.component.DisplayType;
import engine.component.SpriteComponent;
import engine.component.TextComponent;
import engine.entity.Entity;
import engine.entity.EntityManager;
import engine.graphics.GraphicsHandler;
import engine.input.EventHandler;
import engine.input.KeyListener;
import engine.input.MouseListener;
import util.Util;

public class HitboxCreator  implements MouseListener, KeyListener{


	public final static int WIDTH = 800, HEIGHT = 800;
	public static final int FRAMERATE = 1000;
	public static GraphicsHandler graphics;
	public static EntityManager entitymanager;
	private static EventHandler eventhandler;
	private static RenderWindow window;

	static{
		window = new RenderWindow();

		window.create(new VideoMode( WIDTH , HEIGHT), "", Window.TITLEBAR | Window.CLOSE);
		try {
			window.setActive();
		} catch (ContextActivationException e) {
			e.printStackTrace();
		}
		window.setKeyRepeatEnabled(false);
		window.setVerticalSyncEnabled(false); //VSync can can impact performance
		//Limit the framerate
		window.setFramerateLimit(FRAMERATE);
		
		graphics = new GraphicsHandler(window);
		eventhandler = new EventHandler(window);
		entitymanager = new EntityManager();
		
		Entity.setEnvironment(entitymanager, graphics);
	}
	
	public Entity sprite;
	public Entity text;
	public SpriteComponent spritecomp;
	public ConvexPolygonComponent hitbox;
	public ArrayList<Vector2f> pts;
	public String name;
	
	public HitboxCreator(){

		Clock elapsed_time = new Clock();	
		Clock loop_time = new Clock();	
		float t = 0.0f, dt; //total time using double for extra precision

		eventhandler.attachKeyListener(this);
		eventhandler.attachMouseListener(this);
		pts = new ArrayList<>();

		sprite = new Entity(Vector2f.ZERO);
		text = new Entity( new Vector2f(-.53f, .9f));
		Font font = new Font();
		try {
			font.loadFromFile(Paths.get("font\\Calibri.ttf"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		TextComponent tc = new TextComponent(text, Color.BLACK, font);		
		text.addComponent(tc);
		text.setScale(new Vector2f(.05f,.05f));
		sprite.setScale(new Vector2f(.87f,.87f));

		tc.setText("Click to add a new point\nPress backspace to remove the previous point\nPress 's' to save the hitbox data\nPress 'n' to load a new sprite");		


		name = "test";
		loadSprite();


		//Main loop
		while(window.isOpen()) {
			loop_time.restart();
			dt = elapsed_time.restart().asSeconds(); //change in time
			t += dt; //keep track of total time

			float frate = (1.0f / dt);

			window.setTitle("Hitbox creator" + " - " + String.valueOf(frate));	

			graphics.clear();

			entitymanager.drawEntities();
			entitymanager.update(dt, t);

			graphics.display();			
			eventhandler.handleEvents();
		}


	}

	/** loads the sprite
	 * @param w width of a single frame
	 * @param h height of a single frame
	 */
	public void loadSprite(){	
		//lazy solution to stop previous hitbox from being drawn
		if (hitbox != null)hitbox.toggleEnable();
		hitbox = new ConvexPolygonComponent(sprite, null , new Color(255,0,0,100), DisplayType.FILL);		
		boolean error;
		int w = 0;
		do{
			try {
				name = JOptionPane.showInputDialog("Please enter the name of the sprite");
				w = Integer.parseInt(JOptionPane.showInputDialog("Please enter the width of a frame"));

				//stop the previous sprite from being drawn, a lazy solution since entities are not meant to have components removed
				if (spritecomp != null)spritecomp.toggleEnable();
				
				
				spritecomp = new SpriteComponent(sprite, w, 15f, "sprites\\" + name + ".png");
				error = false;
			} catch (IOException | TextureCreationException e) {
				error = true;
				System.out.println("This file does not exist");
			}
		} while (error);

		sprite.addComponent(spritecomp);
		sprite.addComponent(hitbox);
	}

	public static void main(String[] args) {
		new HitboxCreator();

	}

	@Override
	public void onKeyPress(KeyEvent ke) {
		switch (ke.key){
		case N:
			pts.clear();
			loadSprite();			
			break;
		case S: saveHitboxData();
			break;
		case BACKSPACE:  
			if(pts.size() != 0) { 
				pts.remove(pts.size()-1);
				updatePoints();
			}
			break;
		}

	}
	
	public void saveHitboxData(){
		try {
			DataOutputStream dos = new DataOutputStream( new FileOutputStream("hbdata\\" + name + ".hbd",false));
			for(Vector2f v : pts){
				dos.writeFloat(v.x);
				dos.writeFloat(v.y);
			}
			dos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void onKeyRelease(KeyEvent ke) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isUseless() {
		// TODO Auto-generated method stub
		return false;
	}

	public void updatePoints(){
		Vector2f[] temp = new Vector2f[1];
		temp = pts.toArray(temp);		
		hitbox.setPoints(temp);
	}

	@Override
	public void onMousePress(MouseButtonEvent mbe) {
		Vector2f v = Util.fromIntToFloatVector(mbe.position);
		Vector2f ws = graphics.toWorldSpace(v);
		Vector2f es = sprite.toEntitySpace(ws);
		//		System.out.println("Screen space: " + v);		
		//		System.out.println("World space: " + ws);	
		//		System.out.println("Sprite space: " + es);	

		pts.add(es);
		updatePoints();
	}

	@Override
	public void onMouseRelease(MouseButtonEvent mbe) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMouseWheelMoved(MouseWheelEvent mwe) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMouseMoved(MouseEvent me) {
		// TODO Auto-generated method stub

	}

}
