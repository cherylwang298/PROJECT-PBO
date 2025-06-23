package io.github.some_example_name.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public abstract class BaseScreen implements Screen {
    protected Stage stage;
    protected Texture background;

    public BaseScreen(String bgPath) {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
//
//        background = new Texture(bgPath);
//        Image bgImage = new Image(new TextureRegionDrawable(background));
//        bgImage.setFillParent(true);
//        stage.addActor(bgImage);

        if (bgPath != null && !bgPath.isEmpty()) {
            background = new Texture(bgPath);
            Image bgImage = new Image(new TextureRegionDrawable(background));
            bgImage.setFillParent(true);
            stage.addActor(bgImage);
        }
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
//        background.dispose();
        if (background != null) {
            background.dispose();
        }
    }
}
