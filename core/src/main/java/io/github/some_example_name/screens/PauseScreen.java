package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
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

public class PauseScreen implements Screen {

    private final Main game;
    private final Screen previousScreen; // supaya bisa balik ke GameScreen

    private Stage stage;
    private Texture resumeTexture;
    private Texture homeTexture;
    private Texture overlayTexture;

    public PauseScreen(Main game, Screen previousScreen) {
        this.game = game;
        this.previousScreen = previousScreen;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Buat background transparan hitam (50% opacity)
        Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 0.5f);
        pixmap.fill();
        overlayTexture = new Texture(pixmap);
        pixmap.dispose();

        Image overlay = new Image(new TextureRegionDrawable(overlayTexture));
        overlay.setFillParent(true);
        stage.addActor(overlay);

        // Tombol
        resumeTexture = new Texture("resumeButton.png"); // Siapkan gambar
        homeTexture = new Texture("exitButton.png");

        ImageButton resumeButton = new ImageButton(new TextureRegionDrawable(resumeTexture));
        ImageButton homeButton = new ImageButton(new TextureRegionDrawable(homeTexture));

        resumeButton.getImage().setScaling(Scaling.fit);
        homeButton.getImage().setScaling(Scaling.fit);

        resumeButton.addListener(new InputListener() {
            @Override public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) { return true; }

            @Override public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Resume clicked!");
                game.setScreen(previousScreen); // balik ke GameScreen
            }
        });

        homeButton.addListener(new InputListener() {
            @Override public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) { return true; }

            @Override public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Exit clicked!");
                game.setScreen(new HomeScreen(game));
            }
        });

        // Table layout
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        float screenWidth = Gdx.graphics.getWidth();
        float buttonWidth = screenWidth * 0.35f;
        float buttonHeight = buttonWidth * 0.35f;

        table.add(resumeButton).size(buttonWidth, buttonHeight).padBottom(30f);
        table.row();
        table.add(homeButton).size(buttonWidth, buttonHeight);
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

    @Override
    public void dispose() {
        stage.dispose();
        overlayTexture.dispose();
        resumeTexture.dispose();
        homeTexture.dispose();
    }
}
