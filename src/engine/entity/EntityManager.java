package engine.entity;

import java.util.ArrayList;
import java.util.Iterator;

import org.jsfml.system.Vector2f;

import game.Game;
import util.Benchmarker;
import util.Util;

/**
 * Manages the entities in the game
 */
public class EntityManager {

	public static boolean DRAW_HITBOXES = false;
	//private static Benchmarker collbm = new Benchmarker("Collision time", 60);
	//private static Benchmarker upbm = new Benchmarker("Update time", 60);
	EntityUpdator updators[] = new EntityUpdator[8];

	public void toggleHitboxDraw(){
		DRAW_HITBOXES = !DRAW_HITBOXES;
		for(Entity e: entities){
			e.setHitboxDraw(DRAW_HITBOXES);
		}
	}

	private final ArrayList<Entity> entities;
	private final ArrayList<Entity> add = new ArrayList<>(); //to avoid modifying entities while iterating
	
	/**
	 * Construct an entity manager. No parameters needed.
	 */
	public EntityManager() {
		super();
		entities = new ArrayList<>();	
		for(int i = 0; i < updators.length; i++){
			updators[i] = new EntityUpdator();
			updators[i].start();
		}
	}

	/**
	 * Draw all the entities in the list.
	 */
	public void drawEntities(){
		for(Entity e : entities){
			e.draw();			
		}
	}

	private int thread_to_add = 0;

	/**Update all the entities in the list. Collisions are single threaded. This is mostly due to 
	 * the strange things that may happen when this is multithreaded (we tried it). Updates are linear or concurrent,
	 * depending on the dev setting of the game. The concurrent updates can deal with bigger loads, but may cause bugs.
	 * At the time of this documentation, all bugs are believed to be fixed.
	 * Concurrent updates are run in 8 threads. It may yield up to 4x improvement for big loads, and may take slightly longer
	 * for small loads.
	 * @param dt The time elapsed since the previous frame
	 * @param t The total time elapsed in the game
	 */
	public void update(float dt, float t){
		for(Entity e : add){
			entities.add(e);
			updators[thread_to_add].addEntity(e);
			thread_to_add = (thread_to_add + 1)%updators.length; //add in a round-robin fashion
		}
		add.clear();
		//do collisions
		//collbm.startMeasurement();
		for(int i = 0; i < entities.size(); i++){//the starting entity 
			for(int j = i+1; j<entities.size(); j++){
				//the i'th entity is check against all following entities, because there is no need to recheck collisions
				//since both colliding entities are notified of a collision
				//this reduces the number of checks from (N-1)^2 to ((N-1) + (N-2) + ... + 1) where N is the size of the vector

				Entity e1 = entities.get(i);
				Entity e2 = entities.get(j);
				//only checks within a radius of .3, which has some performance improvements
				if( Util.magSquared(Vector2f.sub(e1.getPosition(), e2.getPosition())) <= .3f*.3f ) {
					e1.collide(e2);
				}
			}
		}
		//collbm.stopMeasurement();

		//update and remove all necessary entities
		//upbm.startMeasurement();
		for(EntityUpdator eu: updators){
			eu.doUpdate(dt, t);
		}

		for(EntityUpdator eu: updators){
			eu.waitUntilFinished();
		}


		Iterator<Entity> ite= entities.iterator();
		while(ite.hasNext()){
			Entity e = ite.next();
			if (!Game.concurrentUpdatesEnabled() ) {
				e.update(dt, t);
			}
			if(e.shouldBeRemoved()) {
				ite.remove();
			}				
		}	
		//upbm.stopMeasurement();
		//System.out.println(entities.size());
	}

	/** Adds an entity to the manager before the next frame. Be careful when doing this, since an entity adds itself to the manager.
	 * @param e The entity to add
	 */
	public synchronized void addEntity(Entity e){		
		add.add(e);	
	}


	/**
	 * Clear all the entities from the screen and entities about to be added on the next frame without killing them and invoking OnDeathComponents.
	 */
	public void clearEntities(){
		for(Entity e: entities){ 
			e.remove();
		}

		for(EntityUpdator eu: updators){
			eu.clearEntities();
		}

		add.clear();
	}

	/**
	 * Make sure the update threads DIE!
	 */
	public void killThreads(){
		for(EntityUpdator eu: updators){
			eu.finishUp();
		}
	}

}
