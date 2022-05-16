package nl.arjanfrans.mario.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * @brief This is a class that implements the audio in the game.
 */
public class Audio {

	private static Music overworld = Gdx.audio.newMusic(Gdx.files.internal("data/audio/soundtracks/overworld_theme.ogg"));
	private static Music undergrounds = Gdx.audio.newMusic(Gdx.files.internal("data/audio/soundtracks/underworld_theme.ogg"));
	private static Music lifelost = Gdx.audio.newMusic(Gdx.files.internal("data/audio/soundtracks/life_lost.ogg"));
	private static Music finish = Gdx.audio.newMusic(Gdx.files.internal("data/audio/soundtracks/course_clear.ogg"));
	private static Music song;
	
	public static Sound jump = Gdx.audio.newSound(Gdx.files.internal("data/audio/sfx/sounds/jump-small.wav"));
	public static Sound stomp = Gdx.audio.newSound(Gdx.files.internal("data/audio/sfx/sounds/stomp.wav"));
	public static Sound bump = Gdx.audio.newSound(Gdx.files.internal("data/audio/sfx/sounds/bump.wav"));
	public static Sound flag = Gdx.audio.newSound(Gdx.files.internal("data/audio/sfx/sounds/flagpole.wav"));
	public static Sound powerDown = Gdx.audio.newSound(Gdx.files.internal("data/audio/sfx/sounds/pipeandpowerdown.wav"));
	public static Sound powerUp = Gdx.audio.newSound(Gdx.files.internal("data/audio/sfx/sounds/powerup.wav"));
	public static Sound powerUpAppears = Gdx.audio.newSound(Gdx.files.internal("data/audio/sfx/sounds/powerup_appears.wav"));
	public static Sound coin = Gdx.audio.newSound(Gdx.files.internal("data/audio/sfx/sounds/coin.wav"));

	public static String currentSong = "";

	/** @brief A method meant to play a song on loop depending on the state of the game.
	 * 	@param name - the name of the song (based on the state of the game).
	 * 	@param looping  - a boolean meant to indicate whether the song is looping or not.
	 */
	public static void playSong(String name, boolean looping) {
		if(name.equals("overworld")) {
			currentSong = "overworld";
			song = overworld;
		}
		else if(name.equals("undergrounds")) {
			currentSong = "undergrounds";
			song = undergrounds;
		}
		else if(name.equals("lifelost")) {
			currentSong = "lifelost";
			song = lifelost;
		}
		else if(name.equals("finish")) {
			currentSong = "finish";
			song = finish;
		}
		song.setLooping(looping);
		song.play();
	}

	/** @brief A method meant to play a song on loop depending on the state of the game.
	 * @return a Music object, the current value of the global song variable.
	 */
	public static Music getSong() {
		return song;
	}

	/** @brief A method meant to stop the current song instance variable from playing.
	 */
	public static void stopSong() {
		if(song != null) {
			song.stop();
		}
	}

	/** @brief A method meant to dispose of all the music files.
	 */
	public static void dispose() {
		overworld.dispose();
		undergrounds.dispose();
		song.dispose();
		jump.dispose();
		stomp.dispose();
		coin.dispose();
		powerUp.dispose();
		powerUpAppears.dispose();
		powerDown.dispose();
	}

}
