package engine.entity;

import java.util.Vector;

import org.jsfml.system.Clock;
import org.jsfml.system.Vector2f;

import game.Game;

public class EntityUpdator extends Thread {
	private Vector<Entity> entities;
	private int start_index, end_index; //work partitioning from [start, end)
	private float t, dt;
	private int ID;
	private static int currID = 0;
	private boolean finished = false;

	public EntityUpdator(Vector<Entity> entities) {
		super();
		this.entities = entities;
		ID = currID;
		currID++;
	}



	/** Partition the entities this thread must update. The threads updates entities entities in the range [start, end).
	 * @param start Start index
	 * @param end End index
	 * @param dt The time step difference to update with
	 * @param t The total time passed to update with
	 */
	public void partitionWork(int start, int end, float dt, float t){
		start_index = start;
		end_index = end;
		this.dt = dt;
		this.t = t;
	}

	/**
	 * Waits until the updator has finished by waiting until it releases a lock on itself.
	 */
	public synchronized void waitUntilFinished(){} //equivalent to: public void waitUntilFinished(){synchronized(this){}}

	public synchronized void doUpdate(){
		allowUpdating = true;
		this.notify();
	}

	public synchronized void finishUp(){
		finished = true;
		this.notify();
	}

	private boolean allowUpdating = false;

	@Override
	public void run() {
		synchronized(this){
			while(!finished){
				if(allowUpdating)
					for(int i = start_index; i < end_index; i++){
						//entities.elementAt(i).update(dt, t);
						//System.out.println("Thread " + ID + " updated entity " + i);
					}
				allowUpdating = false;
				try {
					while(!(allowUpdating || finished)) //required as per wait method specification, to stop the rare spurious thread wake-up
						this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("Thread " + ID + " terminated");
	}
	private static int numIt = 0;

	public static void main(String[] args){
		EntityUpdator updators[] = new EntityUpdator[8];

		Vector<Entity> entities = new Vector<>();
		for(int i = 0; i < 1000; i++){
			entities.addElement(null);
		}

		for(int i = 0; i < updators.length; i++){
			updators[i] = new EntityUpdator(entities);
			updators[i].start();
		}		
		Clock clk = new Clock();
		Clock mt_time = new Clock();
		long time_since_update = 0;

		while(numIt < 2){

			time_since_update += clk.restart().asMicroseconds();

			if(time_since_update >= 10000){

				time_since_update -= 10000;
				mt_time.restart();
				int part_size = entities.size()/updators.length;
				for(int i = 0; i < updators.length; i++){
					EntityUpdator eu = updators[i];
					eu.partitionWork(i*part_size, i == updators.length-1 ? entities.size() : (i+1)*part_size, 0f, 0f);
					eu.doUpdate();				
				}
				for(EntityUpdator eu : updators){
					eu.waitUntilFinished();
				}

				System.out.println("MT on IT " + numIt + ": " + mt_time.restart().asMicroseconds());
				numIt++;

			}	
		}


		//have all the threads terminate

		for(EntityUpdator eu: updators){			
			eu.finishUp();
			try {
				eu.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println("DONE");
	}




}
