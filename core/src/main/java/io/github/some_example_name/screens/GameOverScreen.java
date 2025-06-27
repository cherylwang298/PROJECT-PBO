package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen; // Ensure Screen is imported
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.audio.Music; // IMPORTANT: Import Music
import com.badlogic.gdx.audio.Sound; // Assuming you'll add button click sounds later

import io.github.some_example_name.Main;

public class GameOverScreen extends BaseScreen {

    private final Main game;
    private Texture retryTexture;
    private Texture homeTexture;
    private Texture titleTexture;

    private Music gameOverMusic; // Declare the Music object for game over audio
    private Sound buttonClickSound; // Assuming you'll add button click sounds as well

    public GameOverScreen(Main game) {
        super("smallerOne.jpg"); // bg for game over
        this.game = game;

        retryTexture = new Texture("retryButton.png");
        homeTexture = new Texture("exitButton.png"); // temp for now
        titleTexture = new Texture("GameOver.png");

        ImageButton retryButton = new ImageButton(new TextureRegionDrawable(retryTexture));
        ImageButton homeButton = new ImageButton(new TextureRegionDrawable(homeTexture));
        Image titleImage = new Image(new TextureRegionDrawable(titleTexture));

        retryButton.getImage().setScaling(Scaling.fit);
        homeButton.getImage().setScaling(Scaling.fit);
        titleImage.setScaling(Scaling.fit);

        // --- Load the Game Over music ---
        String gameOverMusicPath = "Game Over 4 (audio).MP3"; // Assuming it's directly in assets
        try {
            gameOverMusic = Gdx.audio.newMusic(Gdx.files.internal(gameOverMusicPath));
            Gdx.app.log("GameOverScreen", "Game over music loaded successfully: " + gameOverMusicPath);
        } catch (Exception e) {
            Gdx.app.error("GameOverScreen", "Failed to load game over music '" + gameOverMusicPath + "': " + e.getMessage());
            gameOverMusic = null;
        }

        // --- Load button click sound for this screen's buttons ---
        String clickSoundPath = "Button Click 1 (audio).MP3"; // Assuming it's directly in assets
        try {
            buttonClickSound = Gdx.audio.newSound(Gdx.files.internal(clickSoundPath));
            Gdx.app.log("GameOverScreen", "Button click sound loaded successfully: " + clickSoundPath);
        } catch (Exception e) {
            Gdx.app.error("GameOverScreen", "Failed to load button click sound '" + clickSoundPath + "': " + e.getMessage());
            buttonClickSound = null;
        }


        // Listener for Retry button
        // Listener for Retry button
        retryButton.addListener(new InputListener() {
            @Override public boolean touchDown(InputEvent e, float x, float y, int p, int b) { return true; }
            @Override public void touchUp(InputEvent e, float x, float y, int p, int b) {
                if (buttonClickSound != null) {
                    buttonClickSound.play(0.7f);
                }
                if (gameOverMusic != null) {
                    gameOverMusic.stop(); // Stop game over music
                }
                game.resumeBGM(); // Resume main BGM
                game.setScreen(new GameScreen(game)); // Restart game
            }
        });

        // Listener for Home button
        homeButton.addListener(new InputListener() {
            @Override public boolean touchDown(InputEvent e, float x, float y, int p, int b) { return true; }
            @Override public void touchUp(InputEvent e, float x, float y, int p, int b) {
                if (buttonClickSound != null) {
                    buttonClickSound.play(0.7f);
                }
                if (gameOverMusic != null) {
                    gameOverMusic.stop(); // Stop game over music
                }
                game.resumeBGM(); // Resume main BGM
                game.setScreen(new HomeScreen(game));
            }
        });

        // Table layout
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        float screenWidth = Gdx.graphics.getWidth();
        float buttonWidth = screenWidth * 0.4f;
        float buttonHeight = buttonWidth * 0.35f;
        float titleWidth = screenWidth * 0.55f;
        float titleHeight = titleWidth * 0.5f;

        table.add(titleImage).size(titleWidth, titleHeight).padBottom(50f);
        table.row();
        table.add(retryButton).size(buttonWidth, buttonHeight).padBottom(30f);
        table.row();
        table.add(homeButton).size(buttonWidth, buttonHeight);

        stage.addActor(table);
    }

    @Override
    public void show() {
        super.show(); // Call superclass show if it has logic
        // Play the music when the screen becomes active
        if (gameOverMusic != null) {
            gameOverMusic.setLooping(false); // Play once. Set to true if you want it to loop.
            gameOverMusic.setVolume(0.7f); // Adjust volume (0.0 to 1.0)
            gameOverMusic.play();
            Gdx.app.log("GameOverScreen", "Game over music started playing.");
        }
    }

    @Override
    public void hide() {
        super.hide(); // Call superclass hide if it has logic
        // Stop music when screen is no longer active (e.g., switching to another screen)
        if (gameOverMusic != null) {
            gameOverMusic.stop();
            Gdx.app.log("GameOverScreen", "Game over music stopped playing (screen hidden).");
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        retryTexture.dispose();
        homeTexture.dispose();
        titleTexture.dispose();

        // IMPORTANT: Dispose the game over music
        if (gameOverMusic != null) {
            gameOverMusic.dispose();
            Gdx.app.log("GameOverScreen", "Game over music disposed.");
        }
        // IMPORTANT: Dispose the button click sound
        if (buttonClickSound != null) {
            buttonClickSound.dispose();
            Gdx.app.log("GameOverScreen", "Button click sound disposed.");
        }
    }
}
