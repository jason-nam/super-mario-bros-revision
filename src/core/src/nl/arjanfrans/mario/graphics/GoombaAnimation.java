package nl.arjanfrans.mario.graphics;

import nl.arjanfrans.mario.model.MovingActor.State;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * @brief This is a class that implements the abstract class, CharacterAnimation, specifically for the animation of
 * the Goomba character.
 */
public class GoombaAnimation extends CharacterAnimation {
	private Animation walking;
	private Animation trampled;

	/** @brief Constructor method for GoombaAnimation
	 *  @details Method which initializes an instance of GoombaAnimation.
	 *  @return An instance of GoombaAnimation
	 */
	public GoombaAnimation() {
		Array<AtlasRegion> regions = atlas.findRegions("goomba_walking");
		walking = new Animation(0.2f, regions);
		walking.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
		
		trampled = new Animation(1f, atlas.findRegion("goomba_trampled"));
	}

	/** @brief A method meant to retrieve a Goomba animation based on the Goomba's state.
	 * 	@param state - the state of the Goomba, refers to the State enum class
	 *  @return An instance of Animation
	 */
	public Animation getAnimation(State state) {
		switch(state) {
			case Walking:
				return walking;
			case Dying:
				return trampled;
			default:
				return walking;
		}
	}

	/** @brief A method meant to retrieve the dimensions of a Goomba animation based on the Goomba's state.
	 * 	@param state - the state of the Goomba, refers to the State enum class.
	 *  @return An instance of Vector2.
	 */
	public Vector2 getDimensions(State state) {
		switch(state) {
			case Walking:
				return new Vector2(walking.getKeyFrame(0).getRegionWidth() * scale, 
						walking.getKeyFrame(0).getRegionHeight() * scale);
			case Dying:
				return new Vector2(trampled.getKeyFrame(0).getRegionWidth() * scale, 
						trampled.getKeyFrame(0).getRegionHeight() * scale);
			default:
				return new Vector2(walking.getKeyFrame(0).getRegionWidth() * scale, 
						walking.getKeyFrame(0).getRegionHeight() * scale);
		}
	}
	
	
}
