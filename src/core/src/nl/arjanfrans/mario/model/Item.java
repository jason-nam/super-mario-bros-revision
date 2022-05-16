/** @file Item.java
 */

package nl.arjanfrans.mario.model;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * @brief Represents any item in game
 */
public class Item extends Actor {
	protected World world; /**< The world in which the item exists */
	
	/** @brief Constructor method
     *  @details Method which initializes an instance of Item
     *  @param world The world in which the Goomba will exist in
     *  @param visible If the item is visible or not
     *  @return An instance of Item
	 */
	public Item(World world, boolean visible) {
		this.world = world;
		this.setVisible(visible);
	}

}
