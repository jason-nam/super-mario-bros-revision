/** @file DesktopLauncher.java
 */ 

package nl.arjanfrans.mario.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import nl.arjanfrans.mario.MarioGame;

/**
 * @brief This class is the main method allowing the game to initialize and launch
 */
public class DesktopLauncher {
	
	/** @brief Main method
     *  @details Method which is launched to trigger the initialization of the game through libGDX
     *  @param arg - an array of string arguments from the command line
	 */
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new MarioGame(), config);
	}
}
