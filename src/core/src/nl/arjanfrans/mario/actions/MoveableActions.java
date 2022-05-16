/** @file MoveableActions.java
 */

package nl.arjanfrans.mario.actions;

import nl.arjanfrans.mario.model.MovingActor;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 * @brief Inherited class Actions
 */
public class MoveableActions extends Actions {

	/** @brief Action for Mario's death
	 * @param actor Actor object
	 * @return Die(actor) Action object
	 */
	public static Action DieAction(Actor actor) {
		return new Die(actor);
	}

	/** @brief Inherited class Action
	 */
	static public class Die extends Action {
		private Actor actor;

		/** @brief Constructor method
		 *  @details Method which initializes an instance of Die
		 * @param actor Actor object
		 */
		public Die(Actor actor) {
			this.actor = actor;
		}

		/** @brief Action for Mario dying
		 * @param delta float value
		 * @return true boolean value
		 */
		public boolean act(float delta) {
			((MovingActor) actor).setDead(true);
			return true;
		}
	}

	/** @brief Action for Mario to start moving
	 * @param actor Actor object
	 */
	public static Action startMovingAction(Actor actor) {
		return new startMoving(actor);
	}

	/** @brief Inherited class Action
	 */
	static public class startMoving extends Action {

		/** @brief Constructor method
		 *  @details Method which initializes an instance of startMoving
		 * @param actor Actor object
		 */
		public startMoving(Actor actor) {
			this.actor = actor;
		}

		/** @brief Action for mario to start moving
		 * @param delta
		 * @return true boolean value
		 */
		public boolean act(float delta) {
			((MovingActor) actor).setMoving(true);
			return true;
		}
	}

}
