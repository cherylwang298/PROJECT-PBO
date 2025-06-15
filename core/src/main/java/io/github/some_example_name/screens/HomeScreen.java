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
import com.badlogic.gdx.utils.Scaling;
import io.github.some_example_name.Main;

public class HomeScreen implements Screen {

    private final Main game;
    private Stage stage;
    private Texture bgTexture;
    private Texture playTexture;

    public HomeScreen(Main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Background image
        bgTexture = new Texture("colosseumBG.jpeg");
        Image bg = new Image(new TextureRegionDrawable(bgTexture));
        bg.setFillParent(true);
        stage.addActor(bg);

        // Play button image
        playTexture = new Texture("playButtonP.png");
        ImageButton playButton = new ImageButton(new TextureRegionDrawable(playTexture));

        // Optional: maintain image scaling (biar gak gepeng saat resize)
        playButton.getImage().setScaling(Scaling.fit);

        // Play button listener
        playButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Play clicked!");
                // game.setScreen(new GameScreen(game)); <-- contoh jika kamu lanjut ke game
            }
        });

        // === Table Layout ===
        Table table = new Table();
        table.setFillParent(true);
        table.bottom(); // letakkan di bawah
        stage.addActor(table);

        float screenWidth = Gdx.graphics.getWidth();
        float buttonWidth = screenWidth * 0.55f; // responsive: 40% dari lebar layar
        float buttonHeight = buttonWidth * 0.35f; // sesuaikan rasio PNG-mu

        table.add(playButton).size(buttonWidth, buttonHeight).padBottom(Gdx.graphics.getHeight() * 0.1f);
    }

    @Override public void show() {}
    @Override public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }
    @Override public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        stage.dispose();
        bgTexture.dispose();
        playTexture.dispose();
    }
}
