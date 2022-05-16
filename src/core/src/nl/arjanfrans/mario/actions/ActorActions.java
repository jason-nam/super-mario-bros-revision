/** @file ActorActions.java
 */

package nl.arjanfrans.mario.actions;

import nl.arjanfrans.mario.actions.MarioActions.stopImmume;
import nl.arjanfrans.mario.model.Mario;
import nl.arjanfrans.mario.model.World;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 * @brief Inherited class Actions
 */
public class ActorActions extends Actions {

	/**
	 * @brief Inherited class Action
	 * @details Remove actor
	 */
	static public class removeActor extends Action {

		/** @brief Constructor method
		 *  @details Method which initializes an instance of removeActor
		 * @param actor Actor object
		 */
		public removeActor(Actor actor) {
			this.actor = actor;
		}

		/** @brief Act to remove actor
		 *  @param delta float value
		 * @return true boolean value
		 */
		public boolean act(float delta) {
			World.objectsToRemove.add(actor);
			return true;
		}
	}

	
}
