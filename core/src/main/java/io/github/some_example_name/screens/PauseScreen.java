package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound; // IMPORTANT: Import Sound

import io.github.some_example_name.Main;

public class PauseScreen extends BaseScreen {

    private final Main game;
    private final Screen previousScreen;
    private Texture resumeTexture, exitTexture;
    private Texture overlayTexture;

    private Sound clickSound; // Declare the sound object for button clicks

    public PauseScreen(Main game, Screen previousScreen) {
        super(""); // does not need background image, so empty

        this.game = game;
        this.previousScreen = previousScreen;

        // Background black transparent 50%
        Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 0.5f);
        pixmap.fill();
        overlayTexture = new Texture(pixmap);
        pixmap.dispose();

        Image overlay = new Image(new TextureRegionDrawable(overlayTexture));
        overlay.setFillParent(true);
        stage.addActor(overlay);

        // Load buttons
        resumeTexture = new Texture("resumeButton.png");
        exitTexture = new Texture("exitButton.png");

        ImageButton resumeButton = new ImageButton(new TextureRegionDrawable(resumeTexture));
        ImageButton exitButton = new ImageButton(new TextureRegionDrawable(exitTexture));

        resumeButton.getImage().setScaling(Scaling.fit);
        exitButton.getImage().setScaling(Scaling.fit);

        // --- Load the button click sound ---
        String clickSoundPath = "Button Click 1 (audio).MP3"; // Assuming it's directly in assets
        try {
            clickSound = Gdx.audio.newSound(Gdx.files.internal(clickSoundPath));
            Gdx.app.log("PauseScreen", "Button click sound loaded successfully: " + clickSoundPath);
        } catch (Exception e) {
            Gdx.app.error("PauseScreen", "Failed to load button click sound '" + clickSoundPath + "': " + e.getMessage());
            clickSound = null;
        }

        // Listener for Resume button
        resumeButton.addListener(new InputListener() {
            @Override public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) { return true; }

            @Override public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (clickSound != null) { // Play sound when button is released
                    clickSound.play(0.7f); // Play with 70% volume
                }
                game.setScreen(previousScreen); // return to GameScreen
            }
        });

        // Listener for Exit button
        exitButton.addListener(new InputListener() {
            @Override public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) { return true; }

            @Override public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (clickSound != null) { // Play sound when button is released
                    clickSound.play(0.7f); // Play with 70% volume
                }
                game.setScreen(new HomeScreen(game));
            }
        });

        // Layout buttons in the center of the screen
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        float screenWidth = Gdx.graphics.getWidth();
        float buttonWidth = screenWidth * 0.35f;
        float buttonHeight = buttonWidth * 0.35f;

        table.add(resumeButton).size(buttonWidth, buttonHeight).padBottom(30f);
        table.row();
        table.add(exitButton).size(buttonWidth, buttonHeight);
    }

    @Override
    public void dispose() {
        super.dispose();
        overlayTexture.dispose();
        resumeTexture.dispose();
        exitTexture.dispose();

        // IMPORTANT: Dispose the button click sound
        if (clickSound != null) {
            clickSound.dispose();
            Gdx.app.log("PauseScreen", "Button click sound disposed.");
        }
    }
}
