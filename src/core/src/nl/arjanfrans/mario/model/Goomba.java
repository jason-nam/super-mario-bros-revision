/** @file Goomba.java
 */

package nl.arjanfrans.mario.model;

import nl.arjanfrans.mario.actions.MoveableActions;
import nl.arjanfrans.mario.graphics.GoombaAnimation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;

/**
 * @brief Goomba represents the Goomba enemies from the original Mario game
 */
public class Goomba extends Creature {
	protected float max_velocity = 1f; /**< Maximum velocity of Goomba */
	protected GoombaAnimation gfx = new GoombaAnimation(); /**< Animations of Goomba */
	protected Rectangle rect = new Rectangle(); /**< Rectangle object surrounding the Goomba */

	/** @brief Constructor method
     *  @details Method which initializes an instance of Goomba
     *  @param world The world in which the Goomba will exist in
     *  @param positionX x coordinate of the position of the Goomba
     *  @param positionY y coordinate of the position of the Goomba
     *  @return An instance of Goomba
	 */
	public Goomba(World world, float positionX, float positionY) {
		super(world, positionX, positionY, 3f);
		float width = gfx.getDimensions(state).x;;
		float height = gfx.getDimensions(state).y;
		this.setSize(width, height);
		direction = Direction.LEFT;
		moving = false;
	}	
	
	/** @brief When Goomba dies by getting trampled
     *  @details A Goomba will die when getting trampled (Mario steps on Goomba's head). This
     *  results in the Goomba dying and being removed from the world
	 */
	private void dieByTrample() {
		state = State.Dying;
		velocity.set(0, 0);
		this.addAction(Actions.sequence(Actions.moveBy(0, -(2 * 1/16f) ),
				Actions.delay(0.5f),
				MoveableActions.DieAction(this)));
	}

	/** @brief Goomba dies by getting trampled
	 */
	protected void deadByTrample() {
		dieByTrample();
	}
	
	/** @brief Goomba dies by falling off the map
	 */
	protected void dieByFalling() {
		if(this.getY() < -3f) {
			setDead(true);
		}
	}

	/** @brief Draws the Goomba on the GUI
     *  @details Assists libGDX in drawing the Goomba to a specified batch
     *  @param batch The texture region where the Goomba is being drawn
     *  @param parentAlpha Not used
	 */
	@Override
	public void draw(Batch batch, float parentAlpha) {
		TextureRegion frame = gfx.getAnimation(state).getKeyFrame(stateTime);
		if(state == State.Dying) {
			this.setWidth(gfx.getDimensions(State.Dying).x);
			this.setHeight(gfx.getDimensions(State.Dying).y);
			batch.draw(frame, getX(), getY(), 
					getX()+this.getWidth()/2, getY() + this.getHeight()/2,
	                this.getWidth(), this.getHeight(), getScaleX(), getScaleY(), getRotation());
		}
		else {
			if(facesRight) {
				batch.draw(frame, this.getX(), this.getY(), 
						this.getX()+(this.getWidth()/2), this.getY() + this.getHeight()/2,
		                this.getWidth(), this.getHeight(), getScaleX(), getScaleY(), getRotation());
			}
			else {
				batch.draw(frame, this.getX() + this.getWidth(), this.getY(), 
						this.getX()+(this.getWidth()/2), this.getY() + this.getHeight()/2,
		                -this.getWidth(), this.getHeight(), getScaleX(), getScaleY(), getRotation());
			}
		}
	}
	
	/** @brief Determines behaviour when Goomba collides with another creature
     *  @details Checks if this Goomba is colliding with any other Goomba in the world
	 */
	protected void collisionWithCreature() {
		Array<Goomba> goombas = world.getEnemies();
		Rectangle rect = rectangle();
		rect.set(this.getX(), this.getY(), this.getWidth(), this.getHeight());
		for(Goomba goomba : goombas) {
			Rectangle eRect = goomba.rectangle();
			if(goomba != this && eRect.overlaps(rect) && goomba.state != State.Dying) {
				collisionXAction();
			}
		}
	}

	/** @brief Determines how the Goomba acts after each discrete time step
     *  @param delta Float representing the change in time
	 */
	@Override
	public void act(float delta) {
		super.act(delta);
		if(state != State.Dying) {
			move(direction);
			rect.set(this.getX(), this.getY(), this.getWidth(), this.getHeight());
			applyPhysics(rect);
			collisionWithCreature();
		}
	}

	/** @brief Determines behaviour of when Goomba collides with an object in the X direction
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

	/** @brief Gets the animation of this Goomba
     *  @return Animation object representing which animations the Goomba will play
	 */
	@Override
	public Animation getAnimation() {
		return gfx.getAnimation(state);
	}

	/** @brief Removes the Goomba from the world
	 */
	public void dispose() {
		gfx.dispose();
	}
}
