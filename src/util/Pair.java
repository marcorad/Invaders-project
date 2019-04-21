package util;

/**
 * A pair of objects.
 *
 * @param <T1> Some object
 * @param <T2> Another object
 */
public class Pair <T1, T2> {

	public T1 elem1;
	public T2 elem2;
	
	/**Make a pair
	 * @param e1 Object 1
	 * @param e2 Object 2
	 */
	public Pair(T1 e1, T2 e2){
		elem1 = e1;
		elem2 = e2;
	}
	
	
}
