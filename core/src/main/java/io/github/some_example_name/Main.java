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
    public GameScreen gameScreen;
    private Music bgm;

    @Override
    public void create() {
        batch = new SpriteBatch();

        bgm = Gdx.audio.newMusic(Gdx.files.internal("bgmTrial.mp3"));
        bgm.setLooping(true);
        bgm.setVolume(0.3f); // atur volume sesuai selera
        bgm.play();

        setScreen(new HomeScreen(this)); // mulai dari HomeScreen


    }

    @Override
    public void render() {
        super.render(); // render screen aktif
    }

    @Override
    public void dispose() {
        batch.dispose();
        getScreen().dispose();
        if (bgm != null) bgm.dispose();
    }
}
