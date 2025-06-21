// === Package: io.github.some_example_name.entities ===
package io.github.some_example_name.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch; // IMPORTANT: Changed from SpriteBatch to Batch for Actor's draw method
import com.badlogic.gdx.scenes.scene2d.Actor; // IMPORTANT: New import for Actor base class
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * The Player class represents the Gladiator character in the game.
 * IT NOW EXTENDS SCENE2D'S ACTOR. This allows it to be managed and rendered
 * directly by a LibGDX Stage, which is used in your GameScreen.
 *
 * This class manages the player's core attributes (HP, Speed, Damage),
 * its position and collision, handles movement input, basic attack actions,
 * taking damage, and dynamically changes its sprite based on direction.
 */
public class Player extends Actor { // <--- KEY CHANGE: Player now extends Actor

    // --- Player Core Properties ---
    private float hp;
    private float maxHp;
    private float speed;
    private float damage;

    // --- Player Position & Collision ---
    // We use a private Vector2 for internal position calculations, then synchronize
    // it with the Actor's built-in position (getX(), getY(), setPosition())
    private Vector2 position;
    private Rectangle bounds; // Collision box for the player

    private float minX = 0f, maxX = Gdx.graphics.getWidth();
    private float minY = 0f, maxY = Gdx.graphics.getHeight();

    // --- Graphics: Individual Textures for Directions ---
    // These are the "links" to your pictures!
    private Texture defaultTexture; // For idle, or when not explicitly moving
    private Texture upTexture;
    private Texture downTexture;
    private Texture leftTexture;
    private Texture rightTexture;

    // The currently active texture to draw
    private Texture currentActiveTexture;

    // --- Animation & Combat (keeping the structure for future use, but simplified now) ---
    // For now, we won't have animations like walk cycles from a sheet,
    // but just switch textures based on direction.
    private boolean facingRight; // True if player is facing right, false if facing left

    // Combat related
    private boolean isAttacking;
    private Rectangle attackHitbox;
    private float attackDuration;
    private float attackCooldown;
    private float attackTimer;

    /**
     * Constructor for the Player Actor.
     * Initializes player stats, position, and loads all individual directional sprite textures.
     *
     * @param initialHp The player's starting and maximum health points.
     * @param initialSpeed The player's initial movement speed.
     * @param initialDamage The player's initial attack damage.
     * @param startX The initial X-coordinate of the player's position.
     * @param startY The initial Y-coordinate of the player's position.
     */
    public Player(float initialHp, float initialSpeed, float initialDamage, float startX, float startY) {
        // Initialize player stats
        this.maxHp = initialHp;
        this.hp = initialHp;
        this.speed = initialSpeed;
        this.damage = initialDamage;

        // Initialize internal position Vector2
        this.position = new Vector2(startX, startY);

        // --- Load Individual Textures (THIS IS WHERE YOU LINK THE PICTURES) ---
        // Make sure these file names exactly match what's in your 'core/assets' folder!
        this.defaultTexture = new Texture("gladiator_default.png");
        this.upTexture = new Texture("gladiator_up.png");
        this.downTexture = new Texture("gladiator_down.png");
        this.leftTexture = new Texture("gladiator_left.png");
        this.rightTexture = new Texture("gladiator_right.png");

        // Set initial active texture to default
        this.currentActiveTexture = this.defaultTexture;
        facingRight = true; // Default facing direction

        // Set the Actor's dimensions using the default texture's size.
        // These methods are inherited from Actor and are crucial for Stage management.
        setWidth(defaultTexture.getWidth());  // <--- Using Actor's setWidth
        setHeight(defaultTexture.getHeight());// <--- Using Actor's setHeight

        // Set the Actor's initial position.
        // This is where Stage will draw the actor.
        setPosition(startX, startY); // <--- Using Actor's setPosition

        // Initialize player's main collision bounds using Actor's position and dimensions
        this.bounds = new Rectangle(getX(), getY(), getWidth(), getHeight()); // <--- Using Actor's getX/Y/Width/Height

        // Combat related initialization
        this.isAttacking = false;
        this.attackHitbox = new Rectangle(0, 0, 0, 0);
        this.attackDuration = 0.2f;
        this.attackCooldown = 0.5f;
        this.attackTimer = attackCooldown;
    }

    public void setMovementBounds(float minX, float maxX, float minY, float maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    /**
     * Overrides Actor's `act` method. This method is called automatically by the Stage
     * every frame, serving as the update loop for the Player's logic.
     *
     * @param delta The time elapsed since the last frame.
     */
    @Override
    public void act(float delta) {
        super.act(delta); // IMPORTANT: Always call the superclass's act method for Actor's built-in functionality.

        float moveAmount = speed * delta;
        boolean movedThisFrame = false; // Flag to check if any movement occurred

        // Handle player movement based on WASD keyboard input.
        // We use Gdx.input directly here for responsiveness.
        if (Gdx.input.isKeyPressed(Input.Keys.W)) { // Move Up
            position.y += moveAmount;
            currentActiveTexture = upTexture;
            movedThisFrame = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) { // Move Down
            position.y -= moveAmount;
            currentActiveTexture = downTexture;
            movedThisFrame = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) { // Move Left
            position.x -= moveAmount;
            currentActiveTexture = leftTexture;
            facingRight = false; // Player faces left
            movedThisFrame = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) { // Move Right
            position.x += moveAmount;
            currentActiveTexture = rightTexture;
            facingRight = true; // Player faces right
            movedThisFrame = true;
        }

        // If no movement keys are pressed and not attacking, revert to the default/idle texture.
        if (!movedThisFrame && !isAttacking) {
            currentActiveTexture = defaultTexture;
        }

        //        setPosition(position.x, position.y); // <--- Using Actor's setPosition

        // Clamp posisi ke dalam batas arena
        float clampedX = Math.min(Math.max(position.x, minX), maxX - getWidth());
        float clampedY = Math.min(Math.max(position.y, minY), maxY - getHeight());
        position.set(clampedX, clampedY);
        setPosition(clampedX, clampedY);

        // Always update the player's collision bounds after changing position
        bounds.setPosition(getX(), getY()); // <--- Using Actor's getX/getY

        // Handle player attack input (e.g., Spacebar)
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            attack();
        }

        // Manage the attack state and cooldown
        if (isAttacking) {
            attackTimer += delta;
            if (attackTimer >= attackDuration) {
                isAttacking = false;
                attackHitbox.setSize(0, 0); // Hide hitbox after attack duration
            }
        } else {
            // If not attacking, continue advancing the timer for cooldown
            if (attackTimer < attackCooldown) {
                attackTimer += delta;
            }
        }
    }

    /**
     * Initiates the player's attack action.
     * This method checks for cooldown and activates the attack hitbox.
     */
    public void attack() {
        if (!isAttacking && attackTimer >= attackCooldown) {
            isAttacking = true;
            attackTimer = 0f; // Reset timer for new attack's duration and cooldown
            Gdx.app.log("Player", "Gladiator initiates attack.");

            // Set up placeholder attack hitbox relative to Actor's position and size.
            // Adjust these values to visually match your gladiator's weapon reach.
            float hitboxX;
            float hitboxWidth = getWidth() * 0.7f; // Example: 70% of player width
            float hitboxHeight = getHeight() * 0.8f; // Example: 80% of player height

            if (facingRight) {
                hitboxX = getX() + getWidth() * 0.8f; // To the right of the player
            } else {
                hitboxX = getX() - hitboxWidth * 0.1f; // To the left of the player, slightly overlapping
            }
            attackHitbox.set(hitboxX, getY() + getHeight() * 0.1f, hitboxWidth, hitboxHeight); // <--- Using Actor's getX/Y/Width/Height
        }
    }

    /**
     * Reduces the player's current health points.
     *
     * @param damageAmount The amount of health to subtract from the player.
     */
    public void takeDamage(float damageAmount) {
        this.hp -= damageAmount;
        if (this.hp < 0) {
            this.hp = 0;
        }
        Gdx.app.log("Player", "Gladiator took " + damageAmount + " damage. Current HP: " + hp);
        if (this.hp <= 0) {
            Gdx.app.log("Player", "Gladiator has fallen! Game Over.");
            // TODO: Implement game over logic here.
        }
    }

    /**
     * Overrides Actor's `draw` method. This method is called automatically by the Stage
     * to render the Actor on the screen.
     *
     * @param batch The `Batch` used for rendering (typically `SpriteBatch`).
     * @param parentAlpha The alpha (transparency) value inherited from parent Actors.
     */
    @Override
    public void draw(Batch batch, float parentAlpha) { // <--- KEY CHANGE: draw method signature for Actor
        // Draw the 'currentActiveTexture' at the Actor's position and dimensions.
        // getX(), getY(), getWidth(), getHeight() are inherited methods from Actor.
        // No need to manually flip here since you have separate left/right textures.
        batch.draw(currentActiveTexture, getX(), getY(), getWidth(), getHeight()); // <--- Using Actor's getX/Y/Width/Height
    }

    /**
     * Disposes of all loaded textures to free up memory.
     * This method should be called when the Player object is no longer needed
     * (e.g., in the `dispose()` method of the `GameScreen`).
     */
    public void dispose() {
        if (defaultTexture != null) defaultTexture.dispose();
        if (upTexture != null) upTexture.dispose();
        if (downTexture != null) downTexture.dispose();
        if (leftTexture != null) leftTexture.dispose();
        if (rightTexture != null) rightTexture.dispose();
    }

    // --- Getter Methods (to allow other classes to access player's properties) ---
    // Note: Actor already provides getX(), getY(), getWidth(), getHeight()
    public float getHp() {
        return hp;
    }

    public float getMaxHp() {
        return maxHp;
    }

    public float getSpeed() {
        return speed;
    }

    public float getDamage() {
        return damage;
    }

    // This getter returns the internal position Vector2.
    // Use getX()/getY() for the Actor's actual position on stage.
    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public Rectangle getAttackHitbox() {
        return attackHitbox;
    }

    public boolean isAttacking() {
        return isAttacking;
    }
}
