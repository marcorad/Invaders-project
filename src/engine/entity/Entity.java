package engine.entity;

import java.util.ArrayList;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Transform;
import org.jsfml.system.Vector2f;

import engine.component.CollisionComponent;
import engine.component.CollisionID;
import engine.component.Component;
import engine.component.DrawableComponent;
import engine.component.EntityMovementListener;
import engine.component.NotifierComponent;
import engine.component.SpriteComponent;
import engine.component.UpdateableComponent;
import engine.graphics.GraphicsHandler;
import engine.input.EventHandler;
import game.Game;
import util.Util;

/**
 * The base entity. It has components that specifies behaviour. 
 * It also has a position, a scale, a rotation, health and damage done to other entities.
 * It adds itself to the entity manager.
 * Rotation is specified from the positive Y-axis in normalised world coordination. This is due to how
 * JSFML deals with rotation.
 * The entity may have multiples of some components, such as sprites or collision component, however, only one is ever used
 * at most. The lists are still there, allowing for possible future expansion. 
 */
public class Entity{

	public static EntityManager entitymanager;
	public static GraphicsHandler graphics;
	public static EventHandler eventhandler;
	public static Game game;
	
	protected float maxHealth = .0001f;
	protected float health = .0001f; //health of entity, entity gets removed once health reaches 0
	protected float damage = 0f; //the damage value that this entity can deal, only really used for projectiles
	private boolean collidedThisFrame = false;
	private SpriteComponent sprite; //if the entity has a sprite, this will store reference to it


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
	protected ArrayList<UpdateableComponent> updatecomps= new ArrayList<>();
	protected ArrayList<DrawableComponent> drawcomps= new ArrayList<>();;
	protected ArrayList<EntityMovementListener> movenotifiercomps= new ArrayList<>();
	protected ArrayList<CollisionComponent> collisioncomps= new ArrayList<>();
	protected ArrayList<NotifierComponent> notifiercomps = new ArrayList<>();

	protected float previous_dt = 1.f;

	protected ArrayList<Entity> collidingentities = new ArrayList<>();

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
		setTheta(rotation);
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
		//System.out.println(health);
	}

	/**
	 * Set the entity's health to zero, causing it to be removed, and all required death notifications to be made.
	 */
	public void kill(){
		health = 0f;
		remove = true;
	}
	
	/**
	 * Remove the entity from the list, without killing it.
	 */
	public void remove(){
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
		ArrayList<CollisionComponent> other = e.getCollisionComponents();
		for(CollisionComponent thiscc : collisioncomps){
			for(CollisionComponent othercc : other){
				boolean thiscollides = false, othercollides = false;

				//check if the other component can collide with this component
				for(CollisionID otherid : othercc.getCollidingIDs()){
					if(thiscc.getID() == otherid) {
						othercollides = true;
					}
				}

				//check if this component can collide with the other component
				for(CollisionID thisid : thiscc.getCollidingIDs()){
					if(othercc.getID() == thisid) {
						thiscollides = true;
					}
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

	/**Convenience method for add to this entity's list of colliding entities. Synchronized for possible multithreaded collisions.
	 * @param e The entity to add
	 */
	public synchronized void addCollidingEntity(Entity e){
		collidingentities.add(e);
	}

	/**Get the list containing the entities this entity is currently colliding with
	 * @return The list
	 */
	public ArrayList<Entity> getCollidingEntities(){
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
		for(EntityMovementListener mn : movenotifiercomps){
			if(((Component)mn).isEnabled()) {
				mn.onRotationUpdate();
			}
		}
	}

	/**
	 * Update all components that require a position update
	 */
	private void updatePosition(){
		for(EntityMovementListener mn : movenotifiercomps){
			if(((Component)mn).isEnabled()) {
				mn.onPositionUpdate();
			}
		}		
	}

	/**
	 * Update all components that require a position update
	 */
	private void updateScale(){
		for(EntityMovementListener mn : movenotifiercomps){
			if(((Component)mn).isEnabled()) {
				mn.onScaleUpdate();
			}
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

	/**Gets the scale of this entity.
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
			if(((Component)dc).isEnabled()) {
				dc.draw(graphics);
			}
		}
	}

	/**
	 * Update the notifier components
	 */
	protected void updateNotifierComponents(){
		for(NotifierComponent nc : notifiercomps){
			if(nc.isEnabled()) {
				nc.notifyWhenConditionMet();
			}
		}
	}
	
	public void setHitboxDraw(boolean b){
		for(CollisionComponent cc : collisioncomps){
			cc.setHitboxDraw(b);
		}
	}

	/**Update the entity, i.e. all updateable components and also notify all the notifier components. Also check the health of the entity and remove if necessary.
	 * @param dt Change in time from previous frame
	 * @param t Total time elapsed of the game
	 */
	public void update(float dt, float t){

		
		//damage the entity according to all entities colliding with it
		for(Entity colliding : collidingentities){
			this.doDamage(colliding.getDamage());
		}
		
		updateNotifierComponents();//do after damage taken since some components might be notified on death
		
		//System.out.println(collidingentities.size());
		collidingentities.clear(); //clear the colliding entities since it is handled
		previous_dt = dt;
		previous_pos = position;
		for(UpdateableComponent uc : updatecomps){
			if(((Component)uc).isEnabled()){
				//System.out.println(uc);
				uc.update(dt, t);
			}
		}	

		collidedThisFrame = false;
		if(this.health <= 0f) {
			remove = true;
		}
	}

	public boolean collidedThisFrame(){
		return collidedThisFrame;
	}

	/**Add a component of the various types to change the entity's behaviour or appearance
	 * @param c The component to add
	 */
	public void addComponent(Component c){
		if(c instanceof UpdateableComponent){
			updatecomps.add((UpdateableComponent)c);
		}
		if(c instanceof DrawableComponent){
			drawcomps.add((DrawableComponent)c);
		}
		if(c instanceof EntityMovementListener){
			movenotifiercomps.add((EntityMovementListener)c);
		}
		if(c instanceof CollisionComponent){
			collisioncomps.add((CollisionComponent)c);
		}
		if(c instanceof NotifierComponent){
			notifiercomps.add((NotifierComponent)c);
		}
		if(c instanceof SpriteComponent){
			sprite = (SpriteComponent)c;
		}
	}

	/**
	 * @return The components in this entity that can be collided with
	 */
	public ArrayList<CollisionComponent> getCollisionComponents(){
		return collisioncomps;
	}	

	/**
	 * @return The position of this entity in world-space
	 */
	public Vector2f getPosition() {
		return position;
	}
	
	/**Return the colour of the related sprite. If it does not have a sprite, returns black.
	 * @return The sprite colour
	 */
	public Color getSpriteColour(){
		if(sprite != null){
			return sprite.getColor();
		} else {
			return Color.BLACK;
		}
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

	/**
	 * @return The max health
	 */
	public float getMaxHealth() {
		return maxHealth;
	}

	/**
	 * @param maxHealth The max health
	 */
	public void setMaxHealth(float maxHealth) {
		this.maxHealth = maxHealth;
	}

}
