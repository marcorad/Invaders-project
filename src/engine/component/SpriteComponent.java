package engine.component;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Image;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.graphics.TextureCreationException;
import org.jsfml.graphics.Transform;
import org.jsfml.system.Vector2f;

import engine.entity.Entity;
import engine.graphics.GraphicsHandler;
import engine.input.EventHandler;

//NOTE THAT a sprite component always has it's coordinates normalised to +-1
//which means a square of size 2 will always fit around it
//the setScale must be used to change the size

public class SpriteComponent extends ColorComponent implements UpdateableComponent {

	private Texture tex;
	private Image image;
	private Sprite sprite;
	private Path path;
	private int width, height;

	private Vector2f normalisedscale;
	private int frames;
	private float timebetweenframes;
	private float elapsedtime = 0.0f;
	private int currframe = 0;


	/** Constructs a sprite component with multiple frames of animation. Assumes a single strip
	 * @param entity the entity
	 * @param w width of a single frame
	 * @param fps the number of frames to be shown per second
	 * @param path path to the file
	 * @throws TextureCreationException Could not create the texture
	 * @throws IOException File issues
	 */
	public SpriteComponent(Entity entity, int w, float fps, String path) throws IOException, TextureCreationException {
		super(entity, new Color(255,255,255,255));
		tex = new Texture();
		this.path = Paths.get(path);			
		width = w;
		this.timebetweenframes = 1.f/fps;
		create();		
	}

	/**Constructs a sprite component with a single frame of animation
	 * @param entity the entity
	 * @param w width of a single frame
	 * @param h height of a single frame
	 * @param path path to the file
	 * @throws TextureCreationException  Could not create the texture
	 * @throws IOException File issues
	 */
	public SpriteComponent(Entity entity, int w, int h, String path) throws IOException, TextureCreationException {
		super(entity, new Color(255,255,255,255));
		tex = new Texture();
		this.path = Paths.get(path);
		width = w;
		height = h;		
		create();
		frames = 1;
		timebetweenframes = Float.POSITIVE_INFINITY;
		colorUpdate();
	}


	private void create() throws IOException, TextureCreationException{
		image = new Image();			
		image.loadFromFile(this.path);
		tex.loadFromImage(image);
		frames = tex.getSize().x/width;
		height = tex.getSize().y;
		sprite = new Sprite(tex,new IntRect(0,0,width,height));
		sprite.setOrigin(new Vector2f((float)width/2.f,(float)height/2.f));
		normalisedscale = new Vector2f(2.f/(float)width,-2.f/(float)height);
		onScaleUpdate();
		onRotationUpdate();
		onPositionUpdate();
	}

	@Override
	public void draw(GraphicsHandler graphics) {
		graphics.drawToRenderTexture(sprite);
	}	


	@Override
	public void onPositionUpdate() {
		sprite.setPosition(entity.getPosition());
	}

	@Override
	public void onRotationUpdate() {
		sprite.setRotation(entity.getTheta());
	}

	@Override
	public void onScaleUpdate() {
		sprite.setScale(Vector2f.componentwiseMul(normalisedscale, entity.getScale())); //must be scaled according to normalised coords
	}

	/**Converts a point from world space to sprite space
	 * @param v The point in world space
	 * @return The point in sprite space
	 */
	public Vector2f toNormalisedSpriteSpace(Vector2f v){
		return sprite.getTransform().transformPoint(v);
	}


	@Override
	public void update(float dt, float t) {
		elapsedtime += dt;
		if(elapsedtime >= timebetweenframes){
			elapsedtime -= timebetweenframes;
			currframe++;
			currframe %= frames;
			sprite.setTextureRect(new IntRect(currframe*width,0,width, height));
		}
	}

	@Override
	protected void colorUpdate() {
		this.sprite.setColor(color);
	}

}
