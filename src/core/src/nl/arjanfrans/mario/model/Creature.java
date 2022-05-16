/** @file Creature.java
 */

package nl.arjanfrans.mario.model;

import com.badlogic.gdx.graphics.g2d.Animation;

/**
 * @brief This class is the class model to represent any moving actor that is interactive, and not Mario
 */
public abstract class Creature extends MovingActor {

	/** @brief Constructor method
     *  @details Method which initializes and instance of Creature
     *  @param world World object which defines which game instance the creature exists in
     *  @param positionX The initial x coordinate for the creature
     *  @param positionY The initial y coordinate for the creature
     *  @param f Height of the creature
     *  @return An instance of Creature
	 */
	public Creature(World world, float positionX, float positionY, float f) {
		super(world, positionX, positionY, f);
	}

	/** @brief Gets the animation of the creature
	 */
	public abstract Animation getAnimation();

	/** @brief Eliminates the creature when it dies by falling
	 */
	protected abstract void dieByFalling();

	/** @brief Describes behaviour when creature interacts with object in the X direction
	 */
	@Override
	protected abstract void collisionXAction();

}
