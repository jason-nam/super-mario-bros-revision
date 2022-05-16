/** @file Flag.java
 */

package nl.arjanfrans.mario.model;

import nl.arjanfrans.mario.graphics.Tiles;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 * @brief This flag represents the flag pole at the end of the stage that completes
 * if Mario interacts with it
 */
public class Flag extends Actor {

	private Animation animation; /**< Animation that the flag displays */
	private float stateTime; /**<  Internal time representation of object */
	private float endX; /**< X coordinate to describe where the Flag ends */
	private float endY; /**< Y coordinate to describe where the Flag ends */
	private boolean down = false; /**< If the flag has been captured or not */
	private float bottomY; /**< Y coordinate to describe the bottom of the flag */
	private float slideOffset = 2; /**< Y value to compensate for the slide height */
	
	/** @brief Constructor method
     *  @details Method which initializes an instance of Flag
     *  @param x x coordinate of the position of the Flag
     *  @param y y coordinate of the position of the Flag
     *  @param width The width of the Flag
     *  @param height The height of the Flag
     *  @param endX x coordinate to describe where the Flag ends
     *  @param endY y coordinate to describe where the Flag ends
     *  @return An instance of Flag
	 */
	public Flag(float x, float y, float width, float height, float endX, float endY) {
		animation = Tiles.getAnimation(0.15f, "evil_flag");
		animation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
		this.setOrigin(width / 2, height);
		this.setBounds(x + (8 * World.scale), y, 2 * World.scale, height);
		this.setTouchable(Touchable.disabled);
		this.endX = endX;
		this.endY = endY;
		this.bottomY = y - (height - slideOffset);
	}
	
	/** @brief How the Flag acts each time instance
     *  @details After each discrete time step, this method is called and the state time is updated
     *  @param delta The change in time for the actor
	 */
	@Override
	public void act(float delta) {
		stateTime += delta;
		super.act(delta);
	}
	
	/** @brief Mario captures flag
     *  @details Triggered when Mario interacts with the Flag pole
	 */
	public void takeDown() {
		this.addAction(Actions.sequence(
				Actions.delay(0.2f),
				Actions.moveBy(0, -(this.getHeight() - slideOffset), 2f))
			);
	}

	/** @brief Draws the flag
     *  @details Assists libGDX in drawing the Flag into the world, defines position and animation
     *  @param batch Texture region where the Flag is being drawn
	 *  @param parentAlpha Not used
	 */
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(animation.getKeyFrame(stateTime), this.getX() - (1 * World.scale),  this.getY() + (this.getHeight() - slideOffset), 
				animation.getKeyFrame(stateTime).getRegionWidth() * World.scale, animation.getKeyFrame(stateTime).getRegionHeight() * World.scale);
	}
	
	/** @brief Rectangle object in which the Flag exists in
     *  @return Returns libGDX rectangle object with this objects coordinates
	 */
	public Rectangle rect() {
		return new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}

	/** @brief Gets end X coordinate
     *  @return Returns float representing the x coordinate where the rectangle surrounding the Flag ends
	 */
	public float getEndX() {
		return endX;
	}

	/** @brief Gets end Y coordinate
     *  @return Returns float representing the y coordinate where the rectangle surrounding the Flag ends
     */
	public float getEndY() {
		return endY;
	}

	/** @brief Gets whether the flag is down or not
     *  @return Returns boolean of whether the flag has been captured or not
     */
	public boolean isDown() {
		return Math.round(this.getY()) == bottomY;
	}

	/** @brief Sets the flag to the down state
     *  @param down A boolean representing whether the Flag is in the down state or not
     */
	public void setDown(boolean down) {
		this.down = down;
	}

}
