package nl.arjanfrans.mario.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

/**
 * @brief Represents a moving actor in the game.
 */
public abstract class MovingActor extends Actor {

	public static enum State {
		Standing,
		Walking,
		Jumping,
		Dying,
		Dead,
		FlagSlide,
		NoControl,
		Pose;
	}

	public static enum Direction {
		LEFT,
		RIGHT;
	}

	protected float max_velocity;
	protected float jump_velocity = 40f;
	protected float damping = 0.87f;
	protected Vector2 position;
	protected Vector2 velocity;
	protected World world;
	protected boolean dead;
	protected boolean moving;
	protected State state = State.Standing;
	protected float stateTime = 0;
	protected int level;
	protected boolean facesRight = true;
	protected Direction direction;
	protected boolean grounded = false;

	protected Pool<Rectangle> rectPool = new Pool<Rectangle>() {
		@Override
		protected Rectangle newObject() {
			return new Rectangle();
		}
	};

	protected Rectangle rectangle() {
		return new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}

	/** @brief Constructor method
     *  @details Method which initializes an instance of a MovingActor
     *  @param world The world in which MovingActor will exist in
     *  @param x is the x coordinate of the position of the MovingActor
     *  @param y is the y coordinate of the position of the MovingActor
     *  @return An instance of MovingActor
	 */
	public MovingActor(World world, float x, float y, float max_velocity) {
		this.world = world;
		this.setPosition(x, y);
		this.max_velocity = max_velocity;
		velocity = new Vector2(0, 0);
		dead = false;
		moving = false;
		this.setTouchable(Touchable.disabled);
	}

	/** @brief This method applies the laws of motion to a rectangle
	 *  @details In the game a MovingActor, can be physically represented by a rectangle, so when
	 *  a MovingActor moves the laws of physics should apply.
	 *  @param rect - The rectangle that the MovingActor represents.
	 */
	protected void applyPhysics(Rectangle rect) {
		float deltaTime = Gdx.graphics.getDeltaTime();
		if (deltaTime == 0) return;

		stateTime += deltaTime;

		velocity.add(0, World.GRAVITY * deltaTime);

		if (Math.abs(velocity.x) < 1) {
			velocity.x = 0;
			if (grounded) state = State.Standing;
		}

		// Scale velocity by delta time
		velocity.scl(deltaTime);

		if(collisionX(rect)) collisionXAction();

		rect.x = this.getX();

		collisionY(rect);

		// Update x and y positions
		this.setPosition(this.getX() + velocity.x, this.getY() + velocity.y);

		// Un-scale by delta time
		velocity.scl(1 / deltaTime);

		// Apply damping so character eventually stops moving
		velocity.x *= damping;

		// Checks if the actor is dead from falling
		dieByFalling();
	}

	/** @brief This method checks if the rectangle collides with anything in the x direction.
	 *  @details In the game a MovingActor, can be physically represented by a rectangle, so when
	 *  a MovingActor meets an immovable object in x direction, the method should return true.
	 *  @param rect - The rectangle that the MovingActor represents.
	 *  @return a boolean
	 */
	protected boolean collisionX(Rectangle rect) {
		// int[] bounds = checkTiles(true);
		// Array<Rectangle> tiles = world.getTiles(bounds[0], bounds[1], bounds[2], bounds[3]);

		Array<Rectangle> tiles = getTiles(true);
		// What is this for?
		rect.x += velocity.x;

		// Checks if actor is colliding with a tile
		for (Rectangle tile : tiles) {
			if (rect.overlaps(tile)) {
				return true;
			}
		}

		// If actor is colliding with a static actor and is not destroyed
		for(StaticActor a : world.getStaticActors()) {
			if(rect.overlaps(a.rectangle()) && !a.isDestroyed()) {
				return true;
			}
		}

		return false;
	}

	/** @brief This method checks for tiles in the x and y directions.
	 *  @param checkX - A boolean value indicating in which direction we ar checking for tiles.
	 *  @return an array of integers
	 */
	protected int[] checkTiles(boolean checkX) {
		int startX, startY, endX, endY;

		if(checkX) {
			if (velocity.x > 0)
				startX = endX = (int) (this.getX() + this.getWidth() + velocity.x);
			else
				startX = endX = (int) (this.getX() + velocity.x);

			startY = (int) (this.getY());
			endY = (int) (this.getY() + this.getHeight());
		} else {
			if (velocity.y > 0)
				startY = endY = (int) (this.getY() + this.getHeight() + velocity.y);
			else
				startY = endY = (int) (this.getY() + velocity.y);

			startX = (int) (this.getX());
			endX = (int) (this.getX() + this.getWidth());
		}

		return new int[]{startX, startY, endX, endY};
	}

	/** @brief This method checks if the rectangle collides with anything in the y direction.
	 *  @details In the game a MovingActor, can be physically represented by a rectangle, so when
	 *  a MovingActor meets an immovable object in y direction, the method should return true.
	 *  @param rect - The rectangle that the MovingActor represents.
	 *  @return a boolean
	 */
	protected void collisionY(Rectangle rect) {
		// int[] bounds = checkTiles(false);
		// world.getTiles(bounds[0], bounds[1], bounds[2], bounds[3]);
		Array<Rectangle> tiles = getTiles(false);

		rect.y += velocity.y;

		// Can not jump while falling
		if(velocity.y < 0 ) grounded = false;

		for (Rectangle tile : tiles) {
			if (rect.overlaps(tile)) {
				if (velocity.y > 0) this.setY(tile.y - this.getHeight());
				else {
					this.setY(tile.y + tile.height);
					hitGround();
				}

				velocity.y = 0;

				// Break look once overlapped
				break;
			}
		}

		for (StaticActor a : world.getStaticActors()) {
			if(rect.overlaps(a.rectangle()) && !a.isDestroyed()) {
				if (velocity.y > 0) {
					a.hit(level);
					this.setY(a.getOriginY() - this.getHeight());
				} else {
					this.setY(a.getY() + a.getHeight());
					hitGround();
				}
				velocity.y = 0;
				break;
			}
		}
		rectPool.free(rect);
	}

	/** @brief This method moves the MovingActor in specific directions.
	 *  @details When on the ground a MovingActor can be move in two directions, either left or right
	 *  @param dir - A direction listed in the enumeration class called Direction.
	 */
	public void move(Direction dir) {
		if(state != State.Dying && moving) {
			if(dir == Direction.LEFT) {
				velocity.x = -max_velocity;
				facesRight = false;
			}
			else { // Moving right
				velocity.x = max_velocity;
				facesRight = true;
			}

			// Update direction
			direction = dir;

			// If grounded, then update state
			if (grounded) state = MovingActor.State.Walking;
		}
	}

	/** @brief This method gets the tiles in the x and y directions.
	 *  @param isX - A boolean value indicating the direction in which the tile are being retrieved.
	 *  @return an array of Rectangle objects
	 */
	protected Array<Rectangle> getTiles(boolean isX) {
		int[] bounds = checkTiles(isX);
		return world.getTiles(bounds[0], bounds[1], bounds[2], bounds[3]);
	}
	/** @brief This method sets the grounded boolean instance variable to true.
	 */
	protected void hitGround() {
		grounded = true;
	}

	/** @brief This method gets the value of the max_velocity instance variable.
	 *  @return a float representing the max velocity.
	 */
	public float getMax_velocity() {
		return max_velocity;
	}

	/** @brief This method gets the value of the jump_velocity instance variable.
	 *  @return a float representing the jump velocity.
	 */
	public float getJump_velocity() {
		return jump_velocity;
	}

	/** @brief This method gets the daming constant in the damping instance variable.
	 *  @return a float representing the damping constant.
	 */
	public float getDamping() {
		return damping;
	}

	/** @brief This method gets the stateTime constant in the stateTime instance variable.
	 *  @return a float representing the state time.
	 */
	public float getStateTime() {
		return stateTime;
	}

	/** @brief This method gets the boolean value of the facesRight instance variable.
	 *  @return a boolean representing which way the MovingActor faces.
	 */
	public boolean isFacesRight() {
		return facesRight;
	}

	/** @brief This method gets the Vector2 object in the velocity instance variable.
	 *  @return a Vector2 object representing the velocity of a MovingActor.
	 */
	public Vector2 getVelocity() {
		return velocity;
	}

	/** @brief This method gets the boolean value of the moving instance variable.
	 *  @return a boolean representing whether the MovingActor is in motion.
	 */
	public boolean isMoving() {
		return moving;
	}

	/** @brief This method sets the boolean value of the moving instance variable.
	 * @param moving - a boolean indicating whether the MovignActor is in motion.
	 */
	public void setMoving(boolean moving) {
		this.moving = moving;
	}

	/** @brief This method gets the state of the MovingActor.
	 *  @return a member of the State enumernation class representing what the MovingActor is doing.
	 */
	public State getState() {
		return state;
	}

	/** @brief This method sets the state of the MovingActor.
	 *  @param a member of the State enumernation class representing what the MovingActor is doing.
	 */
	public void setState(State state) {
		this.state = state;
	}

	/** @brief This method sets the boolean value of the dead instance variable.
	 *  @param a boolean representing whether the MovingActor is dead or not.
	 */
	public void setDead(boolean dead) {
		this.dead = dead;
		world.removeActor(this);
	}

	/** @brief This method gets the boolean value of the dead instance variable.
	 *  @return a boolean representing whether the MovingActor is dead or not.
	 */
	public boolean isDead() {
		return dead;
	}

	/** @brief This method is an abstract method handled by the inherited class.
	 */
	protected abstract void dieByFalling();

	/** @brief This method is an abstract method handled by the inherited class.
	 */
	protected abstract void collisionXAction();

}
