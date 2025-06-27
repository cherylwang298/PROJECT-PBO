package io.github.some_example_name.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Slime extends Monsters {

    private Texture slimeFront, slimeBack, slimeLeft, slimeRight;
    private Texture currentTexture;

    private float textureWidth, textureHeight;
    private float exitX, exitY;
    private float waypointX, waypointY;

    private boolean useWaypoint = true;
    private boolean reachedWaypoint = false;

    private enum MovementStyle { BELOK_DULU, TURUN_DULU, LANGSUNG }
    private MovementStyle movementStyle;

    public Slime(float x, float y) {
        super(x, y);

        this.health = 80;
        this.maxHealth=80;  //buat maxhealth healthbar
        this.speed = 60.0f;
        this.damageTocity = 1;
        this.isAggressive = false;

        try {
            slimeFront = new Texture(Gdx.files.internal("slimeFront.png"));
            slimeBack = new Texture(Gdx.files.internal("slimeBack.png"));
            slimeLeft = new Texture(Gdx.files.internal("slimeLeft.png"));
            slimeRight = new Texture(Gdx.files.internal("slimeRight.png"));
            currentTexture = slimeFront;

            textureWidth = slimeFront.getWidth();
            textureHeight = slimeFront.getHeight();
        } catch (Exception e) {
            Gdx.app.error("Slime", "Failed to load slime textures: " + e.getMessage());
            slimeFront = slimeBack = slimeLeft = slimeRight = new Texture("placeholder.png");
            currentTexture = slimeFront;
            textureWidth = textureHeight = 32;
        }

        this.setSize(64, 64);
        assignRandomExitWithWaypoint();
    }

    public Slime(float x, float y, float exitX, float exitY) {
        this(x, y);
        this.exitX = exitX;
        this.exitY = exitY;
    }

    private void assignRandomExitWithWaypoint() {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        exitX = screenWidth * 0.50f; // original = 0.50f
        exitY = screenHeight * 0.08f;

        int style = (int)(Math.random() * 3); // 0, 1, 2
        useWaypoint = true;
        reachedWaypoint = false;

        if (style == 0) {
            // Belok dulu ke kiri/kanan lalu turun
            float offsetX = (float)(Math.random() * 150 - 75); // ±75px
            waypointX = x + offsetX;
            waypointY = y;
            movementStyle = MovementStyle.BELOK_DULU;

        } else if (style == 1) {
            // Turun dulu, baru belok
            waypointX = x;
            waypointY = y - (float)(Math.random() * 80 + 40); // 40–120px
            movementStyle = MovementStyle.TURUN_DULU;

        } else {
            // Langsung ke pintu keluar
            useWaypoint = false;
            reachedWaypoint = true;
            movementStyle = MovementStyle.LANGSUNG;
        }

        Gdx.app.log("Slime", "Spawned with movement style: " + movementStyle.name());
    }

    @Override
    public void update(float deltaTime, Player player) {
        float slimeCenterX = x + width / 2;
        float slimeCenterY = y + height / 2;

        Vector2 target = new Vector2(
            (!useWaypoint || reachedWaypoint) ? exitX : waypointX,
            (!useWaypoint || reachedWaypoint) ? exitY : waypointY
        );

        Vector2 move = new Vector2(target.x - slimeCenterX, target.y - slimeCenterY);

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

        // Arah animasi berdasarkan gerakan
        if (Math.abs(move.x) > Math.abs(move.y)) {
            currentTexture = (move.x > 0) ? slimeRight : slimeLeft;
        } else if (Math.abs(move.y) > 0.01f) {
            currentTexture = (move.y > 0) ? slimeBack : slimeFront;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(currentTexture, x, y, width, height);
    }

    @Override
    public boolean shouldAttackPlayer(Player player) {
        return false;
    }

    @Override
    public void dispose() {
        slimeFront.dispose();
        slimeBack.dispose();
        slimeLeft.dispose();
        slimeRight.dispose();
    }

    @Override
    public float getWidth() { return width; }

    @Override
    public float getHeight() { return height; }

    @Override
    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean hasReachedCity() {
        return y <= 50; // sesuaikan threshold pintu keluar
    }

    @Override
    public void kill() {
        this.health = 0;
    }
}
