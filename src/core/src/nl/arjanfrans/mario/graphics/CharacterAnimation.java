package nl.arjanfrans.mario.graphics;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * @brief This is an abstract class that will be implemented to handle the animations of various characters in the game.
 */
public abstract class CharacterAnimation {

	protected TextureAtlas atlas = new TextureAtlas("data/characters/characters.atlas");

	public static final float scale = 1/16f;

	/**
	 * @brief This method disposes of the animation.
	 */
	public void dispose() {
		atlas.dispose();
	}

}
