package engine.graphics;

import java.util.Vector;


import org.jsfml.graphics.BlendMode;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.PrimitiveType;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTexture;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.TextureCreationException;
import org.jsfml.graphics.Transform;
import org.jsfml.graphics.Vertex;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import util.Util;

public class GraphicsHandler {
	
	private RenderWindow window;
	private RenderStates texturerenderstate, windowrenderstate;
	private RenderTexture rt;
	private Sprite screenspr; //adding a sprite to draw to before the screen allows for the use of shaders on the sprite
	public Transform camera, normalisedspace;
	private Vector<Drawable> windowdraws; //stored objects that need to be drawn on the window which is the topmost layer without a camera transform
	
	public GraphicsHandler(RenderWindow window){
		this.window = window;
		
		
		windowdraws = new Vector<>();
		
		
		rt = new RenderTexture();
		Vector2i wsize = window.getSize();
		try {
			rt.create(wsize.x, wsize.y);
		} catch (TextureCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
		}
		screenspr = new Sprite(rt.getTexture());
		
		//create the scaling 
		Vector2f scale = new Vector2f(.5f*(float)wsize.x,-.5f*(float)wsize.y); //invert the y coord so that y increases going up the screen
		
		normalisedspace = Transform.scale(Transform.translate(Transform.IDENTITY, wsize.x/2.0f, wsize.y/2.0f), scale);
		
		camera = new Transform(normalisedspace);
		
		//create a render state that draws a normalised space on the screen
		//i.e the origin is at the centre of the screen, with +-1 at the edges
		texturerenderstate = new RenderStates(BlendMode.ALPHA, camera, null, null);	
		windowrenderstate = new RenderStates(BlendMode.ALPHA,normalisedspace, null, null);	
		
	}
	
	/**draws to the render texture which is drawn to the window
	 * this uses the camera transformation when drawing
	 * space is normalised: 0,0 in the centre of the screen and +- 1 at edges
	 * the positive y axis upwards and positive x is right
	 * @param d object to draw
	 */
	public void drawToRenderTexture(Drawable d){	
		rt.draw(d, texturerenderstate);
	}
	
	public void drawToRenderTexture(Vertex[] vertices, PrimitiveType type){	
		rt.draw(vertices, type, texturerenderstate);
	}
	
	
	/**draws to the window which is drawn on top of the render texture
	 * this does NOT use the camera transformation when drawing
	 * space is normalised: 0,0 in the centre of the screen and +- 1 at edges
	 * the positive y axis upwards and positive x is right
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
	
	
	/**
	 * clears the screen
	 */
	public void clear(){
		//not necessary to clear the window since a whole sprite is drawn over it first
		//clear rt		
		//rt.clear() does not seem to work, so manually draw a rect
		Vector2f s =new Vector2f(2.0f, 2.0f);
		RectangleShape  rect = new RectangleShape(s);
		rect.setOrigin(Vector2f.mul(s, 0.5f));
		rect.setFillColor(Color.WHITE);
		drawToRenderTexture(rect);
	}
	
	
	/**
	 * displays the screen contents
	 */
	public void display(){
		rt.display();
		window.draw(screenspr);		
		//draw all objects that called drawToWindow
		for(Drawable d : windowdraws){
			window.draw(d, windowrenderstate);
		}
		windowdraws.clear();
		window.display();
	}
	
	

}
