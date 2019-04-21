package engine.graphics;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import org.jsfml.graphics.BlendMode;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.PrimitiveType;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTexture;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Shader;
import org.jsfml.graphics.ShaderSourceException;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.graphics.TextureCreationException;
import org.jsfml.graphics.Transform;
import org.jsfml.graphics.Vertex;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import engine.entity.PoisonCloud;
import game.Game;
import util.Util;

/**
 * Handles all the graphics. This includes render textures and shaders. There are 3 layers of drawing:</br>
 * A main texture, on which the background is drawn to using a GLSL shader</br>
 * An upper texture, which may contain effects such as the poison blobs</br>
 * Rendering directly to the window, which is the topmost layer. All objects on the layer are drawn over the previous layers.
 * Some GUI elements are on this layer.
 */
public class GraphicsHandler {

	private RenderWindow window;
	private RenderStates texturerenderstate, windowrenderstate, bgrenderstate, effectrenderstate;
	private Shader bgshader, effectshader;
	private RenderTexture mainRT, effectRT;
	private Sprite mainSprite, poisonSprite; //adding a sprite to draw to before the screen allows for the use of shaders on the sprite
	public Transform camera, normalisedspace, blobspace;
	private Vector<Drawable> windowdraws; //stored objects that need to be drawn on the window which is the topmost layer without a camera transform
	private Vector2f worldscale;
	private ArrayList<PoisonCloud> clouds = new ArrayList<>();
	private float timeSinceDamage = 0f;
	private boolean damageTaken = false;

	/**Update the background and effects
	 * @param dt Change in time
	 * @param t Total time passed
	 */
	public void update(float dt, float t){		
		effectshader.setParameter("t", t);

		Iterator<PoisonCloud> it = clouds.iterator();
		while(it.hasNext()){
			if(it.next().shouldBeRemoved()){
				it.remove();
			}
		}		
		timeSinceDamage += dt;	
	}

	/**Add a poison cloud to the upper layer. Note the effect supports a maximum of 64 clouds.
	 * @param pc The cloud
	 */
	public void addPoisonCloud(PoisonCloud pc){
		clouds.add(pc);
	}

	/**
	 * Start the damage taken effect (red screen flash)
	 */
	public void damageTakenEffect(){
		damageTaken = true;
		timeSinceDamage = 0f;
	}


	/**
	 * Draw the upper effects layer
	 */
	private void drawEffectsLayer(){		

		Vertex v[] = new Vertex[]{  
				new Vertex(new Vector2f(-1,1)),
				new Vertex(new Vector2f(1,1)),
				new Vertex(new Vector2f(1,-1)),
				new Vertex(new Vector2f(-1,-1))
		};
		//upperbgshader.setParameter("poj_mat", normalisedspace);

		for(int i = 0; i < 32; i++){			
			float alpha = 0f;
			if(i < clouds.size()){
				float t = clouds.get(i).getElapsedTime();
				alpha = t > 1 ? 2-t : t; // quadratic with max at 2		
				alpha *= 0.6f;				
				effectshader.setParameter("blob_pos[" + i + "]", blobspace.transformPoint(clouds.get(i).getPosition()));
			} 
			effectshader.setParameter("alpha_mult[" + i + "]", alpha );
		}
		effectRT.draw(v, PrimitiveType.QUADS, effectrenderstate);	

	}

	/**
	 * @return The world scale
	 */
	public Vector2f getWorldScale(){
		return worldscale;
	}

	/**
	 * @param window The window the graphics operates on
	 */
	public GraphicsHandler(RenderWindow window){
		this.window = window;		
		windowdraws = new Vector<>();		
		mainRT = new RenderTexture();
		Vector2i wsize = window.getSize();
		effectRT = new RenderTexture();
		try {
			mainRT.create(wsize.x, wsize.y);
			effectRT.create(wsize.x, wsize.y);
		} catch (TextureCreationException e) {
			e.printStackTrace();			
		}
		mainSprite = new Sprite(mainRT.getTexture());
		poisonSprite = new Sprite(effectRT.getTexture());

		//create the scaling 
		worldscale = new Vector2f(.5f*wsize.x,-.5f*wsize.y); //invert the y coord so that y increases going up the screen


		normalisedspace = Transform.scale(Transform.translate(Transform.IDENTITY, wsize.x/2.0f, wsize.y/2.0f), worldscale);

		blobspace = Transform.scale(normalisedspace, 1, -1);

		camera = new Transform(normalisedspace);

		bgshader = new Shader();
		effectshader = new Shader();
		try {
			bgshader.loadFromFile(Paths.get("shader//background_shader.glsl"), Shader.Type.FRAGMENT);
			//upperbgshader.loadFromStream(new FileInputStream( new File("shader//upper_bg_vert.glsl")), new FileInputStream( new File("shader//upper_bg_frag.glsl")));
			effectshader.loadFromFile(Paths.get("shader//upper_bg_frag.glsl"), Shader.Type.FRAGMENT);
		} catch (IOException | ShaderSourceException e) {
			e.printStackTrace();

		}

		//create a render state that draws a normalised space on the screen
		//i.e the origin is at the centre of the screen, with +-1 at the edges
		texturerenderstate = new RenderStates(BlendMode.ALPHA, camera, null, null);	
		windowrenderstate = new RenderStates(BlendMode.ALPHA,normalisedspace, null, null);	
		bgrenderstate = new RenderStates(BlendMode.NONE,Transform.IDENTITY, null, bgshader);
		effectrenderstate = new RenderStates(BlendMode.NONE,normalisedspace, null, effectshader);
	}

	/**draws to the render texture which is drawn to the window
	 * this uses the camera transformation when drawing
	 * space is normalised: 0,0 in the centre of the screen and +- 1 at edges
	 * the positive y axis upwards and positive x is right
	 * @param d object to draw
	 */
	public void drawToRenderTexture(Drawable d){	
		mainRT.draw(d, texturerenderstate);
	}

	/**NOT USED OUTSIDE DEBUGGING
	 * @param vertices
	 * @param type
	 */
	public void drawToRenderTexture(Vertex[] vertices, PrimitiveType type){	
		mainRT.draw(vertices, type, texturerenderstate);
	}


	/**Draws to the window which is drawn on top of the render textures.
	 * This does NOT use the camera transformation when drawing.
	 * Space is normalised: (0,0) in the centre of the screen and +- 1 at edges
	 * The positive y axis upwards and positive x is right.
	 * @param d object to draw
	 */

	public void drawToWindow(Drawable d){
		windowdraws.addElement(d);
	}


	/**Converts a point from screen space to world space
	 * @param v The point in screen space
	 * @return The point in world space
	 */
	public Vector2f toWorldSpace(Vector2f v){
		return camera.getInverse().transformPoint(v);
	}

	/**Converts a point from screen space to world space
	 * @param v The point in screen space
	 * @return The point in world space
	 */
	public Vector2f toWorldSpace(Vector2i v){
		return camera.getInverse().transformPoint(Util.fromIntToFloatVector(v));
	}

	private Sprite BG;
	public void setBackground(Texture t){
		BG = new Sprite(t);
		Vector2f size = Util.fromIntToFloatVector(t.getSize());
		BG.setOrigin(Vector2f.mul(size, 0.5f));
		BG.setScale(new Vector2f(2.f/size.x,-2.f/size.y));
	}


	/**
	 * Clears the screen by drawing the background
	 */
	public void clear(){
		drawBackground();
	}

	/**
	 * Draw the BG on main layer
	 */
	private void drawBackground(){
		Vertex v[] = new Vertex[]{  
				new Vertex(new Vector2f(0f,0f)),
				new Vertex(new Vector2f(Game.WIDTH,0f)),
				new Vertex(new Vector2f(Game.WIDTH,Game.HEIGHT)),
				new Vertex(new Vector2f(0f,Game.HEIGHT))
		};	

		bgshader.setParameter("t", Game.getTotalElapsedTime());

		if(damageTaken){			
			if(timeSinceDamage < 0.1f){
				bgshader.setParameter("damage_taken", 1f);
			} else {
				damageTaken = false;
			}
		} else {
			bgshader.setParameter("damage_taken", 0f);
		}

		mainRT.draw(v, PrimitiveType.QUADS, bgrenderstate);
	}


	/**
	 * Displays the screen contents by drawing render textures in the correct order onto the window.
	 */
	public void display(){
		mainRT.display();
		drawEffectsLayer();
		effectRT.display();
		window.draw(mainSprite);	
		window.draw(poisonSprite);
		//draw all objects that called drawToWindow
		for(Drawable d : windowdraws){
			window.draw(d, windowrenderstate);
		}
		windowdraws.clear();
		window.display();
	}



}
