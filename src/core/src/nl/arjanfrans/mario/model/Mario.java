/** @file Mario.java
 */

package nl.arjanfrans.mario.model;

// Local imports
import nl.arjanfrans.mario.actions.MarioActions;
import nl.arjanfrans.mario.actions.MoveableActions;
import nl.arjanfrans.mario.audio.Audio;
import nl.arjanfrans.mario.graphics.MarioAnimation;

// Library imports
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;

/**
 * @brief Represents the playable character in the game
 */
public class Mario extends Creature {
	protected MarioAnimation gfx = new MarioAnimation(); /**< Mario's animations */
	protected Rectangle rect = new Rectangle(); /**< Rectanlge surrounding Mario */
	private float jump_boost = 40f, width, height; /**< Mario's jump height */
	private boolean immume; /**< If Mario can take damage or not */
	private boolean controlsEnabled = true; /**< If the user can control Mario or not */
	private int points = 0;

	/** @brief Constructor method
     *  @details Method which initializes an instance of Mario
     *  @param world The world in which Mario will exist in
     *  @param positionX x coordinate of the position of Mario
     *  @param positionY y coordinate of the position of Mario
     *  @return An instance of Mario
	 */
	public Mario(World world, float positionX, float positionY) {
		super(world, positionX, positionY, 8f);
		immume = false;
		moving = true;
		level = 1; // Health
		updateSize();
	}

	/** 
	 *  @brief Updates Mario's size on GUI based on the dimensions of the state and level
	 */
	protected void updateSize() {
		this.setSize(gfx.getDimensions(state, level).x, gfx.getDimensions(state, level).y);
	}

	/** @brief Behaviour when Mario is hit by an enemy
     *  @details When Mario is hit by an enemy he will take damage, or be eliminated from the stage
	 */
	private void hitByEnemy() {
		if(!immume) level--;

		boolean dead = level < 1 && !immume;

		// Mario dies
		if(dead) {
			state = State.Dying;
			velocity.set(0, 0);
			// Mario's death animation
			this.setWidth(gfx.getDimensions(state, level).x);
			this.setHeight(gfx.getDimensions(state, level).y);
			this.addAction(Actions.sequence(Actions.moveBy(0, 1, 0.2f, Interpolation.linear),
					Actions.delay(0.6f),
					Actions.moveBy(0, -10, 0.6f, Interpolation.linear),
					Actions.delay(1.6f),
					MoveableActions.DieAction(this)));

			Audio.stopSong();
			Audio.playSong("lifelost", false); // Play death music
			this.resetMarioPoints();
		} else {
			// Mario takes damage hit
			if(!immume) Audio.powerDown.play();
			immume = true; // Mario cannot take another damage hit for a second
			// Mario damage animation
			this.addAction(Actions.sequence(Actions.parallel(Actions.alpha(0f, 2f, Interpolation.linear),
					Actions.fadeIn(0.4f, Interpolation.linear),
					Actions.fadeOut(0.4f, Interpolation.linear),
					Actions.fadeIn(0.4f, Interpolation.linear),
					Actions.fadeOut(0.4f, Interpolation.linear),
					Actions.fadeIn(0.4f, Interpolation.linear)),
					Actions.alpha(1f),
					MarioActions.stopImmumeAction(this)));
		}
	}

	/** @brief Behaviour when Mario interacts with the flag pole (finishes the game)
     *  @details Plays slide animation, win sound, and completes the game
     *  @param flag The flag object in which Mario has interacted with
     *  @param endX x coordinate where Mario will walk to after sliding down the pole
     *  @param endY y coordinate where Mario will walk to after sliding down the pole
	 */
	public void captureFlag(Flag flag, float endX, float endY) {
		Rectangle flagRect = flag.rect();
		state = State.FlagSlide;

		this.marioGetsPoints(5000);

		// TODO Flip mario sprite in sliding pose when at bottom
		this.addAction(Actions.sequence(
				Actions.delay(0.2f),
				Actions.parallel(
						Actions.moveTo(this.getX(), flagRect.y, 0.5f, Interpolation.linear),
						MarioActions.flagTakeDownAction(flag)),
				MarioActions.setStateAction(this, State.Walking),
				MarioActions.walkToAction(this, endX, endY),
				MarioActions.setStateAction(this, State.Pose),
				MarioActions.finishLevelAction())
		);

		Audio.stopSong();
		Audio.flag.play();
	}

	/** @brief Eliminates Mario when he is below the bounds of the stage
	 */
	protected void dieByFalling() {
		if(this.getY() < -3f) {
			state = State.Dying;
			velocity.set(0, 0);
			this.addAction(Actions.sequence(Actions.delay(3f),
					MoveableActions.DieAction(this)));
			Audio.stopSong();
			Audio.playSong("lifelost", false);
		}
	}

	/** @brief Controls Mario's movement after each discrete time step
     *  @param delta Change in time
	 */
	@Override
	public void act(float delta) {
		super.act(delta);
		// If Mario is in a controllable state
		if (state != State.Dying && state != State.FlagSlide && controlsEnabled) {
			// Up
			if ((Gdx.input.isKeyPressed(Keys.SPACE)) && grounded)
				jump();

			// Left
			if (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A))
				move(Direction.LEFT);

			// Right
			if (Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D))
				move(Direction.RIGHT);

			// Update display
			width = gfx.getFrameWidth(level, width);
			height = gfx.getFrameHeight(level, height);
			rect.set(this.getX(), this.getY(), width, height);

			// Check for collision
			collisionWithEnemy();
			collisionWithMushroom();

			if(state != State.Dying) applyPhysics(rect);
		}
	}

	/** @brief Draws Mario on the GUI
     *  @details Assists libGDX in drawing Mario in a specific batch
     *  @param batch The texture region where Mario is to be drawn
     *  @param parentAlpha Not used
	 */
	@Override
	public void draw(Batch batch, float parentAlpha) {
		TextureRegion frame = gfx.getAnimation(state, level).getKeyFrame(stateTime);
		updateSize();
		Color oldColor = batch.getColor();
		batch.setColor(this.getColor());

		if(state == State.Dying) {
			batch.draw(frame, getX(), getY(),
					getX()+this.getWidth()/2, getY() + this.getHeight()/2,
					this.getWidth(), this.getHeight(), getScaleX(), getScaleY(), getRotation());
		} else {
			if(facesRight) {
				batch.draw(frame, this.getX(), this.getY(),
						this.getX()+this.getWidth()/2, this.getY() + this.getHeight()/2,
						this.getWidth(), this.getHeight(), getScaleX(), getScaleY(), getRotation());
			}
			else {
				batch.draw(frame, this.getX() + this.getWidth(), this.getY(),
						this.getX()+this.getWidth()/2, this.getY() + this.getHeight()/2,
						-this.getWidth(), this.getHeight(), getScaleX(), getScaleY(), getRotation());
			}
		}
		batch.setColor(oldColor);
	}

	/** @brief Returns whether Mario is touching an object
     *  @param startX x coordinate of the position of the object Mario may be colliding with
     *  @param endY y coordinate of the position of the object Mario may be colliding with
     *  @return A boolean whether Mario is colliding with the object
	 */
	private boolean isTouched(float startX, float endX) {
		for (int i = 0; i < 2; i++) {
			float x = Gdx.input.getX() / (float) Gdx.graphics.getWidth();
			if (Gdx.input.isTouched(i) && (x >= startX && x <= endX)) {
				return true;
			}
		}
		return false;
	}

	/** @brief Behaviour for when Mario jumps
	  * @details Causes Mario's y velocity to increase by his jump velocity. Plays jump sound effect
	 */
	private void jump() {
		if(!grounded) return;

		velocity.y += jump_velocity; // Apply velocity in Y direction
		state = MovingActor.State.Jumping; // Update state
		grounded = false; // Not grounded
		Audio.jump.play(); // Play jumping sound
	}

	/** @brief Triggered when Mario collides with an object in the X direction
	 *  @details Stops Mario since Mario cannot move through object
	 */
	@Override
	protected void collisionXAction() {
		velocity.x = 0;
	}

	/** @brief Determines if Mario is colliding with any enemy
	 *  @details Checks all Goomba's in the stage for collision with Mario. If Mario is colliding
	 *  then Mario will take damage or be eliminated. If Mario is trampling a Goomba then the Goomba
	 *  will be eliminated
	 */
	protected void collisionWithEnemy() {
		Array<Goomba> goombas = world.getEnemies();
		// Is this creating a copy of Mario's rectangle?
		// Why can't we just use a get function to grab Mario's rectangle
		Rectangle marioRect = rectangle();
		marioRect.set(this.getX(), this.getY(), this.getWidth(), this.getHeight());

		for(Goomba goomba : goombas) {
			Rectangle gRect = goomba.rectangle();

			if(gRect.overlaps(marioRect) && goomba.state != State.Dying) {
				boolean marioTrampledGoomba = velocity.y < 0 && this.getY() > goomba.getY();

				if (marioTrampledGoomba) {
					this.marioGetsPoints(100);
					goomba.deadByTrample(); // Goomba dies
					Audio.stomp.play(); // Play stomp sound
					velocity.y += jump_boost; // Mario gains jump boost
					grounded = false;

				} else {
					hitByEnemy(); // Mario takes damage
				}
			}
		}
	}

	/** @brief Applies gravity and physics to Mario
	 *  @details Applies gravity if Mario is falling. Determines if Mario is colliding with an
	 *  actor or object within the stage
     *  @param rect A rectangle object that is being checked for collision against Mario
	 */
	@Override
	protected void applyPhysics(Rectangle rect) {
		float deltaTime = Gdx.graphics.getDeltaTime();
		if (deltaTime == 0) return;

		stateTime += deltaTime;
		velocity.add(0, World.GRAVITY * deltaTime); // apply gravity if we are falling

		// Round velocity to 0 if small enough
		if (Math.abs(velocity.x) < 1) {
			velocity.x = 0;
			if (grounded && controlsEnabled) state = State.Standing;
		}

		// Scale by deltaTime
		velocity.scl(deltaTime);

		// Collision in the X direction
		if(collisionX(rect)) collisionXAction();
		rect.x = this.getX();

		// Collision in Y direction
		collisionY(rect);

		// Move
		this.setPosition(this.getX() + velocity.x, this.getY() +velocity.y);
		// Unscale velocity
		velocity.scl(1 / deltaTime);

		// Apply damping to velocity
		velocity.x *= damping;

		// Check if falling out of map
		dieByFalling();
	}

	/** @brief Causes Mario to transform into Big Mario (power-up)
	 *  @details Changes Mario's model to be large, have an extra health point, and plays an 
	 *  audio cue
     *  @param mushroom The mushroom Mario has consumed
	 */
	private void big_mario(Mushroom mushroom) {
		level = 2; // Give extra health point
		World.objectsToRemove.add(mushroom); // Remove mushroom
		Audio.powerUp.play(); // Play power up sound
	}

	/** @brief Determines if Mario is consuming a mushroom
	 *  @details Checks all mushrooms in the stage and deteremines if Mario is consuming (colliding)
	 *  with any of them
	 */
	protected void collisionWithMushroom() {
		Array<Mushroom> mushrooms = world.getMushrooms();
		Rectangle marioRect = rectangle();
		marioRect.set(this.getX(), this.getY(), this.getWidth(), this.getHeight());

		// Check for collision with a mushroom by their rectangles
		for(Mushroom mushroom : mushrooms) {
			Rectangle mRect = mushroom.rectangle();
			if(mushroom.isVisible() && mRect.overlaps(marioRect) && mushroom.state != State.Dying) {
				if(level == 1) {
					big_mario(mushroom);
					this.marioGetsPoints(1000);
				};
			}
		}
	}

	/** @brief Returns Mario's animations
     *  @return An animation object representing Mario's animations
	 */
	@Override
	public Animation getAnimation() {
		return gfx.getAnimation(state, level);
	}

	/** @brief Removes Mario from the GUI
	 */
	public void dispose() {
		gfx.dispose();
	}

	/** @brief Sets Mario's immune status
     *  @param immume A boolean representing if Mario is immune or not
	 */
	public void setImmume(boolean immume) {
		this.immume = immume;
	}

	/** @brief Sets Mario's velocity and direction to be in the direction of the specified movement
     *  @param dir The direction in which Mario is desired to move in
	 */
	@Override
	public void move(Direction dir) {
		if(state != State.Dying && moving) {
			if(dir == Direction.LEFT) {
				velocity.x = -max_velocity;
				facesRight = false;
			}
			else {
				velocity.x = max_velocity;
				facesRight = true;
			}
			direction = dir;
			if (grounded) state = MovingActor.State.Walking;
		}
	}

	/** @brief Returns whether the user has control of Mario
     *  @return A boolean whether the user has control of Mario or not
	 */
	public boolean isControlsEnabled() {
		return controlsEnabled;
	}

	/** @brief Sets whether the user has control of Mario or not
     *  @param controlsEnabled A boolean representing whether the user has control of Mario or not
	 */
	public void setControlsEnabled(boolean controlsEnabled) {
		this.controlsEnabled = controlsEnabled;
	}

	private void marioGetsPoints(int points) {
		this.increasePoints(points);
		// TODO Play coin audio
		// Audio.points.play();
//		System.out.println(this.points);
	}

	private void increasePoints(int amount) {
		if (amount < 0) return;
		this.points += amount;
	}

	private void resetMarioPoints() {
		this.points = 0;
	}

	public int getPoints() {
		return this.points;
	}
}
