package nl.arjanfrans.mario;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import nl.arjanfrans.mario.model.World;
import nl.arjanfrans.mario.view.MainMenu;

/** @brief The class meant to render the game.
 */
public class MarioGame extends Game {
	private World world;
	private Screen mainMenu;
	public static final String VERSION = "0.01";
	public static final boolean DEBUG = true;
	public static final int FPS = 60;

	/** @brief The method creates a new World object.
	 */
	@Override
	public void create()
	{
		this.mainMenu = new MainMenu(this);
		this.setScreen(mainMenu);
		world = new World();
	}

	/** @brief The method disposes of a World object.
	 */
	@Override
	public void dispose() {
		world.dispose();
	}

	/** @brief The method resizes the game, as the window is resized.
	 */
	@Override
	public void resize(int width, int height)
	{
		if (!this.getScreen().equals(this.mainMenu)) {
			world.getRenderer().resize(width, height);
		}
	}

	@Override
	public void pause()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void resume()
	{
		// TODO Auto-generated method stub
	}

	/** @brief The method makes any necessary updates to the game, and calls when the application should render itself.
	 */
	@Override
	public void render() {
		super.render();
		if (!this.getScreen().equals(this.mainMenu)) {
			world.update();
		}
	}
}
