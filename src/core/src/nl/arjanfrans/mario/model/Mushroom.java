/** @file Mushroom.java
 */

package nl.arjanfrans.mario.model;

import nl.arjanfrans.mario.actions.MoveableActions;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 * @brief Inherited class MovingActor
 */
public abstract class Mushroom extends MovingActor {

	/** @brief Constructor method
	 *  @details Method which initializes an instance of StaticActor
	 *  @param world The world object in which Mario will exist in
	 * @param x coordinate
	 * @param y coordinate
	 * @param max_velocity
	 */
	public Mushroom(World world, float x, float y, float max_velocity) {
		super(world, x, y, max_velocity);
	}

	/** @brief Make mushroom appear
	 */
	public void appear() {
		this.setVisible(true);
		this.addAction(Actions.sequence(Actions.moveTo(this.getX(), this.getY() + this.getHeight(),
				0.3f, Interpolation.linear), MoveableActions.startMovingAction(this)));
	}

	/** @brief Dispose mushroom
	 */
	public abstract void dispose();

}
