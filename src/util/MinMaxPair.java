package util;

/**Specifies a pair of min and max numbers for code clarity
 * @author Marco
 *
 */
public class MinMaxPair<T> {
	public T min, max;

	/**
	 * @param min The minimum
	 * @param max The maximum
	 */
	public MinMaxPair(T min, T max) {
		this.min = min;
		this.max = max;
	}
}
