package io.github.some_example_name;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.some_example_name.screens.HomeScreen;

public class Main extends Game {
    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new HomeScreen(this)); // mulai dengan HomeScreen
    }

    @Override
    public void render() {
        super.render(); // render screen aktif
    }

    @Override
    public void dispose() {
        batch.dispose();
        getScreen().dispose();
    }
}
