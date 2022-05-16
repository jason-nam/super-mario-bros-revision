package nl.arjanfrans.mario.graphics;

import nl.arjanfrans.mario.debug.D;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Array;

/**
 * @brief This is a class meant to deal with the tiles that make up the graphics of the game.
 */
public class Tiles {
	private static TextureAtlas atlas = new TextureAtlas("data/tiles/mario_tileset.atlas");

	/** @brief A method meant to retrieve an animation tile based on the name of the animation.
	 * 	@param name - the name of the animation found in the mario_tileset.atlas file.
	 *  @return an array of StaticTiledMapTile, representing the frames of the Animation.
	 */
	public static Array<StaticTiledMapTile> getAnimatedTile(String name) {
		Array<AtlasRegion> regions = atlas.findRegions(name);
		Array<StaticTiledMapTile> frames = new Array<StaticTiledMapTile>();
		for(int i = 0; i < regions.size; i++) {
			frames.add(new StaticTiledMapTile(regions.get(i)));
		}
		return frames;
	}

	/** @brief A method meant to retrieve an animation based on the name of the animation.
	 * 	@param name - the name of the animation found in the mario_tileset.atlas file.
	 * 	@param speed - a float representing how fast the frames are to be moved through.
	 *  @return an Animation object, representing the Animation named.
	 */
	public static Animation getAnimation(float speed, String name) {
		Array<AtlasRegion> regions = atlas.findRegions(name);
		TextureRegion[] frames = new TextureRegion[regions.size];
		for(int i = 0; i < regions.size; i++) {
			frames[i] = regions.get(i);
		}
		return new Animation(speed, frames);
	}

	/** @brief A method meant to find a tile in a tile sheet based on the named indicated for it in the atlas file.
	 * 	@param name - the name of the tile found in the mario_tileset.atlas file.
	 *  @return a TextureRegion object which represents the tile's location on a tile sheet
	 */
	public static TextureRegion getTile(String name) {
		AtlasRegion ar = atlas.findRegion(name);
		TextureRegion[] tr = ar.split(ar.getRegionWidth(), ar.getRegionHeight())[0];
		return tr[0];
	}

	/** @brief A method meant to find a tile (split into to sets of eight pixels) in a tile sheet based on the named
	 * indicated for it in the atlas file.
	 * 	@param name - the name of the tile found in the mario_tileset.atlas file.
	 *  @return a TextureRegion object which represents the tile's location on a tile sheet
	 */
	public static TextureRegion getTile8(String name) {
		AtlasRegion ar = atlas.findRegion(name);
		TextureRegion[] tr = ar.split(8, 8)[0];
		return tr[0];
	}
	/** @brief A method meant to dipose of the atlas file.
 	*/
	public void dispose() {
		atlas.dispose();
	}

}
