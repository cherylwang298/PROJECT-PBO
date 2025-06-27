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
import static io.github.some_example_name.entities.Player.AttackDirection; // Import the enum directly for cleaner code

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
            20,  // damage
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
        roundManager.startRound(0);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        roundManager.update(delta);

        // --- Handle Input for Attacks ---
        // Mouse Click Attack
        if (Gdx.input.justTouched()) {
            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY(); // konversi Y dari atas ke bawah

            // Calculate player's center for direction calculation
            float playerCenterX = player.getX() + player.getWidth() / 2;
            float playerCenterY = player.getY() + player.getHeight() / 2;

            // Determine attack direction based on mouse position relative to player
            float dirX = mouseX - playerCenterX;
            float dirY = mouseY - playerCenterY;

            AttackDirection calculatedDirection = AttackDirection.NONE;

            // Use a simple comparison to determine the most dominant direction
            // You can refine this with dead zones or specific angles if needed
            if (Math.abs(dirX) > Math.abs(dirY)) { // More horizontal movement
                if (dirX > 0) {
                    calculatedDirection = AttackDirection.RIGHT;
                } else {
                    calculatedDirection = AttackDirection.LEFT;
                }
            } else { // More vertical movement, or purely vertical
                if (dirY > 0) {
                    calculatedDirection = AttackDirection.UP;
                } else {
                    calculatedDirection = AttackDirection.DOWN;
                }
            }

            // Set the attack direction in the Player class before triggering the attack
            player.setAttackDirection(calculatedDirection);

            // 1. Trigger player's internal attack state (for animation/cooldown)
            player.attack();
            // 2. Pass the click coordinates to the round manager for damage calculation
            roundManager.handleClick(mouseX, mouseY);
        }

        // Keyboard 'K' Attack
        if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
            AttackDirection kAttackDirection = AttackDirection.NONE; // Initialize

            // Prioritize vertical movement for K-attack direction
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                kAttackDirection = AttackDirection.UP;
            } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                kAttackDirection = AttackDirection.DOWN;
            } else {
                // If no vertical movement, default to DOWN attack
                // This makes 'K' a general downward smash if not moving vertically
                kAttackDirection = AttackDirection.DOWN;
            }

            // Set the attack direction in the Player class
            player.setAttackDirection(kAttackDirection);

            // 1. Trigger player's internal attack state (for animation/cooldown)
            player.attack();
            // 2. Tell the round manager to process a keyboard-triggered attack
            roundManager.handleKeyboardAttack();
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

    public void reduceCityHealth(int amount) {
        heartsRemaining -= amount;
        if (heartsRemaining < 0) heartsRemaining = 0;

        for (int i = heartImages.size - 1; i >= heartsRemaining; i--) {
            if (i < heartImages.size) {
                Image heartToRemove = heartImages.pop();
                heartToRemove.remove();
            }
        }

        if (heartsRemaining == 0) {
            Gdx.app.log("GameScreen", "Game Over! Hearts habis.");
        }
    }
}
