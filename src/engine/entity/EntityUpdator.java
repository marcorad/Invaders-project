package engine.entity;

import java.util.ArrayList;
import java.util.Iterator;

import game.Game;

/**
 * An update thread. keeps track of its own list of entities and manages them accordingly. 
 * This thread will update once doUpdate() is called, after which it will wait.
 */
public class EntityUpdator extends Thread {
	private float t, dt;
	private int ID;
	private static int currID = 0;
	private boolean finished = false;
	private final ArrayList<Entity> entities = new ArrayList<>();

	/**
	 * Construct an updator.
	 */
	public EntityUpdator() {
		super();
		ID = currID;
		currID++;
	}

	/**Add an entity to the internal list. External code must manage allocation.
	 * @param e The entity
	 */
	public synchronized void addEntity(Entity e){
		entities.add(e);
	}

	/**
	 * Waits until the updator has finished by waiting until it releases a lock on itself.
	 */
	public synchronized void waitUntilFinished(){} //equivalent to: public void waitUntilFinished(){synchronized(this){}}

	/**
	 * Do updates on internally stored entities.
     */
	public synchronized void doUpdate(float dt, float t){
		allowUpdating = true;
		this.dt = dt;
		this.t = t;
		this.notify();
	}	

	/**
	 * DIE!
	 */
	public synchronized void finishUp(){
		finished = true;
		this.notify();
	}

	/**
	 * Remove internally stored entities on the next frame.
	 */
	public synchronized void clearEntities(){		
		for(Entity e : entities){
			e.remove();
		}
	}

	private boolean allowUpdating = false;

	@Override
	public void run() {
		synchronized(this){
			while(!finished){
				if(allowUpdating){ //ensures that when the thread is started, that it will wait until doUpdate() is called					
					Iterator<Entity> it = entities.iterator();					
					while(it.hasNext()){
						Entity e = it.next();
						if(Game.concurrentUpdatesEnabled()){
							synchronized(e){ //in case another thread requires to access this entity in an update
								e.update(dt, t);
							}
						}
						if(e.shouldBeRemoved()) {
							it.remove();
						}

					}					
				}
				allowUpdating = false;
				try {
					while(!(allowUpdating || finished)) {
						this.wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		//System.out.println("Thread " + ID + " terminated");
	}

	//OLD TEST CODE FOR REFERENCE
	//	private static int numIt = 0;
	//
	//	public static void main(String[] args){
	//		EntityUpdator updators[] = new EntityUpdator[8];
	//
	//		Vector<Entity> entities = new Vector<>();
	//		for(int i = 0; i < 1000; i++){
	//			entities.addElement(null);
	//		}
	//
	//		for(int i = 0; i < updators.length; i++){
	//			updators[i] = new EntityUpdator(entities);
	//			updators[i].start();
	//		}		
	//		Clock clk = new Clock();
	//		Clock mt_time = new Clock();
	//		long time_since_update = 0;
	//
	//		while(numIt < 2){
	//
	//			time_since_update += clk.restart().asMicroseconds();
	//
	//			if(time_since_update >= 10000){
	//
	//				time_since_update -= 10000;
	//				mt_time.restart();
	//				int part_size = entities.size()/updators.length;
	//				for(int i = 0; i < updators.length; i++){
	//					EntityUpdator eu = updators[i];
	//					eu.partitionWork(i*part_size, i == updators.length-1 ? entities.size() : (i+1)*part_size, 0f, 0f);
	//					eu.doUpdate();				
	//				}
	//				for(EntityUpdator eu : updators){
	//					eu.waitUntilFinished();
	//				}
	//
	//				System.out.println("MT on IT " + numIt + ": " + mt_time.restart().asMicroseconds());
	//				numIt++;
	//
	//			}	
	//		}
	//
	//
	//		//have all the threads terminate
	//
	//		for(EntityUpdator eu: updators){			
	//			eu.finishUp();
	//			try {
	//				eu.join();
	//			} catch (InterruptedException e) {
	//				e.printStackTrace();
	//			}
	//		}
	//
	//		System.out.println("DONE");
	//	}
	//
	//
	//
	//
	//\
}
