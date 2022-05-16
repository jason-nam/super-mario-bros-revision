package nl.arjanfrans.mario.model;

import nl.arjanfrans.mario.actions.ActorActions;
import nl.arjanfrans.mario.audio.Audio;
import nl.arjanfrans.mario.graphics.Tiles;
import nl.arjanfrans.mario.model.brick.BrickShatter;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;

/**
 * @brief This class is the class meant to model the bricks in the game.
 */
public class Brick extends StaticActor {
	private TextureRegion texture;
	private TextureRegion empty_texture;

	private float stateTime;
	private static Animation bonus_animation;
	private int hitcount = 0;
	private int maxhits = 1;
	private boolean destructable;
	private Array<Actor> items;
	private boolean bonus; //Whether this is a bonus tile or not
	//private int coins = 0;
	private Coin coin;

	private BrickShatter shatter;

	/** @brief Constructor method
	 *  @details Method which initializes an instance of Brick.
	 *  @param world - World object which defines which game instance the brick exists in.
	 *  @param x - The initial x coordinate for the brick.
	 *  @param y - The initial y coordinate for the creature.
	 *  @param color - a string representing the brick's color.
	 * 	@param bonus - a boolean indicating the existence of a bonus
	 * 	@param destructable - a boolean indicating the ability to destroy a brick
	 *  @return An instance of Brick
	 */
	public Brick(World world, float x, float y, String color, boolean bonus, boolean destructable) {
		super(world);
		this.bonus = bonus;
		this.destructable = destructable;
		this.setOrigin(x, y);
		this.setBounds(x, y, 16 * (1/16f), 16 * (1/16f));
		bonus_animation = Tiles.getAnimation(0.15f, "bonus_block");
		bonus_animation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
		empty_texture = Tiles.getTile("brick_empty");
		items = new Array<Actor>();
		if(color.equals("brown")) {
			texture = Tiles.getTile("brick");
			empty_texture = Tiles.getTile("brick_empty");
			shatter =  new BrickShatter(this.getX(), this.getY());
		}
	}

	/** @brief This method updates the actor based on time.
	 *  @param delta - Time in seconds since the last frame.
	 */
	@Override
	public void act(float delta) {
		stateTime += delta;
		super.act(delta);
	}
	
	/** @brief Shatters the brick into pieces.
	 */
	private void shatter() {
		this.addAction(Actions.sequence(Actions.delay(0.3f), Actions.alpha(0, 0.1f), ActorActions.removeActor(this)));
	}

	/** @brief This method updates the actor based on time.
	 *  @param parentAlpha - The parent alpha, to be multiplied with this actor's alpha, allowing the parent's alpha to affect all children.
	 * @param batch - an object used to draw 2D rectangles that reference a texture (region).
	 */
	@Override
	public void draw(Batch batch, float parentAlpha) {
		if(destroyed) {
			shatter.draw(batch);
			shatter();
		}
		else {
			if(items.size < 1 && hitcount > 0) {
				batch.draw(empty_texture, this.getX(), this.getY(), this.getWidth(), this.getHeight());
			}
			else if(bonus) {
				batch.draw(bonus_animation.getKeyFrame(stateTime), this.getX(), this.getY(), this.getWidth(), this.getHeight());
			}
			else {
				batch.draw(texture, this.getX(), this.getY(), this.getWidth(), this.getHeight());
			}
		}
	}

	/** @brief This method models the behaviour when the brick has been hit.
	 *  @param mario_level - an integer indicating the size of Mario.
	 */
	public void hit(int mario_level) {
		if(items.size > 0) {
			if(items.peek() instanceof Mushroom) {
				((Mushroom) items.pop()).appear();
			}
		}
		if(mario_level > 1) {
			hitcount++;
			if(items.size == 0) {
				if(destructable) {
					destroyed = true;
				} else  {
					if(hitcount == 1) {
						this.marioGetsCoin();
						//System.out.println("gain coin");


					}
				}
			}
			this.addAction(Actions.sequence(Actions.moveTo(this.getOriginX(), this.getOriginY() + 0.2f, 0.1f, Interpolation.linear),
					Actions.moveTo(this.getOriginX(), this.getOriginY(), 0.1f, Interpolation.linear)));
			Audio.bump.play();
		}
		else {
			this.addAction(Actions.sequence(Actions.moveTo(this.getOriginX(), this.getOriginY() + 0.2f, 0.1f, Interpolation.linear),
					Actions.moveTo(this.getOriginX(), this.getOriginY(), 0.1f, Interpolation.linear)));
			Audio.bump.play();
		}
	}


	/** @brief This method gets the x coordinate of the brick.
	 *  @return a float representing the y coordinate.
	 */
	@Override
	public float getX() {
		// TODO Auto-generated method stub
		return Math.round(super.getX() * 100.0f) / 100.0f;
	}

	/** @brief This method gets the y coordinate of the brick.
	 *  @return a float representing the y coordinate.
	 */
	@Override
	public float getY() {
		// TODO Auto-generated method stub
		return Math.round(super.getY()  * 100.0f) / 100.0f;
	}

	/** @brief This method gets items located inside the brick.
	 *  @return an array of Actor objects.
	 */
	public Array<Actor> getItems() {
		return items;
	}

	/** @brief This method pops the top item located inside the brick.
	 *  @return an Actor object.
	 */
	public Actor popItem() {
		return items.pop();
	}

	/** @brief This method adds an item to the arrat of items located inside the brick.
	 *  @param item - an Actor object.
	 */
	public void addItem(Actor item) {
		items.add(item);
	}

	private void marioGetsCoin() {
		Coin.c += 1;
		// TODO Play coin audio
		// Audio.points.play();
		//System.out.println(this.points);
	}
/*
	public void resetMarioCoin() {
		this.coins = 0;
	}

	public int getCoin() {
		return this.coins;
	}
*/
}
