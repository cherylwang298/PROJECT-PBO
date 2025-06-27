// === Package: io.github.some_example_name.entities ===
package io.github.some_example_name.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.audio.Sound;

/**
 * The Player class represents the Gladiator character in the game.
 * It extends Scene2D's Actor to integrate with a LibGDX Stage.
 *
 * This class manages the player's core attributes (HP, Speed, Damage),
 * its position and collision, handles movement input, basic attack actions,
 * taking damage, and dynamically changes its sprite based on direction
 * and attack state. It now also plays sound effects for attack and taking damage.
 */
public class Player extends Actor {

    // --- Player Core Properties ---
    private float hp;
    private float maxHp;
    private float speed;
    private float damage;

    // --- Player Position & Collision ---
    private Vector2 position;
    private Rectangle bounds;

    private float minX = 0f, maxX = Gdx.graphics.getWidth();
    private float minY = 0f, maxY = Gdx.graphics.getHeight();

    // --- Graphics: Textures for Movement and Attack Animations ---
    private Texture defaultTexture;
    private Texture upTexture;
    private Texture downTexture;
    private Texture leftTexture;
    private Texture rightTexture;

    // New Textures for Attack Animations
    private Texture attackUpTexture;
    private Texture attackDownTexture;
    private Texture attackLeftTexture;
    private Texture attackRightTexture;

    // Currently active texture to draw
    private Texture currentActiveTexture;
    // Stores the last non-attacking texture, useful for reverting after attack
    private Texture lastMovementTexture;

    // --- Animation & Combat States ---
    private boolean facingRight;
    private boolean isAttacking;
    private float attackDuration;
    private float attackCooldown;
    private float attackTimer;
    private AttackDirection lastAttackDirection;

    // Define a consistent size for the attack hitbox
    private final float ATTACK_BOX_SIZE = 120f;

    // Sound effects
    private Sound attackSound;
    private Sound hitSound;

    public enum AttackDirection {
        UP, DOWN, LEFT, RIGHT, NONE
    }

    /**
     * Constructor for the Player Actor.
     * Initializes player stats, position, loads all sprites, and loads sound effects.
     */
    public Player(float initialHp, float initialSpeed, float initialDamage, float startX, float startY) {
        this.maxHp = initialHp;
        this.hp = initialHp;
        this.speed = initialSpeed;
        this.damage = initialDamage;
        this.position = new Vector2(startX, startY);

        // --- Load Movement Textures ---
        this.defaultTexture = new Texture("gladiator_default.png");
        this.upTexture = new Texture("gladiator_up.png");
        this.downTexture = new Texture("gladiator_down.png");
        this.leftTexture = new Texture("gladiator_left.png");
        this.rightTexture = new Texture("gladiator_right.png");

        // --- Load Attack Textures ---
        this.attackUpTexture = new Texture("attackUpswordUp.png");
        this.attackDownTexture = new Texture("attackDown.png");
        this.attackLeftTexture = new Texture("attackLeft.png");
        this.attackRightTexture = new Texture("attackRight.png");

        this.currentActiveTexture = this.defaultTexture;
        this.lastMovementTexture = this.defaultTexture;
        facingRight = true;
        lastAttackDirection = AttackDirection.NONE;

        setWidth(defaultTexture.getWidth());
        setHeight(defaultTexture.getHeight());
        setPosition(startX, startY);
        this.bounds = new Rectangle(getX(), getY(), getWidth(), getHeight());

        this.isAttacking = false;
        this.attackDuration = 0.2f;
        this.attackCooldown = 0.5f;
        this.attackTimer = attackCooldown;

        // IMPORTANT: Load attack sound using the correct path (assuming directly in assets)
        try {
            attackSound = Gdx.audio.newSound(Gdx.files.internal("Sword swing 1 (audio).MP3"));
            Gdx.app.log("Player", "Attack sound loaded successfully from assets root.");
        } catch (Exception e) {
            Gdx.app.error("Player", "Failed to load attack sound: " + e.getMessage());
            attackSound = null;
        }

        // IMPORTANT: Load hit sound using the new, corrected path (assuming directly in assets)
        try {
            hitSound = Gdx.audio.newSound(Gdx.files.internal("Oof (audio)(1).MP3"));
            Gdx.app.log("Player", "Hit sound loaded successfully from assets root.");
        } catch (Exception e) {
            Gdx.app.error("Player", "Failed to load hit sound: " + e.getMessage());
            hitSound = null;
        }
    }

    public void setMovementBounds(float minX, float maxX, float minY, float maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        float moveAmount = speed * delta;
        boolean movedThisFrame = false;
        Texture intendedMovementTexture = defaultTexture;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            position.y += moveAmount;
            intendedMovementTexture = upTexture;
            movedThisFrame = true;
            if (!isAttacking) this.lastAttackDirection = AttackDirection.UP;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            position.y -= moveAmount;
            intendedMovementTexture = downTexture;
            movedThisFrame = true;
            if (!isAttacking) this.lastAttackDirection = AttackDirection.DOWN;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            position.x -= moveAmount;
            intendedMovementTexture = leftTexture;
            facingRight = false;
            movedThisFrame = true;
            if (!isAttacking) this.lastAttackDirection = AttackDirection.LEFT;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            position.x += moveAmount;
            intendedMovementTexture = rightTexture;
            facingRight = true;
            movedThisFrame = true;
            if (!isAttacking) this.lastAttackDirection = AttackDirection.RIGHT;
        }

        float clampedX = Math.min(Math.max(position.x, minX), maxX - getWidth());
        float clampedY = Math.min(Math.max(position.y, minY), maxY - getHeight());
        position.set(clampedX, clampedY);
        setPosition(clampedX, clampedY);
        bounds.setPosition(getX(), getY());

        if (isAttacking) {
            attackTimer += delta;
            if (attackTimer >= attackDuration) {
                isAttacking = false;
                attackTimer = 0f;
                currentActiveTexture = lastMovementTexture;
            } else {
                switch (lastAttackDirection) {
                    case UP:
                        currentActiveTexture = attackUpTexture;
                        break;
                    case DOWN:
                        currentActiveTexture = attackDownTexture;
                        break;
                    case LEFT:
                        currentActiveTexture = attackLeftTexture;
                        break;
                    case RIGHT:
                        currentActiveTexture = attackRightTexture;
                        break;
                    default:
                        currentActiveTexture = facingRight ? attackRightTexture : attackLeftTexture;
                        break;
                }
            }
        } else {
            if (attackTimer < attackCooldown) {
                attackTimer += delta;
            }
            if (!isAttacking) {
                if (movedThisFrame) {
                    currentActiveTexture = intendedMovementTexture;
                    lastMovementTexture = intendedMovementTexture;
                } else {
                    currentActiveTexture = defaultTexture;
                    lastMovementTexture = defaultTexture;
                }
            }
        }
    }

    /**
     * Initiates the player's attack action.
     * Plays the attack sound if ready.
     */
    public void attack() {
        if (!isAttacking && attackTimer >= attackCooldown) {
            isAttacking = true;
            attackTimer = 0f;
            Gdx.app.log("Player", "Gladiator initiates attack animation.");

            if (attackSound != null) {
                attackSound.play(0.5f);
                Gdx.app.log("Player", "Attack sound played.");
            }
        }
    }


    /**
     * Reduces the player's current health points.
     * Plays a hit sound when damage is taken.
     *
     * @param damageAmount The amount of health to subtract from the player.
     */
    public void takeDamage(float damageAmount) {
        this.hp -= damageAmount;
        if (this.hp < 0) {
            this.hp = 0;
        }
        Gdx.app.log("Player", "Gladiator took " + damageAmount + " damage. Current HP: " + hp);

        if (hitSound != null) {
            hitSound.play(0.7f);
            Gdx.app.log("Player", "Player hit sound played.");
        }

        if (this.hp <= 0) {
            Gdx.app.log("Player", "Gladiator has fallen! Game Over.");
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(currentActiveTexture, getX(), getY(), getWidth(), getHeight());
    }

    /**
     * Disposes of all loaded textures and sounds to free up memory.
     * This method should be called when the Player object is no longer needed.
     */
    public void dispose() {
        if (defaultTexture != null) defaultTexture.dispose();
        if (upTexture != null) upTexture.dispose();
        if (downTexture != null) downTexture.dispose();
        if (leftTexture != null) leftTexture.dispose();
        if (rightTexture != null) rightTexture.dispose();
        if (attackUpTexture != null) attackUpTexture.dispose();
        if (attackDownTexture != null) attackDownTexture.dispose();
        if (attackLeftTexture != null) attackLeftTexture.dispose();
        if (attackRightTexture != null) attackRightTexture.dispose();

        if (attackSound != null) {
            attackSound.dispose();
            Gdx.app.log("Player", "Attack sound disposed.");
        }
        if (hitSound != null) {
            hitSound.dispose();
            Gdx.app.log("Player", "Hit sound disposed.");
        }
    }

    public float getHp() { return hp; }
    public float getMaxHp() { return maxHp; }
    public float getSpeed() { return speed; }
    public float getDamage() { return this.damage; }
    public Vector2 getPosition() { return position; }
    public Rectangle getBounds() { return bounds; }
    public boolean isAttacking() { return isAttacking; }
    public boolean isFacingRight() { return facingRight; }
    public void setAttackDirection(AttackDirection direction) { this.lastAttackDirection = direction; }

    public Rectangle getAttackRect() {
        float playerCenterX = getX() + getWidth() / 2;
        float playerCenterY = getY() + getHeight() / 2;
        float attackBoxHalfSize = ATTACK_BOX_SIZE / 2;

        switch (lastAttackDirection) {
            case UP:
                return new Rectangle(playerCenterX - attackBoxHalfSize, playerCenterY + getHeight() / 2, ATTACK_BOX_SIZE, ATTACK_BOX_SIZE);
            case DOWN:
                return new Rectangle(playerCenterX - attackBoxHalfSize, playerCenterY - getHeight() / 2 - ATTACK_BOX_SIZE, ATTACK_BOX_SIZE, ATTACK_BOX_SIZE);
            case LEFT:
                return new Rectangle(playerCenterX - getWidth() / 2 - ATTACK_BOX_SIZE, playerCenterY - attackBoxHalfSize, ATTACK_BOX_SIZE, ATTACK_BOX_SIZE);
            case RIGHT:
                return new Rectangle(playerCenterX + getWidth() / 2, playerCenterY - attackBoxHalfSize, ATTACK_BOX_SIZE, ATTACK_BOX_SIZE);
            default:
                if (facingRight) {
                    return new Rectangle(playerCenterX + getWidth() / 2, playerCenterY - attackBoxHalfSize, ATTACK_BOX_SIZE, ATTACK_BOX_SIZE);
                } else {
                    return new Rectangle(playerCenterX - getWidth() / 2 - ATTACK_BOX_SIZE, playerCenterY - attackBoxHalfSize, ATTACK_BOX_SIZE, ATTACK_BOX_SIZE);
                }
        }
    }

    public Rectangle getBoundingRect() { return new Rectangle(getX(), getY(), getWidth(), getHeight()); }
    public void setDamage(float damage) { this.damage = damage; }
    public void setSpeed(float speed) { this.speed = speed; }
    public void setMaxHp(float maxHp) { this.maxHp = maxHp; }
    public void setHp(float hp) { this.hp = hp; }
}
