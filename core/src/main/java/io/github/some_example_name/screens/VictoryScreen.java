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
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound; // Assuming you'll add button click sounds

import io.github.some_example_name.Main;

public class VictoryScreen extends BaseScreen {

    private final Main game;
    private Texture retryTexture;
    private Texture homeTexture;
    private Texture titleTexture;

    private Music victoryMusic;
    private Sound buttonClickSound; // Assuming you'll add button click sounds as well

    public VictoryScreen(Main game) {
        super("smallerOne.jpg");
        this.game = game;

        retryTexture = new Texture("playButtonP.png");
        homeTexture = new Texture("exitButton.png");
        titleTexture = new Texture("Victory.png");

        ImageButton retryButton = new ImageButton(new TextureRegionDrawable(retryTexture));
        ImageButton homeButton = new ImageButton(new TextureRegionDrawable(homeTexture));
        Image titleImage = new Image(new TextureRegionDrawable(titleTexture));

        retryButton.getImage().setScaling(Scaling.fit);
        homeButton.getImage().setScaling(Scaling.fit);
        titleImage.setScaling(Scaling.fit);

        // --- Load the Victory music ---
        try {
            victoryMusic = Gdx.audio.newMusic(Gdx.files.internal("Victory Sound Effect (audio).mp3"));
            Gdx.app.log("VictoryScreen", "Victory music loaded successfully.");
        } catch (Exception e) {
            Gdx.app.error("VictoryScreen", "Failed to load victory music: " + e.getMessage());
            victoryMusic = null;
        }

        // --- Load button click sound for this screen's buttons ---
        String clickSoundPath = "Button Click 1 (audio).MP3";
        try {
            buttonClickSound = Gdx.audio.newSound(Gdx.files.internal(clickSoundPath));
            Gdx.app.log("VictoryScreen", "Button click sound loaded successfully: " + clickSoundPath);
        } catch (Exception e) {
            Gdx.app.error("VictoryScreen", "Failed to load button click sound '" + clickSoundPath + "': " + e.getMessage());
            buttonClickSound = null;
        }


        retryButton.addListener(new InputListener() {
            @Override public boolean touchDown(InputEvent e, float x, float y, int p, int b) { return true; }
            @Override public void touchUp(InputEvent e, float x, float y, int p, int b) {
                if (buttonClickSound != null) {
                    buttonClickSound.play(0.7f);
                }
                // Stop victory music before changing screen
                if (victoryMusic != null) {
                    victoryMusic.stop();
                }
                game.resumeBGM(); // Resume main BGM
                game.setScreen(new GameScreen(game));
            }
        });

        homeButton.addListener(new InputListener() {
            @Override public boolean touchDown(InputEvent e, float x, float y, int p, int b) { return true; }
            @Override public void touchUp(InputEvent e, float x, float y, int p, int b) {
                if (buttonClickSound != null) {
                    buttonClickSound.play(0.7f);
                }
                // Stop victory music before changing screen
                if (victoryMusic != null) {
                    victoryMusic.stop();
                }
                game.resumeBGM(); // Resume main BGM
                game.setScreen(new HomeScreen(game));
            }
        });

        float screenWidth = Gdx.graphics.getWidth();
        float buttonWidth = screenWidth * 0.35f;
        float buttonHeight = buttonWidth * 0.35f;
        float titleWidth = screenWidth * 0.7f;
        float titleHeight = titleWidth * 0.45f;

        Table table = new Table();
        table.setFillParent(true);
        table.center().top().padTop(Gdx.graphics.getHeight() * 0.12f);

        table.add(titleImage).size(titleWidth, titleHeight);
        table.row();
        table.add(retryButton).size(buttonWidth, buttonHeight).padBottom(0.05f);
        table.row();
        table.add(homeButton).size(buttonWidth, buttonHeight);

        stage.addActor(table);
    }

    @Override
    public void show() {
        super.show();
        if (victoryMusic != null) {
            victoryMusic.setLooping(false);
            victoryMusic.setVolume(0.7f);
            victoryMusic.play();
            Gdx.app.log("VictoryScreen", "Victory music started playing.");
        }
    }

    @Override
    public void hide() {
        super.hide();
        if (victoryMusic != null) {
            victoryMusic.stop();
            Gdx.app.log("VictoryScreen", "Victory music stopped playing (screen hidden).");
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void dispose() {
        super.dispose();
        retryTexture.dispose();
        homeTexture.dispose();
        titleTexture.dispose();

        if (victoryMusic != null) {
            victoryMusic.dispose();
            Gdx.app.log("VictoryScreen", "Victory music disposed.");
        }
        if (buttonClickSound != null) {
            buttonClickSound.dispose();
            Gdx.app.log("VictoryScreen", "Button click sound disposed.");
        }
    }
}
