//package io.github.some_example_name.entities;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.math.Vector2;
//
//public class Buffalo extends Monsters {
//
//    private Texture front1, front2, right1, right2, left1, left2;
//    private Texture currentTexture;
//
//    private float directionX = 0f;
//    private float directionY = -1f;
//    private float knockbackForce = 150f;
//    private float knockbackCooldown = 1.5f;
//    private float knockbackTimer = 0f;
//
//    private float animationTimer = 0f;
//    private float animationSpeed = 0.2f;
//    private boolean useAltFrame = false;
//
//    private float exitX, exitY;
//    private float waypointX, waypointY;
//    private boolean useWaypoint = true;
//    private boolean reachedWaypoint = false;
//
//    public Buffalo(float x, float y) {
//        super(x, y);
//        this.health = 100;
//        this.speed = 80f;
//        this.damage = 10;
//        this.attackRadius = 80f;
//        this.isAggressive = true;
//
//        try {
//            front1 = new Texture(Gdx.files.internal("buffaloFront1.png"));
//            front2 = new Texture(Gdx.files.internal("buffaloFront2.png"));
//            right1 = new Texture(Gdx.files.internal("buffaloRight1.png"));
//            right2 = new Texture(Gdx.files.internal("buffaloRight2.png"));
//            left1 = new Texture(Gdx.files.internal("buffaloLeft1.png"));
//            left2 = new Texture(Gdx.files.internal("buffaloLeft2.png"));
//            currentTexture = front1;
//        } catch (Exception e) {
//            front1 = front2 = right1 = right2 = left1 = left2 = new Texture("placeholder.png");
//            currentTexture = front1;
//            Gdx.app.error("Buffalo", "Texture not found. Using placeholder.");
//        }
//
//        this.setSize(64, 64);
//        assignRandomExitWithWaypoint();
//    }
//
//    public Buffalo(float x, float y, float exitX, float exitY) {
//        this(x, y);
//        this.exitX = exitX;
//        this.exitY = exitY;
//    }
//
//    private void assignRandomExitWithWaypoint() {
//        float screenWidth = Gdx.graphics.getWidth();
//        float screenHeight = Gdx.graphics.getHeight();
//
//        exitX = screenWidth * 0.50f;
//        exitY = screenHeight * 0.08f;
//
//        int style = (int)(Math.random() * 3);
//        useWaypoint = true;
//        reachedWaypoint = false;
//
//        if (style == 0) {
//            float offsetX = (float)(Math.random() * 150 - 75);
//            waypointX = x + offsetX;
//            waypointY = y;
//        } else if (style == 1) {
//            waypointX = x;
//            waypointY = y - (float)(Math.random() * 80 + 40);
//        } else {
//            useWaypoint = false;
//            reachedWaypoint = true;
//        }
//    }
//
//    @Override
//    public void update(float deltaTime, Player player) {
//        animationTimer += deltaTime;
//        if (animationTimer >= animationSpeed) {
//            useAltFrame = !useAltFrame;
//            animationTimer = 0f;
//        }
//
//        float centerX = x + width / 2;
//        float centerY = y + height / 2;
//
//        Vector2 target = new Vector2(
//            (!useWaypoint || reachedWaypoint) ? exitX : waypointX,
//            (!useWaypoint || reachedWaypoint) ? exitY : waypointY
//        );
//
//        Vector2 move = new Vector2(target.x - centerX, target.y - centerY);
//
//        if (move.len() < 5f) {
//            if (useWaypoint && !reachedWaypoint) {
//                reachedWaypoint = true;
//            } else {
//                move.setZero();
//            }
//        } else {
//            move.nor();
//        }
//
//        x += move.x * speed * deltaTime;
//        y += move.y * speed * deltaTime;
//
//        directionX = move.x;
//        directionY = move.y;
//
//        if (isPlayerInFront(player)) {
//            knockbackTimer += deltaTime;
//            if (knockbackTimer >= knockbackCooldown) {
//                applyKnockback(player);
//                knockbackTimer = 0f;
//            }
//        }
//    }
//
//    private boolean isPlayerInFront(Player player) {
//        float px = player.getX() + player.getWidth() / 2;
//        float py = player.getY() + player.getHeight() / 2;
//
//        float bx = x + width / 2;
//        float by = y + height / 2;
//
//        Vector2 toPlayer = new Vector2(px - bx, py - by);
//        return toPlayer.len() <= attackRadius && Math.abs(toPlayer.angleDeg()) < 45;
//    }
//
//    private boolean isPlayerOnSide(Player player) {
//        float px = player.getX() + player.getWidth() / 2;
//        float bx = x + width / 2;
//        return Math.abs(px - bx) > width * 0.3f;
//    }
//
//    private void applyKnockback(Player player) {
//        Vector2 push = new Vector2(player.getX() - x, player.getY() - y).nor().scl(knockbackForce);
//        player.getPosition().add(push);
//        Gdx.app.log("Buffalo", "Knockback applied to player!");
//    }
//
//    @Override
//    public void onHit(Player player) {
//        if (isPlayerOnSide(player)) {
//            health -= 10;
//            Gdx.app.log("Buffalo", "Player hit from side. Health now: " + health);
//            if (health <= 0) kill();
//        } else {
//            Gdx.app.log("Buffalo", "Player attack from front blocked (knockback).");
//        }
//    }
//
//    @Override
//    public boolean shouldAttackPlayer(Player player) {
//        return true;
//    }
//
//    @Override
//    public void render(SpriteBatch batch) {
//        Texture currentFrame = front1;
//
//        if (directionY < 0) {
//            currentFrame = useAltFrame ? front2 : front1;
//        } else if (directionX > 0) {
//            currentFrame = useAltFrame ? right2 : right1;
//        } else if (directionX < 0) {
//            currentFrame = useAltFrame ? left2 : left1;
//        }
//
//        batch.draw(currentFrame, x, y, width, height);
//    }
//
//    @Override
//    public void dispose() {
//        front1.dispose(); front2.dispose();
//        right1.dispose(); right2.dispose();
//        left1.dispose(); left2.dispose();
//    }
//
//    @Override
//    public void setSize(float width, float height) {
//        this.width = width;
//        this.height = height;
//    }
//
//    @Override
//    public boolean hasReachedCity() {
//        return y <= 50;
//    }
//
//    @Override
//    public void kill() {
//        this.health = 0;
//    }
//}


package io.github.some_example_name.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Buffalo extends Monsters {

    private Texture front1, front2, right1, right2, left1, left2;
    private Texture currentTexture;

    private float directionX = 0f;
    private float directionY = -1f;
    private float knockbackForce = 150f;
    private float knockbackCooldown = 1.5f;
    private float knockbackTimer = 0f;

    private float animationTimer = 0f;
    private float animationSpeed = 0.2f;
    private boolean useAltFrame = false;

    private float exitX, exitY;
    private float waypointX, waypointY;
    private boolean useWaypoint = true;
    private boolean reachedWaypoint = false;

    public Buffalo(float x, float y) {
        super(x, y);
        this.health = 100;
        this.maxHealth=100; //buat maxhealth healthbar
        this.speed = 80f;
        this.damageToplayer = 50; //cheryl: edit, ini belum di panggil ye
        this.damageTocity = 1;
        this.attackRadius = 80f;
        this.isAggressive = true;

        try {
            front1 = new Texture(Gdx.files.internal("buffaloFront1.jpeg"));
            front2 = new Texture(Gdx.files.internal("buffaloFront2.jpeg"));
            right1 = new Texture(Gdx.files.internal("buffaloRight1.jpeg"));
            right2 = new Texture(Gdx.files.internal("buffaloRight2.jpeg"));
            left1 = new Texture(Gdx.files.internal("buffaloLeft1.jpeg"));
            left2 = new Texture(Gdx.files.internal("buffaloLeft2.jpeg"));
            currentTexture = front1;
        } catch (Exception e) {
            front1 = front2 = right1 = right2 = left1 = left2 = new Texture("placeholder.png");
            currentTexture = front1;
            Gdx.app.error("Buffalo", "Texture not found. Using placeholder.");
        }

        this.setSize(64, 64);
    }

    public Buffalo(float x, float y, float exitX, float exitY) {
        this(x, y);
        this.exitX = exitX;
        this.exitY = exitY;
        assignRandomExitWithWaypoint(); // hanya dipanggil di constructor ini
        Gdx.app.log("Buffalo", "Spawned at (" + x + ", " + y + ") → (" + exitX + ", " + exitY + ")");
    }

    private void assignRandomExitWithWaypoint() {
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
        if (animationTimer >= animationSpeed) {
            useAltFrame = !useAltFrame;
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

        directionX = move.x;
        directionY = move.y;

        if (isPlayerInFront(player)) {
            knockbackTimer += deltaTime;
            if (knockbackTimer >= knockbackCooldown) {
                applyKnockback(player);
                knockbackTimer = 0f;
            }
        }
    }

    private boolean isPlayerInFront(Player player) {
        float px = player.getX() + player.getWidth() / 2;
        float py = player.getY() + player.getHeight() / 2;

        float bx = x + width / 2;
        float by = y + height / 2;

        Vector2 toPlayer = new Vector2(px - bx, py - by);
        return toPlayer.len() <= attackRadius && Math.abs(toPlayer.angleDeg()) < 45;
    }

    private boolean isPlayerOnSide(Player player) {
        float px = player.getX() + player.getWidth() / 2;
        float bx = x + width / 2;
        return Math.abs(px - bx) > width * 0.3f;
    }

    private void applyKnockback(Player player) {
        Vector2 push = new Vector2(player.getX() - x, player.getY() - y).nor().scl(knockbackForce);
        player.getPosition().add(push);
        Gdx.app.log("Buffalo", "Knockback applied to player!");
    }

    @Override
    public void onHit(Player player) {
        if (isPlayerOnSide(player)) {
            health -= 10;
            Gdx.app.log("Buffalo", "Player hit from side. Health now: " + health);
            if (health <= 0) kill();
        } else {
            Gdx.app.log("Buffalo", "Player attack from front blocked (knockback).");
        }
    }

    @Override
    public boolean shouldAttackPlayer(Player player) {
        return true;
    }

    @Override
    public void render(SpriteBatch batch) {
        Texture currentFrame = front1;
        if (directionY < 0) {
            currentFrame = useAltFrame ? front2 : front1;
        } else if (directionX > 0) {
            currentFrame = useAltFrame ? right2 : right1;
        } else if (directionX < 0) {
            currentFrame = useAltFrame ? left2 : left1;
        }

        if (currentFrame != null) {
            batch.draw(currentFrame, x, y, width, height);
        }
    }

    @Override
    public void dispose() {
        front1.dispose(); front2.dispose();
        right1.dispose(); right2.dispose();
        left1.dispose(); left2.dispose();
    }

    @Override
    public void setSize(float width, float height) {
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
}
