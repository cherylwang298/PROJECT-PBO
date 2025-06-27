package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.audio.Sound; // IMPORTANT: Import Sound

import io.github.some_example_name.Main;
import io.github.some_example_name.managers.GameRoundManager; // Potentially needed for GameScreen

public class HomeScreen implements Screen {

    private final Main game;
    private Stage stage;
    private Texture bgTexture;
    private Texture playTexture;
    private Texture exitTexture;

    private ImageButton playButton;
    private ImageButton exitButton;
    private Table table;
    private Cell<ImageButton> playCell;
    private Cell<ImageButton> exitCell;

    private Sound buttonClickSound; // Declare the sound object for button clicks

    public HomeScreen(Main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Background
        bgTexture = new Texture("smallerOne.jpg");
        Image bg = new Image(new TextureRegionDrawable(bgTexture));
        bg.setFillParent(true);
        stage.addActor(bg);

        // Textures
        playTexture = new Texture("playButtonP.png");
        exitTexture = new Texture("exitButton.png");

        // Buttons
        playButton = new ImageButton(new TextureRegionDrawable(playTexture));
        exitButton = new ImageButton(new TextureRegionDrawable(exitTexture));
        playButton.getImage().setScaling(Scaling.fit);
        exitButton.getImage().setScaling(Scaling.fit);

        // --- Load the button click sound ---
        String clickSoundPath = "Button Click 1 (audio).MP3"; // Assuming it's directly in assets
        try {
            buttonClickSound = Gdx.audio.newSound(Gdx.files.internal(clickSoundPath));
            Gdx.app.log("HomeScreen", "Button click sound loaded successfully: " + clickSoundPath);
        } catch (Exception e) {
            Gdx.app.error("HomeScreen", "Failed to load button click sound '" + clickSoundPath + "': " + e.getMessage());
            buttonClickSound = null;
        }

        playButton.addListener(new InputListener() {
            @Override public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) { return true; }
            @Override public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (buttonClickSound != null) { // Play sound when button is released
                    buttonClickSound.play(0.7f); // Play with 70% volume
                }
                System.out.println("Play clicked!");
                game.setScreen(new GameScreen(game));
            }
        });

        exitButton.addListener(new InputListener() {
            @Override public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) { return true; }
            @Override public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (buttonClickSound != null) { // Play sound when button is released
                    buttonClickSound.play(0.7f); // Play with 70% volume
                }
                System.out.println("Exit clicked!");
                Gdx.app.exit();
            }
        });

        // Table layout
        table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        // Tambahkan tombol (dummy size dulu, akan disesuaikan di resize)
        playCell = table.add(playButton);
        table.row();
        exitCell = table.add(exitButton).padTop(-55f);

        // Pertama kali sesuaikan ukuran
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

        float buttonWidth = width * 0.4f;
        float buttonHeight = buttonWidth * 0.35f;

        playCell.size(buttonWidth, buttonHeight);
        exitCell.size(buttonWidth, buttonHeight);

        table.invalidateHierarchy(); // refresh table layout
    }

    @Override public void show() {
        // You might want to stop any background music from previous screens here if HomeScreen doesn't have its own.
        // Or if HomeScreen has its own background music, start it here.
    }
    @Override public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        stage.dispose();
        bgTexture.dispose();
        playTexture.dispose();
        exitTexture.dispose();

        // IMPORTANT: Dispose the button click sound for HomeScreen
        if (buttonClickSound != null) {
            buttonClickSound.dispose();
            Gdx.app.log("HomeScreen", "Button click sound disposed.");
        }
    }
}
