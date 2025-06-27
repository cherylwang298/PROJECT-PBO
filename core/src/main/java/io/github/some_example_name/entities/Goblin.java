package io.github.some_example_name.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import io.github.some_example_name.loots.*;

import java.util.Random;

public class Goblin extends Monsters {

    private Texture front1, front2;
    private Texture left1, left2;
    private Texture right1, right2;
    private Texture currentTexture;

    private float animationTimer = 0f;
    private final float FRAME_DURATION = 0.2f;
    private boolean toggleFrame = false;

    private float exitX, exitY;
    private float waypointX, waypointY;
    private boolean useWaypoint = true;
    private boolean reachedWaypoint = false;

    private boolean aggro = false;

    //testing loot: cheryl
    private LootEffect lootEffect;
    private LootManager lootManager;
    private final Random rand = new Random();

    public Goblin(float x, float y) {
        super(x, y);

        this.health = 70;
        this.speed = 155.0f;
        this.maxHealth = 40;
        this.damageToplayer = 5;
        this.damageTocity = 1;
        this.isAggressive = false;

        try {
            front1 = new Texture(Gdx.files.internal("goblinFront1.png"));
            front2 = new Texture(Gdx.files.internal("goblinfront2.png"));
            left1 = new Texture(Gdx.files.internal("goblinLeft1.png"));
            left2 = new Texture(Gdx.files.internal("goblinLeft2.png"));
            right1 = new Texture(Gdx.files.internal("goblinRight1.png"));
            right2 = new Texture(Gdx.files.internal("goblinRight2.png"));
            currentTexture = front1;
        } catch (Exception e) {
            Gdx.app.error("Goblin", "Failed to load goblin textures.");
            front1 = front2 = left1 = left2 = right1 = right2 = new Texture("placeholder.png");
            currentTexture = front1;
        }

        this.setSize(64, 64);
        assignRandomExitWithWaypoint();
    }

    public Goblin(float x, float y, float exitX, float exitY, LootManager lootManager) {
        this(x, y);
        this.exitX = exitX;
        this.exitY = exitY;

        //test loot
        this.lootManager = lootManager;
        this.lootEffect = new SpeedLootEffect(rand.nextInt(5, 11));
    }

    private void assignRandomExitWithWaypoint() {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        exitX = screenWidth * 0.50f;
        exitY = screenHeight * 0.08f;

        int style = (int)(Math.random() * 3);
        useWaypoint = true;
        reachedWaypoint = false;

        if (style == 0) {
            float offsetX = (float)(Math.random() * 150 - 75);
            waypointX = x + offsetX;
            waypointY = y;
        } else if (style == 1) {
            waypointX = x;
            waypointY = y - (float)(Math.random() * 80 + 40);
        } else {
            useWaypoint = false;
            reachedWaypoint = true;
        }
    }

    @Override
    public void update(float deltaTime, Player player) {
        animationTimer += deltaTime;
        if (animationTimer >= FRAME_DURATION) {
            toggleFrame = !toggleFrame;
            animationTimer = 0f;
        }

        float centerX = x + width / 2;
        float centerY = y + height / 2;

        Vector2 target = new Vector2(
            (!useWaypoint || reachedWaypoint) ? exitX : waypointX,
            (!useWaypoint || reachedWaypoint) ? exitY : waypointY
        );

        Vector2 move = new Vector2(target.x - centerX, target.y - centerY);

        if (move.len() < 5f) {
            if (useWaypoint && !reachedWaypoint) {
                reachedWaypoint = true;
            } else {
                move.setZero();
            }
        } else {
            move.nor();
        }

        x += move.x * speed * deltaTime;
        y += move.y * speed * deltaTime;

        if (Math.abs(move.x) > Math.abs(move.y)) {
            currentTexture = (move.x > 0) ? (toggleFrame ? right1 : right2) : (toggleFrame ? left1 : left2);
        } else {
            currentTexture = (toggleFrame ? front1 : front2);
        }

        if (!aggro && isReachedExit()) {
            this.health = 0;
        }
    }

    private boolean isReachedExit() {
        return Vector2.dst(x + width / 2, y + height / 2, exitX, exitY) < 20f;
    }


    @Override
    public boolean shouldAttackPlayer(Player player) {
        return aggro;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(currentTexture, x, y, width, height);
    }

    @Override
    public void dispose() {
        front1.dispose(); front2.dispose();
        left1.dispose(); left2.dispose();
        right1.dispose(); right2.dispose();
    }

    @Override public float getWidth() { return width; }
    @Override public float getHeight() { return height; }
    @Override public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean hasReachedCity() {
        return y <= 50;
    }

    @Override
    public void kill() {
        this.health = 0;

    }

    @Override
    public LootEffect getLootEffect(){
        return isKilledByPlayer() ? lootEffect : null;
    }

    @Override
    public void renderHealthBar(SpriteBatch batch) {
        float barWidth = this.width;
        float barHeight = 6f;

        float healthRatio = Math.max(0, Math.min(1, health / (float) maxHealth));
        float currentBarWidth = barWidth * healthRatio;

        float barX = this.x;
        float barY = this.y + this.height + 5; // 5px di atas kepala monster

        // background bar (hitam)
        batch.setColor(1, 0, 0, 1);
        batch.draw(Monsters.pixelTexture, barX, barY, barWidth, barHeight);

        // health bar
        batch.setColor(0, 1, 0, 1);
        batch.draw(Monsters.pixelTexture, barX, barY, currentBarWidth, barHeight);

        batch.setColor(1, 1, 1, 1); // reset warna
    }


}
