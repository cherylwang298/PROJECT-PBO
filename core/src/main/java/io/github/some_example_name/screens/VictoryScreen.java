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

public class VictoryScreen extends BaseScreen {

    private final Main game;
    private Texture retryTexture;
    private Texture homeTexture;
    private Texture titleTexture;

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

        retryButton.addListener(new InputListener() {
            @Override public boolean touchDown(InputEvent e, float x, float y, int p, int b) { return true; }
            @Override public void touchUp(InputEvent e, float x, float y, int p, int b) {
                game.setScreen(new GameScreen(game));
            }
        });

        homeButton.addListener(new InputListener() {
            @Override public boolean touchDown(InputEvent e, float x, float y, int p, int b) { return true; }
            @Override public void touchUp(InputEvent e, float x, float y, int p, int b) {
                game.setScreen(new HomeScreen(game));
            }
        });

        float screenWidth = Gdx.graphics.getWidth();
        float buttonWidth = screenWidth * 0.35f;
        float buttonHeight = buttonWidth * 0.35f;
        float titleWidth = screenWidth * 0.7f;
        float titleHeight = titleWidth * 0.45f;

        // Table layout
//        Table table = new Table();
//        table.setFillParent(true);
//        table.center().top().padTop(Gdx.graphics.getHeight() * 0.15f); // posisinya responsive
//
//        table.add(titleImage).size(titleWidth, titleHeight).padBottom(Gdx.graphics.getHeight() * 0.04f);
//        table.row();
//        table.add(retryButton).size(buttonWidth, buttonHeight).padBottom(Gdx.graphics.getHeight() * 0.02f);
//        table.row();
//        table.add(homeButton).size(buttonWidth, buttonHeight);
//
        // Table layout
        Table table = new Table();
        table.setFillParent(true);
        table.center().top().padTop(Gdx.graphics.getHeight() * 0.12f); // sedikit lebih naik

        table.add(titleImage).size(titleWidth, titleHeight); // kurangin jarak
        table.row();
        table.add(retryButton).size(buttonWidth, buttonHeight).padBottom(0.05f); // tombol lebih rapat
        table.row();
        table.add(homeButton).size(buttonWidth, buttonHeight); // tombol exit lebih naik


        stage.addActor(table);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        // Optional: add logic to re-layout elements if needed during resize
    }

    @Override
    public void dispose() {
        super.dispose();
        retryTexture.dispose();
        homeTexture.dispose();
        titleTexture.dispose();
    }
}
