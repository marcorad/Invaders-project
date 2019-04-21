package util;

import org.jsfml.system.Clock;

/**
 * For time measurement in benchmarking phase.
 */
public class Benchmarker {

	private static Benchmarker mainbm;

	private Clock clk;
	private int number_of_readings;
	private int num_taken_measurements;	
	private String name;
	private float curr_average = 0f;
	private float prev_measure[];

	/**Construct a benchmarker object.
	 * @param name The name of the benchmarker, e.g. "Update time"
	 * @param number_of_readings The number of readings that will be averaged.
	 */
	public Benchmarker(String name, int number_of_readings) {
		this.number_of_readings = number_of_readings;
		prev_measure = new float[number_of_readings];
		num_taken_measurements = 0;
		clk = new Clock();
		this.name = name;
	}

	public void setAsMainBenchmarker(){
		mainbm = this;
	}

	/**
	 * Start measuring.
	 */
	public void startMeasurement(){
		clk.restart();
	}
	
	private static void shiftInFront(float x, float[] arr){
		for(int i = arr.length-1; i > 0; i--){
			arr[i] = arr[i-1];
		}
		arr[0] = x;
	}

	/**
	 * Stop measuring. If the number of measurements were met, the average time will be printed on screen along with the percentage of time it is of the main benchmarker, after which another measure cycle begins.
	 */
	public void stopMeasurement(){
		float t = clk.restart().asMicroseconds();
		curr_average -= prev_measure[prev_measure.length-1]; //subtract oldest value
		shiftInFront(t/number_of_readings, prev_measure); //the averaged value
		curr_average += prev_measure[0]; //add newest value
		
		num_taken_measurements++;
		if(num_taken_measurements == number_of_readings){
			num_taken_measurements = 0;
			if(mainbm != null){
				if(this!=mainbm) {
					System.out.printf("%s : %.2f us (%.2f%%)%n", name, curr_average,curr_average/mainbm.curr_average * 100f);
				}
			} else {
				System.out.printf("%s : %.2f us %n", name, curr_average);
			}
		}
	}


}


