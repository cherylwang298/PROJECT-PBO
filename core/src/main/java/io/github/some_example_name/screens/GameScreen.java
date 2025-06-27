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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.audio.Sound; // IMPORTANT: Import Sound

import io.github.some_example_name.Main;
import io.github.some_example_name.entities.Player;
import io.github.some_example_name.managers.GameRoundManager;
import static io.github.some_example_name.entities.Player.AttackDirection;

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
    private int heartsRemaining = 5;

    private Sound buttonClickSound; // Declare the sound object for button clicks

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

        // --- Load the button click sound ---
//        String clickSoundPath = "Button Click 1 (audio).MP3"; // Assuming it's directly in assets
//        try {
//            buttonClickSound = Gdx.audio.newSound(Gdx.files.internal(clickSoundPath));
//            Gdx.app.log("GameScreen", "Button click sound loaded successfully: " + clickSoundPath);
//        } catch (Exception e) {
//            Gdx.app.error("GameScreen", "Failed to load button click sound '" + clickSoundPath + "': " + e.getMessage());
//            buttonClickSound = null;
//        }

        // Pause button UI
        pauseButton = new ImageButton(new TextureRegionDrawable(pauseTexture));
        pauseButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (buttonClickSound != null) { // Play sound when button is released
                    buttonClickSound.play(0.7f); // Play with 70% volume
                }
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
            // This logic favors diagonal directions if both x and y components are significant
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
            boolean movingUp = Gdx.input.isKeyPressed(Input.Keys.W);
            boolean movingDown = Gdx.input.isKeyPressed(Input.Keys.S);
            boolean movingLeft = Gdx.input.isKeyPressed(Input.Keys.A);
            boolean movingRight = Gdx.input.isKeyPressed(Input.Keys.D);

            if (movingUp) {
                kAttackDirection = AttackDirection.UP;
            } else if (movingDown) {
                kAttackDirection = AttackDirection.DOWN;
            } else if (movingRight) { // If not moving vertically, check horizontal
                kAttackDirection = AttackDirection.RIGHT;
            } else if (movingLeft) {
                kAttackDirection = AttackDirection.LEFT;
            } else {
                // If no movement keys pressed, default to a specific direction, e.g., DOWN
                // This makes 'K' a general downward smash if not moving
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

        // --- DEBUGGING: Draw Player Attack Hitbox (di player cari ATTACK_BOX_SIZE)---
        // Uncomment this block in your GameScreen.render() if you want to visualize the player's attack hitbox.
        // You will need to ensure 'game' has a ShapeRenderer getter, as commented out in GameRoundManager.
        /*
        if (player.isAttacking()) {
            shapeRenderer.setProjectionMatrix(stage.getCamera().combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line); // Use Line for outline
            shapeRenderer.setColor(1, 1, 0, 1); // Yellow color for hitbox
            Rectangle attackRect = player.getAttackRect();
            shapeRenderer.rect(attackRect.x, attackRect.y, attackRect.width, attackRect.height);
            shapeRenderer.end();
        }
        */
        // --- END DEBUGGING ---

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

        // IMPORTANT: Dispose the button click sound for GameScreen
        if (buttonClickSound != null) {
            buttonClickSound.dispose();
            Gdx.app.log("GameScreen", "Button click sound disposed.");
        }
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
