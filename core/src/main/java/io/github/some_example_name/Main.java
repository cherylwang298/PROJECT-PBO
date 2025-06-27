package io.github.some_example_name;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.some_example_name.screens.GameOverScreen;
import io.github.some_example_name.screens.GameScreen;
import io.github.some_example_name.screens.HomeScreen;
import io.github.some_example_name.screens.VictoryScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class Main extends Game {
    public SpriteBatch batch;
    public GameScreen gameScreen; // Keep this if you need direct access, but often GameRoundManager is enough
    private Music bgm;

    @Override
    public void create() {
        batch = new SpriteBatch();

        bgm = Gdx.audio.newMusic(Gdx.files.internal("bgmTrial.mp3")); // Assuming "bgmTrial.mp3" is in assets root
        bgm.setLooping(true);
        bgm.setVolume(0.3f); // adjust volume as needed
        bgm.play();

        setScreen(new HomeScreen(this)); // start from HomeScreen
    }

    @Override
    public void render() {
        super.render(); // render active screen
    }

    /**
     * Pauses the main background music.
     */
    public void pauseBGM() {
        if (bgm != null && bgm.isPlaying()) {
            bgm.pause();
            Gdx.app.log("Main", "BGM paused.");
        }
    }

    /**
     * Resumes the main background music.
     */
    public void resumeBGM() {
        if (bgm != null && !bgm.isPlaying()) { // Only play if not already playing
            bgm.play();
            Gdx.app.log("Main", "BGM resumed.");
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        // It's generally better to let the current screen dispose itself.
        // If you set a new screen, the old one's hide() and dispose() should be called.
        // getScreen().dispose(); // Remove this line. Game.setScreen handles disposing previous screen.
        if (bgm != null) bgm.dispose();
        Gdx.app.log("Main", "Main class resources disposed.");
    }
}
