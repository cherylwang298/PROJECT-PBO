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

import io.github.some_example_name.Main;

public class PauseScreen extends BaseScreen {

    private final Main game;
    private final Screen previousScreen;
    private Texture resumeTexture, exitTexture;
    private Texture overlayTexture;

    public PauseScreen(Main game, Screen previousScreen) {
        super(""); // tidak butuh background image, jadi dikosongkan

        this.game = game;
        this.previousScreen = previousScreen;

        // Background hitam transparan 50%
        Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 0.5f);
        pixmap.fill();
        overlayTexture = new Texture(pixmap);
        pixmap.dispose();

        Image overlay = new Image(new TextureRegionDrawable(overlayTexture));
        overlay.setFillParent(true);
        stage.addActor(overlay);

        // Load tombol
        resumeTexture = new Texture("resumeButton.png");
        exitTexture = new Texture("exitButton.png");

        ImageButton resumeButton = new ImageButton(new TextureRegionDrawable(resumeTexture));
        ImageButton exitButton = new ImageButton(new TextureRegionDrawable(exitTexture));

        resumeButton.getImage().setScaling(Scaling.fit);
        exitButton.getImage().setScaling(Scaling.fit);

        // Listener tombol Resume
        resumeButton.addListener(new InputListener() {
            @Override public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) { return true; }

            @Override public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(previousScreen); // kembali ke GameScreen
            }
        });

        // Listener tombol Exit
        exitButton.addListener(new InputListener() {
            @Override public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) { return true; }

            @Override public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new HomeScreen(game));
            }
        });

        // Layout tombol di tengah layar
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
    }
}
