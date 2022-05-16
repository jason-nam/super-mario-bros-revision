/** @file MarioInput.java
 */

package nl.arjanfrans.mario.input;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

/**
 * @brief Inherited class that overrides mario input methods
 */
public class MarioInput extends InputListener {

	/**
	 * @copydoc InputListener::touchDown()
	 */
	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer,
			int button) {
		return super.touchDown(event, x, y, pointer, button);
	}

	/**
	 * @copydoc InputListener::touchUp()
	 */
	@Override
	public void touchUp(InputEvent event, float x, float y, int pointer,
			int button) {
		super.touchUp(event, x, y, pointer, button);
	}

	/**
	 * @copydoc InputListener::keyDown()
	 */
	@Override
	public boolean keyDown(InputEvent event, int keycode) {
		return super.keyDown(event, keycode);
	}

	/**
	 * @copydoc InputListener::keyUp()
	 */
	@Override
	public boolean keyUp(InputEvent event, int keycode) {
		return super.keyUp(event, keycode);
	}

}
