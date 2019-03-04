package engine.entity;

import java.util.Vector;

import org.jsfml.graphics.ConvexShape;
import org.jsfml.graphics.Transform;
import org.jsfml.system.Vector2f;

import engine.component.*;
import engine.graphics.GraphicsHandler;
import engine.input.EventHandler;
import game.Game;
import util.Util;

/**A class that specifies all the requirements of an entity. 
 * @author Marco
 *
 */
public class Entity{

	public static EntityManager entitymanager;
	public static GraphicsHandler graphics;
	public static EventHandler eventhandler;
	protected float maxHealth = .0001f;
	protected float health = .0001f; //health of entity, entity gets removed once health reaches 0
	protected float damage = 0f; //the damage value that this entity can deal, only really used for projectiles
	private boolean collidedThisFrame = false;


	/**Sets the manager and graphics that the entity has access to. This must be done before the game is started.
	 * @param em The entity manager
	 * @param gh The garphics
	 */
	public static void setEnvironment(EntityManager em,GraphicsHandler gh, EventHandler eh){
		entitymanager = em;
		graphics = gh;
		eventhandler = eh;
	}	

	/**
	 * @return The damage that is associated with the entity, which can be used to deal specific damages
	 */
	public float getDamage() {
		return damage;
	}

	/**Set the damage associated with an entity
	 * @param damage The damage
	 */
	public void setDamage(float damage) {
		this.damage = damage;
	}

	protected Vector2f position;
	protected Vector2f previous_pos;

	//lists of component methods 
	protected Vector<UpdateableComponent> updatecomps= new Vector<>();
	protected Vector<DrawableComponent> drawcomps= new Vector<>();;
	protected Vector<MovementNotifier> movenotifiercomps= new Vector<>();
	protected Vector<CollisionComponent> collisioncomps= new Vector<>();
	protected Vector<NotifierComponent> notifiercomps = new Vector<>();

	protected float previous_dt = 1.f;

	protected Vector<Entity> collidingentities = new Vector<>();

	private Vector2f minpositionclamp = new Vector2f(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY); //the position will be clamped to these minimum coords
	private Vector2f maxpositionclamp = new Vector2f(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY); // the posistion will be clamped to these maximum coords

	private float theta;
	private Vector2f scale = new Vector2f(1.f,1.f);
	private boolean remove = false;//whether the object should be removed from the entity list

	/***Constructs and entity with a scale of 1 and a rotation of 0
	 * @param position The position in the world
	 */
	public Entity(Vector2f position) {
		setTheta(.0f);
		previous_pos = position;
		setPosition(position);
		entitymanager.addEntity(this);
	}


	/**Constructs and entity with a scale of 1
	 * @param position The position in the world
	 * @param rotation The rotation measured from positive x-axis in degrees
	 */
	public Entity(Vector2f position, float rotation) {
		previous_pos = position;
		setPosition(position);
		setTheta(.0f);
		entitymanager.addEntity(this);
	}

	/**Set the maximum position in world space the this entity will clamped to
	 * @param max The max position, i.e top right of the clamped region
	 */
	public void setMaxPosition(Vector2f max){
		maxpositionclamp = max;
	}
	/**Set the minimum position in world space the this entity will clamped to
	 * @param min The min position, i.e bottom left of the clamped region
	 */
	public void setMinPosition(Vector2f min){
		minpositionclamp = min;
	}

	/**Damage this entity
	 * @param damage The damage
	 */
	public void doDamage(float damage){
		health -= damage;
		System.out.println(health);
	}

	/**
	 * Set the entity's health to zero, causing it to be removed
	 */
	public void kill(){
		health = 0f;
		remove = true;
	}

	public void healFully(){
		health = maxHealth;
	}

	/**
	 * @return Whether this entity should be removed from the list
	 */
	public boolean shouldBeRemoved(){
		return remove;
	}	


	/**Test for a collision between another entity and add the entities to each others' list of colliding entities
	 * @param e The other entity
	 */
	public void collide(Entity e){
		Vector<CollisionComponent> other = e.getCollisionComponents();
		for(CollisionComponent thiscc : collisioncomps){
			for(CollisionComponent othercc : other){
				boolean thiscollides = false, othercollides = false;

				//check if the other component can collide with this component
				for(CollisionID otherid : othercc.getCollidingIDs()){
					if(thiscc.getID() == otherid) othercollides = true;
				}

				//check if this component can collide with the other component
				for(CollisionID thisid : thiscc.getCollidingIDs()){
					if(othercc.getID() == thisid) thiscollides = true;
				}

				if(thiscollides || othercollides){
					if(thiscc.Collision(othercc)){
						collidedThisFrame = true;
						if(thiscollides){
							this.addCollidingEntity(e);
						}
						if(othercollides){
							e.addCollidingEntity(this);
						}
						//System.out.println(collidingentities.size()); //debug
						return;
					}
				}
			}
		}
	}

	/**Convenience method for add to this entity's list of colliding entities
	 * @param e The entity to add
	 */
	public void addCollidingEntity(Entity e){
		collidingentities.addElement(e);
	}

	/**Get the list containing the entities this entity is currently colliding with
	 * @return The list
	 */
	public Vector<Entity> getCollidingEntities(){
		return this.collidingentities;
	}

	/**
	 * @return The current health of this entity
	 */
	public float getHealth() {
		return health;
	}

	/**Set the health of this entity, which will get clamped to the max health.
	 * @param health The health
	 */
	public void setHealth(float health) {
		this.health = Util.clamp(health, 0f, maxHealth);
	}

	/**
	 * Update all components that require a rotation update
	 */
	private void updateRotation(){
		for(MovementNotifier mn : movenotifiercomps){
			if(((Component)mn).isEnabled())
				mn.onRotationUpdate();
		}
	}

	/**
	 * Update all components that require a position update
	 */
	private void updatePosition(){
		for(MovementNotifier mn : movenotifiercomps){
			if(((Component)mn).isEnabled())
				mn.onPositionUpdate();
		}		
	}

	/**
	 * Update all components that require a position update
	 */
	private void updateScale(){
		for(MovementNotifier mn : movenotifiercomps){
			if(((Component)mn).isEnabled())
				mn.onScaleUpdate();
		}
	}

	/**Take the world-space vector and convert it to a vector as seen by the entity (relative to the entity) in entity-space
	 * @param v The world-space vector
	 * @return The vector in entity-space
	 */
	public Vector2f toEntitySpace(Vector2f v){
		Transform t = Transform.IDENTITY;		
		t = Transform.rotate(t, theta);
		t = Transform.scale(t, scale);		
		t = Transform.translate(t, position);
		t = t.getInverse();
		return t.transformPoint(v);
	}

	/**Increase the rotation
	 * @param dtheta Change in rotation
	 */
	public void addToRotation(float dtheta){
		setTheta(theta + dtheta);
	}

	/**
	 * @return The rotation of the entity
	 */
	public float getTheta() {
		return theta;
	}	

	/**
	 * @return The scale of the entity
	 */
	public Vector2f getScale() {
		return scale;
	}

	/**Set the scale of the entity
	 * @param scale The scale
	 */
	public void setScale(Vector2f scale) {
		this.scale = scale;
		updateScale();
	}

	/**Set the rotation of entity
	 * @param theta The rotation
	 */
	public void setTheta(float theta) {
		this.theta = theta;
		updateRotation();
	}

	/**Increase the scale of the entity
	 * @param ds The change in scale
	 */
	public void addToScale(Vector2f ds){
		setScale(Vector2f.add(scale, ds));
	}

	/**Add to the position of the entity
	 * @param dp The change in position
	 */
	public void addToPosition(Vector2f dp){
		setPosition(Vector2f.add(position, dp));		
	}

	/**
	 * Clamp the position to the specified max and min bounds
	 */
	private void clampPos(){
		position = new Vector2f(Util.clamp(position.x, minpositionclamp.x, maxpositionclamp.x),
				Util.clamp(position.y, minpositionclamp.y, maxpositionclamp.y));
	}


	/**
	 * Draw the entity, i.e. the drawable components
	 */
	public void draw(){
		for(DrawableComponent dc : drawcomps){
			if(((Component)dc).isEnabled())
				dc.draw(graphics);
		}
	}

	/**
	 * Update the notifier components
	 */
	protected void updateNotifierComponents(){
		for(NotifierComponent nc : notifiercomps){
			if(nc.isEnabled())
				nc.notifyWhenConditionMet();
		}
	}

	/**Update the entity, i.e. all updateable components and also notify all the notifier components. Also check the health of the entity and remove if necessary.
	 * @param dt Change in time from previous frame
	 * @param t Total time elapsed of the game
	 */
	public void update(float dt, float t){

		updateNotifierComponents();
		//damage the entity according to all entities colliding with it
		for(Entity colliding : collidingentities){
			this.doDamage(colliding.getDamage());
		}
		//System.out.println(collidingentities.size());
		collidingentities.clear(); //clear the colliding entities since it is handled
		previous_dt = dt;
		previous_pos = position;
		for(UpdateableComponent uc : updatecomps){
			if(((Component)uc).isEnabled())
				uc.update(dt, t);
		}	

		collidedThisFrame = false;
		if(this.health <= 0f) remove = true;
	}

	public boolean collidedThisFrame(){
		return collidedThisFrame;
	}

	/**Add a component of the various types to change the entity's behaviour or appearance
	 * @param c The component to add
	 */
	public void addComponent(Component c){
		if(c instanceof UpdateableComponent){
			updatecomps.addElement((UpdateableComponent)c);
		}
		if(c instanceof DrawableComponent){
			drawcomps.addElement((DrawableComponent)c);
		}
		if(c instanceof MovementNotifier){
			movenotifiercomps.addElement((MovementNotifier)c);
		}
		if(c instanceof CollisionComponent){
			collisioncomps.addElement((CollisionComponent)c);
		}
		if(c instanceof NotifierComponent){
			notifiercomps.addElement((NotifierComponent)c);
		}
	}

	/**
	 * @return The components in this entity that can be collided with
	 */
	public Vector<CollisionComponent> getCollisionComponents(){
		return collisioncomps;
	}	

	/**
	 * @return The position of this entity in world-space
	 */
	public Vector2f getPosition() {
		return position;
	}

	/**Set the position of the entity
	 * @param position The position
	 */
	public void setPosition(Vector2f position) {
		this.position = position;
		clampPos();
		updatePosition();
	}


	/**Approximates the instantaneous velocity of the entity
	 * @return The instantaneous velocity
	 */
	public Vector2f getInstantaneousVelocity(){
		return Vector2f.div(Vector2f.sub(position, previous_pos), previous_dt); //v = dp/dt ~= (p1-p0)/dt
	}

	public float getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(float maxHealth) {
		this.maxHealth = maxHealth;
	}




}
