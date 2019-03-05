package util;

import java.util.Random;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.ConvexShape;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.Shape;
import org.jsfml.graphics.Transform;
import org.jsfml.graphics.Vertex;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import engine.entity.Entity;

public class Util {
	public final static Random rand = new Random();

	/**
	 * A float representation of PI
	 */
	public static final float PI = 3.14159265359f;

	/**Convenience method to save you from casting.
	 * @param t Angle in radians
	 * @return The cosine of t
	 */
	public static float cosf(float t){
		return (float) Math.cos(t);
	}

	/**Gets the sign of a number as either +1 or -1
	 * @param f The number
	 * @return The sign
	 */
	public static int sgn(float f){
		return f < 0f ?  -1 :  1;
	}

	/** The angle of the vector
	 * @param v The vector
	 * @return The angle in degrees
	 */
	public static float vectorAngle(Vector2f v){
		return toDegrees((float)Math.atan2(v.y, v.x));
	}
	
	/**Get the unit vector pointing in the direction the entity is facing
	 * @param e The entity
	 * @return The vector pointing in the facing direction
	 */
	public static Vector2f facing(Entity e){
		return unitVectorWithRotation(e.getTheta()+90f);
	}

	/**Convenience method to save you from casting.
	 * @param t Angle in radians
	 * @return The sine of t
	 */
	public static float sinf(float t){
		return (float) Math.sin(t);
	}

	/**Get a random colour
	 * @return The colour
	 */
	public static Color randomColor(){
		return new Color(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255),255);
	}

	/**Linearly interpolate between a starting and ending colour
	 * @param t The precentage of interpolation between [0,1]
	 * @param begin The colour at 0
	 * @param end The colour at 1
	 * @return The linearly interpolated colour
	 */
	public static Color lerpColor(float t, Color begin, Color end){
		float r = begin.r, g = begin.g, b = begin.b, a = begin.a;
		return new Color((int)(r + t*(end.r - r)), (int)(g + t*(end.g - g)), (int)(b + t*(end.b - b)), (int)(a + t*(end.a - a)));

	}
	
	/**Get a random unit vector between 2 angles.
	 * @param angleMin The minimum angle in radians
	 * @param angleMax The maximum angle in radians
	 * @return
	 */
	public static Vector2f randomUnitVector(float angleMin, float angleMax){
		float theta = randInRange(angleMin, angleMax);
		return new Vector2f(cosf(theta), sinf(theta));
	}
	
	/**Randomly vary a vector with a certain maximum angle
	 * @param v The vector
	 * @param varyRad The varyin angle in radians
	 * @return The varied vector
	 */
	public static Vector2f varyVector(Vector2f v, float varyRad){
		float angle = toRad(vectorAngle(v));
		return Vector2f.mul(randomUnitVector(angle - varyRad, angle + varyRad), mag(v));
	}

	/**Clamp a value to a certain range
	 * @param x value to be checked
	 * @param min minimum the value can be
	 * @param max maximum the value can be
	 * @return the clamped value
	 */
	public static float clamp(float x, float min, float max){
		if(x > max) return max;
		if(x < min) return min;
		return x;
	}

	/**Convert an integer vector to a float vector
	 * @param v The vector
	 * @return The converted vector
	 */
	public static Vector2f fromIntToFloatVector(Vector2i v){
		return new Vector2f((float)v.x, (float)v.y);
	}


	/**To use less calculation, the first 8 regular polygons are generated automatically for ease of use
	 * ranges from a equilateral triangle ([0]) to a regular decagon ([7])
	 */
	public static Vector2f[][] REGULAR_POLYGONS =  
		{
				getRegularPoly(3),
				getRegularPoly(4),
				getRegularPoly(5),
				getRegularPoly(6),
				getRegularPoly(7),
				getRegularPoly(8),
				getRegularPoly(9),
				getRegularPoly(10),
		};


	/** Generate a regular n-gon 
	 * @param n number of points
	 * @return the points of the n-gon
	 */
	public static Vector2f[] getRegularPoly(int n){
		Vector2f pts[] = new Vector2f[n];
		float angle = -PI; //start at negative pi to make the lazy cos and sin more accurate
		for(int i = 0; i < n; i++){
			pts[i] = new Vector2f(cosf(angle), sinf(angle));
			angle += 2*PI/n;
		}
		return pts;
	}


	/**Get a unit vector with a specified angle in degrees
	 * @param theta Rotation of the vector from the x-axis in degrees
	 * @return A unit vector with the specified rotation
	 */
	public static Vector2f unitVectorWithRotation(float theta){
		return new Vector2f(cosf(toRad(theta)),sinf(toRad(theta)));
	}

	/**Get a random float in the range [low, high)
	 * @param low The lowest number
	 * @param high the highest number
	 * @return The float in the range
	 */
	public static float randInRange(float low, float high){
		return (high - low)*rand.nextFloat() + low;
	}

	/**Get a random integer in the range [low, high)
	 * @param low The lowest number
	 * @param high The highest number
	 * @return The integer in the range
	 */
	public static int randInRange(int low, int high){
		return low + rand.nextInt(high - low);
	}

	/**The absolute value of a number
	 * @param a The number
	 * @return The absolute value
	 */
	public static float abs(float a){
		return a < .0f ? -a : a;
	}	


	/**Convert from degrees to radians
	 * @param d Angle in degrees
	 * @return Andle in radians
	 */
	public static float toRad(float d){
		return d * (PI/180.f);
	}

	/**Convert from radians to degrees
	 * @param d Angle in radians
	 * @return Andle in degrees
	 */
	public static float toDegrees(float rad){
		return rad * (180f/PI);
	}

	/**Compute the dot product of two vectors
	 * @param a The first vector
	 * @param b The second vector
	 * @return The dot product
	 */
	public static float dot(Vector2f a, Vector2f b){
		return a.x * b.x + a.y * b.y; 
	}

	/**Get the magnitude of a vector
	 * @param v The vector
	 * @return The magnitude
	 */
	public static float mag(Vector2f v){
		return (float)Math.sqrt(v.x * v.x + v.y * v.y);
	}
	/**Get the magnitude squared of ta vector
	 * @param v The vector
	 * @return The magnitude squared
	 */
	public static float magSquared(Vector2f v){
		return (v.x * v.x + v.y * v.y);
	}

	/**Convert from polar coordinates to Cartesian coordinates
	 * @param r The magnitude of the vector
	 * @param theta The angle in degrees
	 * @return The vector in Cartesian coordinates
	 */
	public static Vector2f getVectorFromPolar(float r, float theta){
		return Vector2f.mul(unitVectorWithRotation(theta),r);
	}

	/**Convert a vector to a unit vector
	 * @param v The vector
	 * @return The unit vector
	 */
	public static Vector2f normalise(Vector2f v){
		return Vector2f.div(v, mag(v));
	}

	/**Get a vector perpendicular to 2 points
	 * @param pt1 The first point
	 * @param pt2 The second point
	 * @return The normal of the edge consisting of pt1 and pt2
	 */
	public static Vector2f getNormal(Vector2f pt1, Vector2f pt2){
		Vector2f v = new Vector2f((pt2.y - pt1.y), -(pt2.x - pt1.x));
		return normalise(v);
	}

	/**Gets the normal of an edge without scaling it to a unit vector, to save some time on a costly sqrt function
	 * @param pt1 The first point
	 * @param pt2 The second point
	 * @return The normal of the edge consisting of pt1 and pt2, but not a unit vector
	 */
	public static Vector2f getLazyNormal(Vector2f pt1, Vector2f pt2){
		return new Vector2f((pt2.y - pt1.y), -(pt2.x - pt1.x));		
	}


	/**Rotate a vector about a certain point
	 * @param target The vector to rotate
	 * @param origin The origin of rotation
	 * @param degrees The angle in degrees
	 * @return The rotated vector
	 */
	public static Vector2f rotateVector(Vector2f target, Vector2f origin, float degrees){
		Transform t = Transform.IDENTITY;
		t = Transform.rotate(t, degrees, origin);
		return t.transformPoint(target);
	}
	
	
	/**Rotate a vector about the origin
	 * @param target The vector to rotate
	 * @param degrees The angle in degrees
	 * @return The rotated vector
	 */
	public static Vector2f rotateVector(Vector2f target, float degrees){
		Transform t = Transform.IDENTITY;
		t = Transform.rotate(t, degrees);
		return t.transformPoint(target);
	}

	/**Scales a vector about a point.
	 * @param target Vector to scale.
	 * @param origin Point about which to scale.
	 * @param sx X scale
	 * @param sy Y scale
	 * @return The scaled vector.
	 */
	public static Vector2f scaleVector(Vector2f target, Vector2f origin, float sx, float sy){
		Vector2f scale = new Vector2f(sx, sy);
		Vector2f v = Vector2f.sub(target, origin);
		return Vector2f.componentwiseMul(v, scale);
	}


	/** Get the vertices of a shape in world space
	 * @param s The shape to retrieve transformed vertices from.
	 * @return An array of vertices that was transformed according to the data stored in the shape.
	 */
	public static Vertex[] getTransformedVertices(Shape s){
		Vertex pts[] = new Vertex[s.getPointCount()];
		Transform t = s.getTransform();
		for(int i = 0; i < pts.length; i++){
			pts[i] = new Vertex(t.transformPoint(s.getPoint(i)), s.getFillColor());
		}
		return pts;
	}
	
	
	public static Vector2f[] getTransformedPoints(Shape s){
		Vector2f pts[] = new Vector2f[s.getPointCount()];
		Transform t = s.getTransform();
		for(int i = 0; i < pts.length; i++){
			pts[i] = (t.transformPoint(s.getPoint(i)));
		}
		return pts;
	}


	
	/**Get the bounding rectangle of a shape, since getGlobalBounds() does not seem to work properly
	 * @param s The shape
	 * @return The bounding rectangle
	 */
	public static FloatRect getBoundingRect(Shape s){
		Vector2f[] pts = getTransformedPoints(s);
		float x[] = new float[pts.length], y[] = new float[pts.length];
		for(int i = 0; i < pts.length; i++){
			x[i] = pts[i].x;
			y[i] = pts[i].y;
		}
		MinMaxPair xs = getMinMax(x);
		MinMaxPair ys = getMinMax(y);

		return new FloatRect(xs.min, ys.max, xs.max - xs.min, ys.max - ys.min);
	}

	/**Get a random oscillator
	 * @param freqlow The lowest freq
	 * @param freqhigh The highest freq 
	 * @param minamp The minimum allowable amplitude
	 * @param maxamp The maximum allowable amplitude
	 * @param rangelow The lowest allowable value
	 * @param rangehigh The highest allowable value
	 * @param type The type the type of oscillator
	 * @return An oscillator with the specified properties. Note that the max amplitude
	 * will be clamped between (rangehigh-rangelow)/2.0. Avoid using negative amplitudes, and rather use phase shifts.
	 */
	public static Oscillator randomOscillator( float freqlow, float freqhigh, float minamp, float maxamp, float rangelow, float rangehigh, Oscillator.OscType type){
		maxamp = clamp(maxamp,.0f, (rangehigh-rangelow)/2.f);
		float amp = randInRange(minamp, maxamp);	
		float offsetmin = rangelow + amp;
		float offsetmax = rangehigh - amp;
		float offset = randInRange(offsetmin , offsetmax);

		float phase = randInRange((float)-Math.PI,(float) Math.PI);
		float freq = randInRange(freqlow, freqhigh);
		return new Oscillator(freq, amp, offset, phase, type);
	}

	/**Get a random oscillator
	 * @param freqlow The lowest freq
	 * @param freqhigh The highest freq 
	 * @param minamp The minimum allowable amplitude
	 * @param maxamp The maximum allowable amplitude
	 * @param offset The offset
	 *  @param type The type the type of oscillator
	 * @return An oscillator with the specified properties centered about offset
	 */
	public static Oscillator randomOscillator( float freqlow, float freqhigh, float minamp, float maxamp, float offset, Oscillator.OscType type){
		float amp = randInRange(minamp, maxamp);		
		float phase = randInRange((float)-Math.PI,(float) Math.PI);
		float freq = randInRange(freqlow, freqhigh);
		return new Oscillator(freq, amp, offset, phase, type);
	}



	/**Get the minimum and maximum values of an array
	 * @param a The values to check
	 * @return The minimum and maximum values
	 */
	public static MinMaxPair getMinMax(float[] a){
		float min = a[0], max = a[0];
		for(int i = 1; i < a.length; i ++){
			if(a[i] > max) max = a[i];
			if(a[i] < min) min = a[i];
		}
		return new MinMaxPair(min,max);
	}

	/**Check if a range of numbers overlap
	 * @param p The first range
	 * @param q The second range
	 * @return Whether the range overlaps
	 */
	public static boolean overlaps(MinMaxPair p, MinMaxPair q){
		return (p.min < q.max && q.min < p.max);		
	}

	/**Check if two rectangles overlap
	 * @param a The first rectangle
	 * @param b The second rectangle
	 * @return Whether they overlap
	 */
	public static boolean intersects(FloatRect a, FloatRect b){
		if(a.left > b.left + b.width || a.left + a.width < b.left) return false;
		if(a.top < b.top - b.height || a.top - a.height > b.top) return false;
		return true;
	}

	/**Get vertex objects from a set of points, with a red colour.
	 * @param pts The points
	 * @return The vertices
	 */
	public static  Vertex[] verticesFromPoints(Vector2f pts[]){
		Vertex[] v = new Vertex[pts.length];
		for(int i = 0; i < pts.length; i++)
			v[i] = new Vertex(pts[i], Color.RED);
		return v;
	}

	/**Approximate where a particle could be such that the particle is outside the bounding box of an entity, taking x and y scales into account
	 * @param unitdir The direction in which the particle is offset
	 * @param e The entity it is offset from
	 * @return The approximated offset
	 */
	public static Vector2f approxParticleOffset(Vector2f unitdir, Entity e){
		Vector2f scale = e.getScale();
		scale = new Vector2f(Util.sgn(unitdir.x)*scale.x, Util.sgn(unitdir.y)*scale.y);
		return Vector2f.mul(unitdir, dot(unitdir, scale));
	}
	

	/**Approximate where a particle could be such that the particle is outside the bounding box of an entity, taking x and y scales into account, with an addition compensational offset in the direction of the offset
	 * @param unitdir The direction in which the particle is offset
	 * @param e The entity it is offset from, positive for positive offset and negative for negative offset
	 * @param compensate The magnitude of the compensating offset
	 * @return The approximated offset
	 */
	public static Vector2f approxParticleOffset(Vector2f unitdir, Entity e, float compensate){
		Vector2f off = approxParticleOffset(unitdir,e);
		Vector2f comp = Vector2f.mul(unitdir, compensate);
		return Vector2f.add(off, comp);
	}

	/**Get a colour with a certain variation in the components
	 * @param main The main colour
	 * @param vary The number with which to vary the RGBA components
	 * @return The varied colour
	 */
	public static Color colorWithVariation(Color main, int vary){
		return new Color(main.r + randInRange(-vary, vary), main.g + randInRange(-vary, vary), main.b + randInRange(-vary, vary), main.a + randInRange(-vary, vary));
	}

	/**Point an entity in a certain direction based off a vector
	 * @param e The entity
	 * @param dir The direction vector, which does not necessarily have to be a unit vector
	 */
	public static void pointEntityInDirection(Entity e, Vector2f dir){
		e.setTheta(-90f +vectorAngle(dir));
	}

	/**
	 * Tests for collision between two convex polygons according to the separating axis theorem (SAT)
	 * @param a First convex polygon
	 * @param b Second convex polygon
	 * @return whether they are colliding
	 */

	public static boolean convexPolyCollision(ConvexShape a, ConvexShape b ){		

		//preliminary bounding rectangle check to improve efficiency
		if(!intersects(getBoundingRect(a),getBoundingRect(b))){
			return false;
		}



		//System.out.println("Rect intersects");

		/*
		 * ALGORITHM
		 * For each edge of a and b, which has a set of points p and q respectively
		 * Get the edge normal
		 * Project the vertices of a and b onto the normal (dot product)
		 * Find the max and min projection values for each shape
		 * If the projections do not overlap, then return false
		 * If algorithm completes, return true
		 * 
		 * This algorithm was discovered on the page with URL:
		 * http://back2basic.phatcode.net/?Issue-%231/2D-Convex-Polygon-Collision-using-SAT
		 * 		 
		 * Note that this Java implementation is original and specific for the JSFML library.
		 */

		Vector2f[] p = getTransformedPoints(a);
		Vector2f[] q = getTransformedPoints(b);
		float[] p_proj = new float[p.length];
		float[] q_proj = new float[q.length];

		//check for a
		for(int edge_a = 0; edge_a < p.length; edge_a++){

			Vector2f normal= getLazyNormal(p[edge_a], p[(edge_a+1)%p.length]); //get the normal of two vertices

			//project
			for(int pi = 0; pi < p.length; pi++){
				p_proj[pi] = dot(p[pi], normal);
			}
			for(int qi = 0; qi < q.length; qi++){
				q_proj[qi] = dot(q[qi], normal);
			}

			//check if projections intersect
			if(!overlaps(getMinMax(p_proj), getMinMax(q_proj))){
				return false;
			}
		}

		//repeat for b
		for(int edge_b = 0; edge_b < q.length; edge_b++){

			Vector2f normal= getLazyNormal(q[edge_b], q[(edge_b+1)%q.length]);


			for(int pi = 0; pi < p.length; pi++){
				p_proj[pi] = dot(normal, p[pi]);
			}
			for(int qi = 0; qi < q.length; qi++){
				q_proj[qi] = dot(normal, q[qi]);
			}

			if(!overlaps(getMinMax(p_proj), getMinMax(q_proj))){
				return false;
			}
		}
		//System.out.println("YAY");

		return true;
	}

}
