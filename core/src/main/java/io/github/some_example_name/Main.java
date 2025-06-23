package io.github.some_example_name;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.some_example_name.screens.GameOverScreen;
import io.github.some_example_name.screens.GameScreen;
import io.github.some_example_name.screens.HomeScreen;
import io.github.some_example_name.screens.VictoryScreen;

public class Main extends Game {
    public SpriteBatch batch;
    public GameScreen gameScreen;

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new HomeScreen(this)); // mulai dengan HomeScreen
    }
//    @Override
////    public void create() {
////        setScreen(new VictoryScreen(this));
//        //setScreen(new GameOverScreen(this));
//    }

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
