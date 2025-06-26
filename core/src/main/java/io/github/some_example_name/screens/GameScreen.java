package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.utils.Scaling;

import io.github.some_example_name.Main;
import io.github.some_example_name.entities.Player;
import io.github.some_example_name.managers.GameRoundManager;

public class GameScreen implements Screen {

    private final Main game;
    private Texture gameBackground;
    private Texture pauseTexture;
    private Stage stage;
    private SpriteBatch gameSpriteBatch;
    private ShapeRenderer shapeRenderer;

    private ImageButton pauseButton;
    private Cell<ImageButton> pauseCell;
    private Table table;

    private Player player;
    private GameRoundManager roundManager;

    private Texture heartTexture;
    private Array<Image> heartImages = new Array<>();
    private int heartsRemaining = 5; // jumlah nyawa kota, bisa disesuaikan

    public GameScreen(Main game) {
        this.game = game;

        // Load textures
        gameBackground = new Texture("biggerOne.jpg");
        pauseTexture = new Texture("pauseButton.png");
        heartTexture = new Texture("cityHeart.png");

        stage = new Stage(new ScreenViewport());
        gameSpriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        Gdx.input.setInputProcessor(stage);

        // Background
        Image bg = new Image(new TextureRegionDrawable(gameBackground));
        bg.setFillParent(true);
        bg.setScaling(Scaling.fill);
        stage.addActor(bg);

        // Player setup (posisi tengah layar)
        player = new Player(
            100, // hp
            200, // speed
            40,  // damage
            Gdx.graphics.getWidth() / 2 - 32,
            Gdx.graphics.getHeight() / 2 - 32
        );
        player.setSize(82, 82);

        float paddingX = 280f;
        float paddingTop = 500f;
        float paddingBottom = 720f;

        player.setMovementBounds(
            paddingX,
            Gdx.graphics.getWidth() - paddingX,
            paddingBottom,
            Gdx.graphics.getHeight() - paddingTop
        );

        stage.addActor(player);

        // Pause button UI
        pauseButton = new ImageButton(new TextureRegionDrawable(pauseTexture));
        pauseButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new PauseScreen(game, GameScreen.this));
            }
        });

        table = new Table();
        table.setFillParent(true);
        table.top().left();
        stage.addActor(table);

        float buttonSize = Gdx.graphics.getWidth() * 0.08f;
        pauseCell = table.add(pauseButton).pad(10f).size(buttonSize, buttonSize);

        // Hearts UI (kiri bawah)
        Table heartTable = new Table();
        heartTable.setFillParent(true);
        heartTable.bottom().left();
        stage.addActor(heartTable);

        float heartSize = Gdx.graphics.getWidth() * 0.05f;

        for (int i = 0; i < heartsRemaining; i++) {
            Image heart = new Image(new TextureRegionDrawable(heartTexture));
            heart.setScaling(Scaling.fit);
            heartTable.add(heart).size(heartSize, heartSize).pad(2);
            heartImages.add(heart);
        }

        // Game round manager (untuk spawn monster)
        roundManager = new GameRoundManager(game, player);

        roundManager.setCityHealthListener(new GameRoundManager.CityHealthListener() {
            @Override
            public void onMonsterReachedCity(int damage) {
                reduceCityHealth(damage);
            }
        });
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        roundManager.startRound(0);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        roundManager.update(delta);

        if (Gdx.input.justTouched()) {
            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY(); // konversi Y dari atas ke bawah

            roundManager.handleClick(mouseX, mouseY);
        }

        stage.draw();

        gameSpriteBatch.begin();
        roundManager.render(gameSpriteBatch);
        gameSpriteBatch.end();

        drawPlayerHealthBar();

        // Contoh pindah ronde dengan Enter jika ronde selesai
        if (!roundManager.isRoundActive() && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            roundManager.startNextRound();
        }
    }

    private void drawPlayerHealthBar() {
        float hpPercent = player.getHp() / player.getMaxHp();

        float x = player.getX();
        float y = player.getY() + player.getHeight() + 5;
        float width = player.getWidth();
        float height = 6;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 0, 0, 1); // merah background
        shapeRenderer.rect(x, y, width, height);
        shapeRenderer.setColor(0, 1, 0, 1); // hijau hp sisa
        shapeRenderer.rect(x, y, width * hpPercent, height);
        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        player.setPosition(width / 2 - player.getWidth() / 2, height / 2 - player.getHeight() / 2);

        float buttonSize = width * 0.08f;
        pauseCell.size(buttonSize, buttonSize);
        table.invalidateHierarchy();

        gameSpriteBatch.setProjectionMatrix(stage.getCamera().combined);
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() {
        stage.dispose();
        gameBackground.dispose();
        pauseTexture.dispose();
        heartTexture.dispose();
        player.dispose();
        roundManager.dispose();
        shapeRenderer.dispose();
        gameSpriteBatch.dispose();
    }
    //logic buat hilangin hearts (cheryl)
    public void reduceCityHealth(int amount) {
        heartsRemaining -= amount;
        if (heartsRemaining < 0) heartsRemaining = 0;

        // Hilangkan icon heart yang sudah gak dipakai
        for (int i = heartImages.size - 1; i >= heartsRemaining; i--) {
            Image heartToRemove = heartImages.pop();
            heartToRemove.remove(); // remove dari stage
        }

        // Jika ingin, bisa cek game over di sini
        if (heartsRemaining == 0) {
            // Panggil logic game over, misal:
            Gdx.app.log("GameScreen", "Game Over! Hearts habis.");
            // game.setScreen(new GameOverScreen(game)); // contoh
        }
    }

}
