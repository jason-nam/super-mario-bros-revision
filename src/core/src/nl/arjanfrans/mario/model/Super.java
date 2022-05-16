/** @file Super.java
 */

package nl.arjanfrans.mario.model;

import nl.arjanfrans.mario.graphics.Tiles;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * @brief Inherited class that overrides Mushroom methods that represents mario in super state
 */
public class Super extends Mushroom {
	private static TextureRegion texture;
	protected Rectangle rect = new Rectangle();

	/** @brief Constructor method
	 *  @details Method which initializes an instance of Super
	 *  @param world The world object in which Mario will exist in
	 * @param x coordinate of super mushroom
	 * @param y coordinate of super mushroom
	 * @param max_velocity of super mushroom
	 */
	public Super (World world, float x, float y, float max_velocity) {
		super(world, x, y, max_velocity);
		texture = Tiles.getTile("mushroom_super");
		this.setBounds(x, y, texture.getRegionWidth() * World.scale, texture.getRegionHeight() * World.scale);
		direction = Direction.RIGHT;
		moving = false;
		this.setVisible(false);
	}

	/**
	 * @copydoc Mushroom::draw()
	 */
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(texture, this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}

	/**
	 * @copydoc Mushroom::act()
	 */
	@Override
	public void act(float delta) {
		super.act(delta);
		move(direction);
		rect.set(this.getX(), this.getY(), this.getWidth(), this.getHeight());
		if(moving) applyPhysics(rect);
	}

	/**
	 * @copydoc Mushroom::dieByFalling()
	 */
	@Override
	protected void dieByFalling() {
		if(this.getY() < -3f) {
			setDead(true);
		}
	}

	/**
	 * @copydoc Mushroom::collisionXAction()
	 */
	@Override
	protected void collisionXAction() {
		if(direction == Direction.LEFT) {
			direction = Direction.RIGHT;
		}
		else {
			direction = Direction.LEFT;
		}
	}

	/**
	 * @copydoc Mushroom::dispose()
	 */
	@Override
	public void dispose() {
		texture.getTexture().dispose();
	}


}
