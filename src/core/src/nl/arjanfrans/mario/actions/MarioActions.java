/** @file MarioActions.java
 */

package nl.arjanfrans.mario.actions;

import nl.arjanfrans.mario.model.Flag;
import nl.arjanfrans.mario.model.Mario;
import nl.arjanfrans.mario.model.MovingActor;
import nl.arjanfrans.mario.model.MovingActor.Direction;
import nl.arjanfrans.mario.model.MovingActor.State;
import nl.arjanfrans.mario.model.World;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 * @brief Inherited class Actions
 */
public class MarioActions extends Actions {

	/** @brief Stopping immune actions
	 * @param actor Mario object
	 * @return true boolean value
	 */
	public static Action stopImmumeAction(Mario actor) {
		return new stopImmume(actor);
	}

	/** @brief Inherited class Action
	 */
	static public class stopImmume extends Action {

		/** @brief Constructor method
		 *  @details Method which initializes an instance of stopImmume
		 * @param actor Mario object
		 */
		public stopImmume(Mario actor) {
			this.actor = actor;
		}

		/** @brief Action of stop of Mario being immune
		 * @param delta float value
		 * @return true boolean value
		 */
		public boolean act(float delta) {
			((Mario) actor).setImmume(false);
			return true;
		}
	}


	/** @brief Actions for big mario
	 * @param actor Mario object
	 * @return bigMario(actor) Action object
	 */
	public static Action bigMarioAction(Mario actor) {
		return new bigMario(actor);
	}

	/** @brief Inherited class Action
	 */
	static public class bigMario extends Action {

		/** @brief Constructor method
		 *  @details Method which initializes an instance of bigMario
		 * @param actor Mario object
		 */
		public bigMario(Mario actor) {
			this.actor = actor;
		}

		/** @brief Actions for big mario
		 * @param delta float value
		 * @return true boolean value
		 */
		public boolean act(float delta) {
			((Mario) actor).setImmume(false);
			return true;
		}
	}

	/** @brief Taking down the flag action
	 * @param flag Flag object
	 * @return flagTakeDown(flag)
	 */
	public static Action flagTakeDownAction(Flag flag) {
		return new flagTakeDown(flag);
	}

	/** @brief Inherited class Action
	 */
	static public class flagTakeDown extends Action {
		private Flag flag;

		/** @brief Constructor method
		 *  @details Method which initializes an instance of flagTakeDown
		 * @param flag Flag object
		 */
		public flagTakeDown(Flag flag) {
			this.flag = flag;
			flag.takeDown();
		}

		/** @brief Flag actions
		 * @param delta float value
		 * @return flag.isDown() boolean value
		 */
		public boolean act(float delta) {
			return flag.isDown();
		}
	}
	
	
	/**
	 * Set the World reset_flag to true
	 * @return finishLevel() Action object
	 */
	public static Action finishLevelAction() {
		return new finishLevel();
	}

	/** @brief Inherited class Action
	 */
	static public class finishLevel extends Action {

		/** @brief Action for finish of ldevel
		 * @param delta float value
		 * @return true boolean value
		 */
		public boolean act(float delta) {
			World.reset_flag = true;
			return true;
		}
	}

	/** @brief Set action state
	 * @param actor Mario object
	 * @param state State object
	 * @return setState(actor, state) Action object
	 */
	public static Action setStateAction(Mario actor, State state) {
		return new setState(actor, state);
	}

	/** @brief Inherited class Action
	 */
	static public class setState extends Action {
		private State state;

		/** @brief Constructor method
		 *  @details Method which initializes an instance of setState
		 * @param actor Mario object
		 * @param state State object
		 */
		public setState(Mario actor, State state) {
			this.actor = actor;
			this.state = state;
		}

		/** @brief Action for setting state
		 * @param delta float value
		 * @return true boolean value
		 */
		public boolean act(float delta) {
			((Mario) actor).setState(state);
			return true;
		}
	}

	/** @brief Action for walking to
	 * @param actor Mario object
	 * @param x coordinate
	 * @param y coordinate
	 */
	public static Action walkToAction(Mario actor, float x, float y) {
		return new walkTo(actor, x, y);
	}

	/** @brief Inherited class Action
	 */
	static public class walkTo extends Action {
		private float x;
		private float y;

		/** @brief Constructor method
		 *  @details Method which initializes an instance of walkTo
		 * @param actor Mario object
		 * @param x coordinate
		 * @param y coordinate
		 */
		public walkTo(Mario actor, float x, float y) {
			this.actor = actor;
			this.x = x;
			this.y = y;
			((Mario) actor).setControlsEnabled(false);
			
		}

		/** @brief Action for walking
		 * @param delta float value
		 */
		public boolean act(float delta) {
			if(actor.getX() < x) {
				((Mario) actor).move(Direction.RIGHT);
				return false;
			}
			return true;
		}
	}

}
