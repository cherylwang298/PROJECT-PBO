//package io.github.some_example_name.screens;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Screen;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.scenes.scene2d.ui.Image;
//import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
//import com.badlogic.gdx.scenes.scene2d.ui.Table;
//import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
//import com.badlogic.gdx.utils.viewport.ScreenViewport;
//import com.badlogic.gdx.scenes.scene2d.InputEvent;
//import com.badlogic.gdx.scenes.scene2d.InputListener;
//import com.badlogic.gdx.scenes.scene2d.ui.Cell;
//import com.badlogic.gdx.utils.Scaling;
//import io.github.some_example_name.Main;
//
//public class GameScreen implements Screen {
//
//    private final Main game;
//    private Texture gameBackground;
//    private Texture pauseTexture;
//    private Stage stage;
//
//    private ImageButton pauseButton;
//    private Cell<ImageButton> pauseCell;
//    private Table table;
//
//    public GameScreen(Main game) {
//        this.game = game;
//
//        // Load textures
//        gameBackground = new Texture("biggerOne.jpg");
//        pauseTexture = new Texture("pauseButton.png");
//
//        // Set up stage
//        stage = new Stage(new ScreenViewport());
//        Gdx.input.setInputProcessor(stage);
//
//        // Background as Image actor
//        Image bg = new Image(new TextureRegionDrawable(gameBackground));
//        bg.setFillParent(true);
//        bg.setScaling(Scaling.fill); // or Scaling.stretch / fit
//        stage.addActor(bg); // Tambahkan background dulu supaya di bawah
//
//        // Pause Button
//        pauseButton = new ImageButton(new TextureRegionDrawable(pauseTexture));
//        pauseButton.addListener(new InputListener() {
//            @Override
//            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                return true;
//            }
//
//            @Override
//            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//                System.out.println("Pause clicked!");
//                game.setScreen(new PauseScreen(game, this)); // Bisa diganti ke pause screen
//            }
//        });
//
//        // Table layout for button
//        table = new Table();
//        table.setFillParent(true);
//        table.top().left(); // Posisi tombol kiri atas
//        stage.addActor(table); // Tambahkan setelah background supaya muncul di atas
//
//        // Responsive initial size
//        float buttonSize = Gdx.graphics.getWidth() * 0.08f;
//        pauseCell = table.add(pauseButton).pad(10f).size(buttonSize, buttonSize);
//    }
//
//    @Override
//    public void show() {}
//
//    @Override
//    public void render(float delta) {
//        stage.act(delta);
//        stage.draw(); // Tidak perlu batch manual karena semua pakai Stage
//    }
//
//    @Override
//    public void resize(int width, int height) {
//        stage.getViewport().update(width, height, true);
//
//        float buttonSize = width * 0.08f;
//        pauseCell.size(buttonSize, buttonSize);
//        table.invalidateHierarchy(); // refresh layout
//    }
//
//    @Override public void pause() {}
//    @Override public void resume() {}
//    @Override public void hide() {}
//
//    @Override
//    public void dispose() {
//        stage.dispose();
//        gameBackground.dispose();
//        pauseTexture.dispose();
//    }
//}


package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.utils.Scaling;
import io.github.some_example_name.Main;

public class GameScreen implements Screen {

    private final Main game;
    private Texture gameBackground;
    private Texture pauseTexture;
    private Stage stage;

    private ImageButton pauseButton;
    private Cell<ImageButton> pauseCell;
    private Table table;

    public GameScreen(Main game) {
        this.game = game;

        // Load textures
        gameBackground = new Texture("biggerOne.jpg");
        pauseTexture = new Texture("pauseButton.png");

        // Set up stage
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Background as Image actor
        Image bg = new Image(new TextureRegionDrawable(gameBackground));
        bg.setFillParent(true);
        bg.setScaling(Scaling.fill); // or Scaling.stretch / fit
        stage.addActor(bg); // Tambahkan background dulu supaya di bawah

        // Pause Button
        pauseButton = new ImageButton(new TextureRegionDrawable(pauseTexture));
        pauseButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Pause clicked!");
                game.setScreen(new PauseScreen(game, GameScreen.this)); // ✅ Perbaikan di sini

            }
        });

        // Table layout for button
        table = new Table();
        table.setFillParent(true);
        table.top().left(); // Posisi tombol kiri atas
        stage.addActor(table); // Tambahkan setelah background supaya muncul di atas

        // Responsive initial size
        float buttonSize = Gdx.graphics.getWidth() * 0.08f;
        pauseCell = table.add(pauseButton).pad(10f).size(buttonSize, buttonSize);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw(); // Tidak perlu batch manual karena semua pakai Stage
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

        float buttonSize = width * 0.08f;
        pauseCell.size(buttonSize, buttonSize);
        table.invalidateHierarchy(); // refresh layout
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        gameBackground.dispose();
        pauseTexture.dispose();
    }
}
