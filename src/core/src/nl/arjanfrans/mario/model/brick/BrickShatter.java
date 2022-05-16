package nl.arjanfrans.mario.model.brick;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * @brief This class is the class meant to model the shattering of the bricks in the game.
 */
public class BrickShatter extends Sprite {
	private BrickPiece piece1;
	private BrickPiece piece2;
	private BrickPiece piece3;
	private BrickPiece piece4;

	/** @brief Constructor method for BrickShatter
	 * @param x - base x coordinate of the brick pieces
	 * @param y - base y coordinate of the brick pieces
	 * @return an instance of BrickShatter
	 */
	public BrickShatter(float x, float y) {
		piece1 = new BrickPiece(x, y + 0.5f, 0);
		piece2 = new BrickPiece(x + 0.5f, y + 0.5f, 1);
		piece3 = new BrickPiece(x, y, 2);
		piece4 = new BrickPiece(x+0.5f, y, 3);
		this.setSize(16, 16);
	}
	/** @brief This is the method meant to draw a BrickShatter
	 * @param batch - an object used to draw 2D rectangles that reference a texture (region).
	 */
	@Override
	public void draw(Batch batch) {
		piece1.draw(batch);
		piece2.draw(batch);
		piece3.draw(batch);
		piece4.draw(batch);
	}
	
	
	

}
