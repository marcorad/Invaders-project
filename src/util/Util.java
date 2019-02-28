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

	/**Calculates an efficient approximation of cosine based on the Taylor series expansion of cosine.
	 * IT IS ONLY GOOD AT APPROXIMATION FROM -PI to PI
	 * @param t angle in radians
	 * @return an approximation of the cosine function
	 */
	public static float lazyCos(float t){
		return 1f - t*t/2f + t*t*t*t/24f - t*t*t*t*t*t / 720f + t*t*t*t*t*t*t*t/40320f - t*t*t*t*t*t*t*t*t*t/3628800f;
	}
	
	public static int sgn(float f){
		return f < 0f ?  -1 :  1;
	}

	public static float vectorAngle(Vector2f v){
		return toDegrees((float)Math.atan2(v.y, v.x));
	}
	
	/**Calculates an efficient approximation of sine based on the Taylor series expansion of sine.
	 * IT IS ONLY GOOD AT APPROXIMATION FROM -PI to PI
	 * @param t angle in radians
	 * @return an approximation of the sine function
	 */
	public static float lazySin(float t){
		return t - t*t*t/6f + t*t*t*t*t/120f - t*t*t*t*t*t*t/5040f + t*t*t*t*t*t*t*t*t / 362880f;
	}
	
	public static Color randomColor(){
		return new Color(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255),255);
	}

	/**
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

	public static Vector2f fromIntToFloatVector(Vector2i v){
		return new Vector2f((float)v.x, (float)v.y);
	}

	/**Creates a regular polygon bound to the unit circle
	 * @param n The number of points
	 * @return An array containing the points of the poly
	 */


	/**to use less calculation, the first 8 regular polygons are generated automatically for ease of use
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
			pts[i] = new Vector2f(lazyCos(angle), lazySin(angle));
			angle += 2*PI/n;
		}
		return pts;
	}


	/**
	 * @param theta rotation of the vector from the x-axis in degrees
	 * @return a unit vector with the specified rotation
	 */
	public static Vector2f unitVectorWithRotation(float theta){
		return new Vector2f((float)Math.cos(theta),(float)Math.sin(theta));
	}

	public static float randInRange(float low, float high){
		return (high - low)*rand.nextFloat() + low;
	}
	
	public static int randInRange(int low, int high){
		return low + rand.nextInt(high - low);
	}

	public static float abs(float a){
		return a < .0f ? -a : a;
	}	


	public static float toRad(float d){
		return d * (PI/180.f);
	}
	
	public static float toDegrees(float rad){
		return rad * (180f/PI);
	}

	public static float dot(Vector2f a, Vector2f b){
		return a.x * b.x + a.y * b.y; 
	}

	public static float mag(Vector2f v){
		return (float)Math.sqrt(v.x * v.x + v.y * v.y);
	}
	public static float magSquared(Vector2f v){
		return (v.x * v.x + v.y * v.y);
	}

	public static Vector2f getVectorFromPolar(float r, float theta){
		return Vector2f.mul(new Vector2f((float)Math.cos(toRad(theta)) , (float)Math.sin(toRad(theta))),r);
	}

	public static Vector2f normalise(Vector2f v){
		return Vector2f.div(v, mag(v));
	}

	/**
	 * @param pt1 first point
	 * @param pt2 second point
	 * @return the normal of the edge consisting of pt1 and pt2
	 */
	public static Vector2f getNormal(Vector2f pt1, Vector2f pt2){
		Vector2f v = new Vector2f((pt2.y - pt1.y), -(pt2.x - pt1.x));
		return normalise(v);
	}
	
	/**gets the normal of an edge without scaling it to a unit vector, to save some time on a costly sqrt function
	 * @param pt1 first point
	 * @param pt2 second point
	 * @return the normal of the edge consisting of pt1 and pt2, but not a unit vector
	 */
	public static Vector2f getLazyNormal(Vector2f pt1, Vector2f pt2){
		return new Vector2f((pt2.y - pt1.y), -(pt2.x - pt1.x));		
	}
	
	/**converts to a unit vector
	 * @param v the vector
	 * @return the unit vector
	 */
	public static Vector2f toUnitVector(Vector2f v){
		return Vector2f.div(v, mag(v));
	}

	public static Vector2f rotateVector(Vector2f target, Vector2f origin, float angle){
		Vector2f v = Vector2f.sub(target, origin);
		return new Vector2f(v.x *(float) Math.cos(toRad(angle)) - v.y* (float)Math.sin(toRad(angle)),
				v.y *(float) Math.cos(toRad(angle)) + v.x*(float) Math.sin(toRad(angle)) );
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


	/**
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


	//getGlobalBounds() does not work properly for some reason
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

	/**
	 * @param freqlow lowest freq
	 * @param freqhigh highest freq 
	 * @param minamp minimum allowable amplitude
	 * @param maxamp maximum allowable amplitude
	 * @param rangelow lowest allowable value
	 * @param rangehigh highest allowable value
	 * @param type type the type of oscillator
	 * @return  an oscillator with the specified properties. note that the max amplitude
	 * will be clamped between (rangehigh-rangelow)/2.0. avoid using negative amplitudes for amplitudes
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

	/**
	 * @param freqlow lowest freq
	 * @param freqhigh highest freq 
	 * @param minamp minimum allowable amplitude
	 * @param maxamp maximum allowable amplitude
	 * @param offset the offset
	 *  @param type type the type of oscillator
	 * @return an oscillator with the specified properties centered about offset
	 */
	public static Oscillator randomOscillator( float freqlow, float freqhigh, float minamp, float maxamp, float offset, Oscillator.OscType type){
		float amp = randInRange(minamp, maxamp);		
		float phase = randInRange((float)-Math.PI,(float) Math.PI);
		float freq = randInRange(freqlow, freqhigh);
		return new Oscillator(freq, amp, offset, phase, type);
	}



	public static MinMaxPair getMinMax(float[] a){
		float min = a[0], max = a[0];
		for(int i = 1; i < a.length; i ++){
			if(a[i] > max) max = a[i];
			if(a[i] < min) min = a[i];
		}
		return new MinMaxPair(min,max);
	}

	public static boolean overlaps(MinMaxPair p, MinMaxPair q){
		return (p.min < q.max && q.min < p.max);		
	}

	public static boolean intersects(FloatRect a, FloatRect b){
		if(a.left > b.left + b.width || a.left + a.width < b.left) return false;
		if(a.top < b.top - b.height || a.top - a.height > b.top) return false;
		return true;
	}

	public static  Vertex[] verticesFromPoints(Vector2f pts[]){
		Vertex[] v = new Vertex[pts.length];
		for(int i = 0; i < pts.length; i++)
			v[i] = new Vertex(pts[i], Color.RED);
		return v;
	}
	
	public static Vector2f approxParticleOffset(Vector2f unitdir, Vector2f scale){
		return Vector2f.mul(unitdir, dot(unitdir, scale));
	}

	public static Color colorWithVariation(Color main, int vary){
		return new Color(main.r + randInRange(-vary, vary), main.g + randInRange(-vary, vary), main.b + randInRange(-vary, vary), main.a + randInRange(-vary, vary));
	}
	
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
