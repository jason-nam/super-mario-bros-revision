/** @file D.java
 */

package nl.arjanfrans.mario.debug;

import nl.arjanfrans.mario.MarioGame;

/**
 * @brief Debug class
 */
public class D {

	/** @brief Print debug message
	 *  @param msg String value
	 */
	public static void o(String msg) {
		if(MarioGame.DEBUG) {
			System.out.println(msg);
		}
	}

	/** @brief Print debug message
	 *  @param msg float value
	 */
	public static void o(float msg) {
		if(MarioGame.DEBUG) {
			System.out.println(msg);
		}
	}
}
