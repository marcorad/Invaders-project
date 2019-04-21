package engine.component;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;

import engine.entity.Entity;
import engine.graphics.GraphicsHandler;

//NOTE THAT a sprite component always has it's coordinates normalised to +-1
//which means a square of size 2 will always fit around it
//the setScale must be used to change the size

/**
 * Specifies the sprite to display on screen. The sprite will be have normalised texture coordinates, as per most graphic standards.
 * It operates on a texture that is a strip of frames showing the animation. Each frame is shown for a specified time period after
 * switching to the next frame. The width of a single frame must be know to make this happen.
 */
public class SpriteComponent extends ColorComponent implements UpdateableComponent {

	
	private Sprite sprite;
	private Texture tex;
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
	 * @param tex The texture containing the image
	 */
	public SpriteComponent(Entity entity, int w, float fps, Texture tex) {
		super(entity, Color.BLACK);
		this.tex = tex;	
		width = w;
		this.timebetweenframes = 1.f/fps;
		create();		
	}

	/**Constructs a sprite component with a single frame of animation
	 * @param entity the entity
	 * @param w width of a single frame
	 * @param h height of a single frame
	 * @param tex The texture containing the image
	 */
	public SpriteComponent(Entity entity, int w, int h, Texture tex){
		super(entity, Color.BLACK);
		tex = new Texture();
		width = w;
		height = h;
		frames = 1;
		timebetweenframes = Float.POSITIVE_INFINITY;
	}


	/**
	 * Helper method to create the sprite.
	 */
	private void create(){
		frames = tex.getSize().x/width;
		height = tex.getSize().y;
		sprite = new Sprite(tex,new IntRect(0,0,width,height));
		sprite.setOrigin(new Vector2f(width/2.f,height/2.f));
		normalisedscale = new Vector2f(2.f/width,-2.f/height);
		onScaleUpdate();
		onRotationUpdate();
		onPositionUpdate();
		colorUpdate();
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
	
	/**Check if a point is inside the sprite's global bounds.
	 * @param pt The point
	 * @return Whether it is inside
	 */
	public boolean pointInside(Vector2f pt){
		return sprite.getGlobalBounds().contains(pt);
	}

}
