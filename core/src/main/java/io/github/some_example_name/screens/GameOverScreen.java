package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import io.github.some_example_name.Main;

public class GameOverScreen extends BaseScreen {

    private final Main game;
    private Texture retryTexture;
    private Texture homeTexture;
    private Texture titleTexture;

    public GameOverScreen(Main game) {
        super("smallerOne.jpg"); // bg untuk game over
        this.game = game;

        retryTexture = new Texture("retryButton.png");
        homeTexture = new Texture("exitButton.png"); //sek temporary
        titleTexture = new Texture("GameOver.png");

        ImageButton retryButton = new ImageButton(new TextureRegionDrawable(retryTexture));
        ImageButton homeButton = new ImageButton(new TextureRegionDrawable(homeTexture));
        Image titleImage = new Image(new TextureRegionDrawable(titleTexture));

        // Biarkan gambar scaling otomatis
        retryButton.getImage().setScaling(Scaling.fit);
        homeButton.getImage().setScaling(Scaling.fit);
        titleImage.setScaling(Scaling.fit);

        // Listener tombol
        retryButton.addListener(new InputListener() {
            @Override public boolean touchDown(InputEvent e, float x, float y, int p, int b) { return true; }
            @Override public void touchUp(InputEvent e, float x, float y, int p, int b) {
                game.setScreen(new GameScreen(game)); // Restart game
            }
        });

        homeButton.addListener(new InputListener() {
            @Override public boolean touchDown(InputEvent e, float x, float y, int p, int b) { return true; }
            @Override public void touchUp(InputEvent e, float x, float y, int p, int b) {
                game.setScreen(new HomeScreen(game));
            }
        });

        // Table layout
        Table table = new Table();
        table.setFillParent(true);
        table.center();

//.padTop(Gdx.graphics.getHeight() * 0.15f)

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
    public void dispose() {
        super.dispose();
        retryTexture.dispose();
        homeTexture.dispose();
        titleTexture.dispose();
    }
}
