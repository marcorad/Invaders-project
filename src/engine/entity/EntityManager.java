package engine.entity;

import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import engine.graphics.GraphicsHandler;
import engine.input.EventHandler;

public class EntityManager {


	private final Vector<Entity> entities;
	public EntityManager() {
		super();
		entities = new Vector<>();
	}

	public void drawEntities(){
		for(Entity e : entities){
			e.draw();
		}
	}

	public void update(float dt, float t){
		
		//do collisions
		for(int i = 0; i < entities.size(); i++){//the starting entity 
			for(int j = i+1; j<entities.size(); j++){
				//the i'th entity is check against all following entities, because there is no need to recheck collisions
				//since both colliding entities are notified of a collision
				//this reduces the number of checks from (N-1)^2 to ((N-1) + (N-2) + ... + 1) where N is the size of the vector
				entities.get(j-1).collide(entities.get(j));
			}
		}


		//update and remove all necessary entities
		Iterator<Entity> ite= entities.iterator();
		while(ite.hasNext()){
			Entity e = ite.next();
			e.update(dt, t);
			if(e.shouldBeRemoved())ite.remove();				
		}
		
		//System.out.println(entities.size()); //debug purposes
	}

	/** Adds an entity to the manager. Be careful when doing this, since an entity adds itself to the manager.
	 * @param e The entity to add
	 */
	public void addEntity(Entity e){
		entities.addElement(e);
	}



}
