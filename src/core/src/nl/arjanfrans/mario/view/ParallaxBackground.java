package nl.arjanfrans.mario.view;

import java.util.Iterator;

import nl.arjanfrans.mario.debug.D;
import nl.arjanfrans.mario.model.World;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapObject;

/** @brief The class meant to create a parallax background.
 */
public class ParallaxBackground {

	// the layers of this background
	private ParallaxLayer[] layers;
	//the camera
	private Camera camera;
	//sprite batch
	private SpriteBatch batch;
	private World world;

	/** @brief Constructor method for ParallaxBackground
	 * @param world - the world that the ParallaxBackground is displaying
	 * @param pLayers - an array of ParallaxLayers
	 * @param pCamera - a Camera object that will move back and forth on the background
	 * @param pBatch - a SpriteBatch object
	 * @return an instance of ParallaxBackground
	 */
	public ParallaxBackground(World world, ParallaxLayer[] pLayers, Camera pCamera,
			SpriteBatch pBatch) {
		this.world = world;
		layers = pLayers;
		camera = pCamera;
		batch = pBatch;
	}
	
	/**
	 * @brief A method meant to render the parallax background.
	 */
	public void render() {
		batch.begin();
		for (ParallaxLayer layer : layers) {
			drawLayer(layer, batch);
		}
		batch.end();
	}

	/**@brief A method meant to draw the layers of the Parallax background.
	 * @param layer - a ParallaxLayer
	 * @param batch - a SpriteBatch object
	 */
	private void drawLayer(ParallaxLayer layer, SpriteBatch batch) {
		Iterator<MapObject> it = layer.getLayerObjects().iterator();
		while(it.hasNext()) {
			MapObject obj = it.next();
			float x = (float) ((Float) obj.getProperties().get("x") * 1/16f);
			float y = (float) ((Float) obj.getProperties().get("y") * 1/16f);
			
			String file = "data/backgrounds/" + (String) obj.getProperties().get("src");
			Texture texture = layer.getLayerTextures().get(file);
			batch.draw(layer.getLayerTextures().get(file), x + layer.positionX,
					y + layer.positionY, texture.getWidth() * 1/16f, texture.getHeight() * 1/16f);
		}
	}

	/** @brief A method meant to move the parallax background on the x-axis
	 * @param pDelta - a float constant indicating how much to move by on the x - axis
	 */
	public void moveX(float pDelta) {
		for (ParallaxLayer layer : layers) {
			layer.moveX(pDelta);
		}
	}

	/** @brief A method meant to move the parallax background on the y-axis
	 * @param pDelta - a float constant indicating how much to move by on the y - axis
	 */
	public void moveY(float pDelta) {
		for (ParallaxLayer layer : layers) {
			layer.moveY(pDelta);
		}
	}

	/** @brief A method meant to dispose of all layers of the ParallaxBackground.
	 */
	public void dispose() {
		for(int i = 0; i < layers.length; i++) {
			layers[i].dispose();
		}
	}
}