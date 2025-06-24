        // === Package: io.github.some_example_name.screens ===
        package io.github.some_example_name.screens;

        import com.badlogic.gdx.Gdx;
        import com.badlogic.gdx.Input; // Added for player input
        import com.badlogic.gdx.Screen;
        import com.badlogic.gdx.graphics.GL20; // Added for screen clearing
        import com.badlogic.gdx.graphics.Texture;
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
        import io.github.some_example_name.entities.Player; // Import your Player class

        public class GameScreen implements Screen {

            private final Main game;
            private Texture gameBackground;
            private Texture pauseTexture;
            private Stage stage;

            private ShapeRenderer shapeRenderer;

            private ImageButton pauseButton;
            private Cell<ImageButton> pauseCell;
            private Table table;

            private Player player; // Declare the Player object

            private Texture heartTexture;
            private Array<Image> heartImages = new Array<>();
            private int heartsRemaining = 3; // jml nyawa awal (bisa kegnti next)

            public GameScreen(Main game) {
                this.game = game;

                // Load textures for UI elements
                gameBackground = new Texture("biggerOne.jpg");
                pauseTexture = new Texture("pauseButton.png");

                // Set up stage (for UI and now your Player Actor)
                stage = new Stage(new ScreenViewport());

                shapeRenderer = new ShapeRenderer();


                // CRITICAL: Set input processor to the stage so it can handle input for Actors (like your Player)
                Gdx.input.setInputProcessor(stage);


                // --- CORRECTED ORDER: Add background FIRST, then player, then UI ---
                // 1. Background as Image actor (should be at the very bottom layer)
                Image bg = new Image(new TextureRegionDrawable(gameBackground));
                bg.setFillParent(true);
                bg.setScaling(Scaling.fill);
                stage.addActor(bg); // Add background first so it draws behind everything else

                // 2. Initialize and Add your Player Actor
                // Player constructor: (initialHp, initialSpeed, initialDamage, startX, startY)
                // startX and startY place the player roughly in the center initially.
                // The -32 is for centering (assuming player sprites are around 64x64, as Actor position is bottom-left).
                player = new Player(
                    100, // initialHp
                    200, // initialSpeed (Adjust as needed)
                    10,  // initialDamage
                    Gdx.graphics.getWidth() / 2 - 32, // X-coordinate for player's bottom-left corner
                    Gdx.graphics.getHeight() / 2 - 32 // Y-coordinate for player's bottom-left corner
                );
        //        player.setScale(0.2f);
                player.setSize(64, 64);

                float paddingX = 65f;
                float paddingTop = 300f;
                float paddingBottom = 400f;
    //            player.setMovementBounds(
    //                paddingX,
    //                Gdx.graphics.getWidth() - paddingX,
    //                paddingY,
    //                Gdx.graphics.getHeight() - paddingY
    //            );
                player.setMovementBounds(
                    paddingX,                                // minX
                    Gdx.graphics.getWidth() - paddingX,     // maxX
                    paddingBottom,                              // minY
                    Gdx.graphics.getHeight() - paddingTop       // maxY
                );



                stage.addActor(player); // Add the player to the stage AFTER the background

                // 3. Pause Button and Table (should be on top of game elements)
                pauseButton = new ImageButton(new TextureRegionDrawable(pauseTexture));
                pauseButton.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }

                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        System.out.println("Pause clicked!");
                        // Make sure PauseScreen exists and accepts these arguments
                        // Use GameScreen.this to refer to the GameScreen instance
                        game.setScreen(new PauseScreen(game, GameScreen.this));
                    }
                });

                table = new Table();
                table.setFillParent(true);
                table.top().left(); // Position button top-left
                stage.addActor(table); // Add table (and button) after player so it appears on top

                // Responsive initial size for button
                float buttonSize = Gdx.graphics.getWidth() * 0.08f;
                pauseCell = table.add(pauseButton).pad(10f).size(buttonSize, buttonSize);

                //HEARTS UI: kiri bawah
                heartTexture = new Texture("cityHeart.png");

                Table heartTable = new Table();
                heartTable.setFillParent(true);
                heartTable.bottom().left(); // Posisi kiri bawah
                stage.addActor(heartTable); // Tambahkan ke stage

                float heartSize = Gdx.graphics.getWidth() * 0.05f; // Responsive

                for (int i = 0; i < heartsRemaining; i++) {
                    Image heart = new Image(new TextureRegionDrawable(heartTexture));
                    heart.setScaling(Scaling.fit);
                    heartTable.add(heart).size(heartSize, heartSize).pad(2);
                    heartImages.add(heart);
                }

            }

            @Override
            public void show() {
                // This method is called when this screen becomes the current screen.
                // All necessary setup is done in the constructor for this scenario.
                Gdx.input.setInputProcessor(stage); // Ini WAJIB supaya tombol pause bisa diklik lagi
            }

            @Override
            public void render(float delta) {
                // Clear the screen with a background color before drawing anything
                Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1); // Dark grey background
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

                // Your friend's existing render loop:
                // stage.act(delta) automatically calls the act() method of all actors on the stage (including your player).
                // stage.draw() automatically calls the draw() method of all actors on the stage (including your player).
                stage.act(delta);
                stage.draw();

                drawPlayerHealthBar();

                // If you want to handle player attack input (e.g., Spacebar) directly from GameScreen,
                // you would add it AFTER stage.act(delta) but before stage.draw() if it impacts rendering
                // or just after stage.act(delta) if it only updates internal state.
                // Since player.act(delta) now handles input, this is technically not strictly needed here anymore,
                // but it shows where you *could* add more direct screen input handling if needed.
                // if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                //     player.attack();
                // }
                // The Player's act() method already handles the SPACE key for attack, so this line isn't necessary here.
            }

            @Override
            public void resize(int width, int height) {
                // Update the stage's viewport when the screen size changes
                stage.getViewport().update(width, height, true);

                // Update player's position on resize to keep it centered
                player.setPosition(width / 2 - player.getWidth() / 2, height / 2 - player.getHeight() / 2);

                // Adjust button size for responsiveness
                float buttonSize = width * 0.08f;
                pauseCell.size(buttonSize, buttonSize);
                table.invalidateHierarchy(); // refresh layout
            }

            @Override public void pause() {}
            @Override public void resume() {}
            @Override public void hide() {}


    //        jangan dispose dulu, soale kalau resume pause dia ke dispose
            @Override
            public void dispose() {
                // Dispose of all disposable assets to prevent memory leaks
    //            stage.dispose();
    //            gameBackground.dispose();
    //            pauseTexture.dispose();
    //            player.dispose(); // Dispose the player's textures
            }

            private void drawPlayerHealthBar() {
                float hpPercent = player.getHp() / player.getMaxHp();

                float x = player.getX();
                float y = player.getY() + player.getHeight() + 5; // 5 pixel di atas kepala
                float width = player.getWidth();
                float height = 6;

                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

                // Merah (background)
                shapeRenderer.setColor(1, 0, 0, 1);
                shapeRenderer.rect(x, y, width, height);

                // Hijau (sisa HP)
                shapeRenderer.setColor(0, 1, 0, 1);
                shapeRenderer.rect(x, y, width * hpPercent, height);

                shapeRenderer.end();
            }


        }
