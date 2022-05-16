package nl.arjanfrans.mario.view;

import java.util.Iterator;

import nl.arjanfrans.mario.model.World;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

/** @brief The class meant to retrieve a layer from a ParallaxBackground.
 */
public class ParallaxLayer {

	/**
	 * how much shall this layer (in percent) be moved if the whole background is moved
	 * 0.5f is half as fast as the speed
	 * 2.0f is twice the speed
	 */
	float ratioX, ratioY;

	// current position
	float positionX, positionY;

	private World world;
	private Array<MapObject> layer_objects;
	private ArrayMap<String, Texture> layer_textures;
	private String layer_name;
	

	/** @brief Constructor method for ParallaxLayer.
	 * @param world - The world that the ParallaxLayer exists in
	 * @param layer_name - a string representing the name of the layer
	 * @param pRatioX - a float representing how much the layer will move in the x direction if the background is moved.
	 * @param pRatioY - a float representing how much the layer will move in the y direction if the background is moved.
	 */
	public ParallaxLayer(World world, String layer_name, float pRatioX, float pRatioY) {
		this.world = world;
		this.layer_name = layer_name;
		layer_objects = new Array<MapObject>();
		layer_textures = new ArrayMap<String, Texture>();
		ratioX = pRatioX;
		ratioY = pRatioY;
		loadObjects();
	}
	
	/** @brief A method meant to load the objects from the tmx file, convert them into textures and put them on the layer.
	 */
	private void loadObjects() {
		//TODO Use a spritesheet for the background objects
		Map map = world.getMap();
		Iterator<MapObject> it = map.getLayers().get(layer_name).getObjects().iterator();
		while(it.hasNext()) {
			MapObject obj = it.next();
			String file = "data/backgrounds/" + (String) obj.getProperties().get("src");
			layer_objects.add(obj);
			if(!layer_textures.containsKey(file)) {
				Texture texture = new Texture(file);
				texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
				layer_textures.put(file, texture);
			}
		}
	}
	
	public ArrayMap<String, Texture> getLayerTextures() {
		return layer_textures;
	}
	
	public Array<MapObject> getLayerObjects() {
		return layer_objects;
	}

	/**@brief A method meant to move this layer in the x direction, based on a constant value.
	 * @param pDelta - a float representing the shift in the layer.
	 */
	protected void moveX(float pDelta) {
		positionX += pDelta * ratioX;
	}

	/**@brief A method meant to move this layer in the y direction, based on a constant value.
	 * @param pDelta - a float representing the shift in the layer.
	 */
	protected void moveY(float pDelta) {
		positionY += pDelta * ratioY;
	}

	/** @brief A method meant to dispose of all layer textures of the ParallaxLayer.
	 */
	public void dispose() {
		for(int i = 0; i < layer_textures.size; i++) {
			layer_textures.getValueAt(i).dispose();
		}
	}
}