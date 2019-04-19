package engine.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import engine.graphics.GraphicsHandler;
import engine.input.EventHandler;
import util.Benchmarker;

/**Manages the entities in the game
 * @author Marco
 *
 */
public class EntityManager {

	public static boolean DRAW_HITBOXES = false;
	private static Benchmarker collbm = new Benchmarker("Collision time", 60);
	private static Benchmarker upbm = new Benchmarker("Update time", 60);

	public void toggleHitboxDraw(){
		DRAW_HITBOXES = !DRAW_HITBOXES;
		for(Entity e: entities){
			e.setHitboxDraw(DRAW_HITBOXES);
		}
	}


	private final ArrayList<Entity> entities;
	private final ArrayList<Entity> add = new ArrayList<>(); //to avoid modifying entities while iterating
	/**
	 * 
	 * Construct an entity manager. No parameters needed.
	 */
	public EntityManager() {
		super();
		entities = new ArrayList<>();
	}

	/**
	 * Draw all the entities in the list.
	 */
	public void drawEntities(){
		for(Entity e : entities){
			e.draw();
		}
	}

	/**Update all the entities in the list
	 * @param dt The time elapsed since the previous frame
	 * @param t The total time elapsed in the game
	 */
	public void update(float dt, float t){

		for(Entity e : add){
			entities.add(e);
		}
		add.clear();		

		//do collisions
		collbm.startMeasurement();
		for(int i = 0; i < entities.size(); i++){//the starting entity 
			for(int j = i+1; j<entities.size(); j++){
				//the i'th entity is check against all following entities, because there is no need to recheck collisions
				//since both colliding entities are notified of a collision
				//this reduces the number of checks from (N-1)^2 to ((N-1) + (N-2) + ... + 1) where N is the size of the vector
				entities.get(i).collide(entities.get(j));
			}
		}
		collbm.stopMeasurement();


		//update and remove all necessary entities
		upbm.startMeasurement();
		Iterator<Entity> ite= entities.iterator();
		while(ite.hasNext()){
			Entity e = ite.next();
			e.update(dt, t);
			if(e.shouldBeRemoved())ite.remove();				
		}	
		upbm.stopMeasurement();

		//System.out.println(entities.size());
	}

	/** Adds an entity to the manager before the next frame. Be careful when doing this, since an entity adds itself to the manager.
	 * @param e The entity to add
	 */
	public synchronized void addEntity(Entity e){		
		add.add(e);	
	}


	/**
	 * Clear all the entities from the screen on the next frame without killing them and invoking OnDeathComponents.
	 */
	public void clearEntities(){
		for(Entity e: entities){ 
			e.remove();;
		}
	}


}
