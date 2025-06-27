// === Package: io.github.some_example_name.entities ===
package io.github.some_example_name.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * The Player class represents the Gladiator character in the game.
 * It extends Scene2D's Actor to integrate with a LibGDX Stage.
 *
 * This class manages the player's core attributes (HP, Speed, Damage),
 * its position and collision, handles movement input, basic attack actions,
 * taking damage, and dynamically changes its sprite based on direction
 * and attack state.
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
    private Texture attackUpTexture; // This will now load 'attackUpswordUp.png'
    private Texture attackDownTexture;
    private Texture attackLeftTexture;
    private Texture attackRightTexture;

    // Currently active texture to draw
    private Texture currentActiveTexture;
    // Stores the last non-attacking texture, useful for reverting after attack
    private Texture lastMovementTexture;

    // --- Animation & Combat States ---
    private boolean facingRight; // True if player is facing right, false if facing left
    private boolean isAttacking;
    private float attackDuration;
    private float attackCooldown;
    private float attackTimer; // Used for both attack duration and cooldown
    private AttackDirection lastAttackDirection; // To remember which attack animation to show

    // Enum to clearly define attack directions for animation purposes
    public enum AttackDirection {
        UP, DOWN, LEFT, RIGHT, NONE
    }

    /**
     * Constructor for the Player Actor.
     * Initializes player stats, position, and loads all individual sprite textures,
     * including those for attack animations.
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

        // --- Load Movement Textures ---
        this.defaultTexture = new Texture("gladiator_default.png"); // Assuming you have a default idle texture
        this.upTexture = new Texture("gladiator_up.png");
        this.downTexture = new Texture("gladiator_down.png");
        this.leftTexture = new Texture("gladiator_left.png");
        this.rightTexture = new Texture("gladiator_right.png");

        // --- Load Attack Textures (Corrected attackUpTexture loading) ---
        this.attackUpTexture = new Texture("attackUpswordUp.png"); // Changed from attackUpswordUp.png
        this.attackDownTexture = new Texture("attackDown.png"); // This remains correct
        this.attackLeftTexture = new Texture("attackLeft.png");
        this.attackRightTexture = new Texture("attackRight.png");

        // Set initial active texture to default
        this.currentActiveTexture = this.defaultTexture;
        this.lastMovementTexture = this.defaultTexture; // Initialize last movement texture
        facingRight = true; // Default facing direction
        lastAttackDirection = AttackDirection.NONE;

        // Set the Actor's dimensions using the default texture's size.
        setWidth(defaultTexture.getWidth());
        setHeight(defaultTexture.getHeight());

        // Set the Actor's initial position.
        setPosition(startX, startY);

        // Initialize player's main collision bounds using Actor's position and dimensions
        this.bounds = new Rectangle(getX(), getY(), getWidth(), getHeight());

        // Combat related initialization
        this.isAttacking = false;
        this.attackDuration = 0.2f; // How long the attack animation plays
        this.attackCooldown = 0.5f; // How long until the player can attack again after finishing an attack
        this.attackTimer = attackCooldown; // Start ready to attack
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
        super.act(delta); // IMPORTANT: Always call the superclass's act method.

        float moveAmount = speed * delta;
        boolean movedThisFrame = false; // Flag to check if any movement occurred
        Texture intendedMovementTexture = defaultTexture; // Default if no movement

        // Handle player movement based on WASD keyboard input.
        // We capture the intended movement texture first.
        if (Gdx.input.isKeyPressed(Input.Keys.W)) { // Move Up
            position.y += moveAmount;
            intendedMovementTexture = upTexture;
            movedThisFrame = true;
            // Update lastAttackDirection based on movement
            if (!isAttacking) this.lastAttackDirection = AttackDirection.UP;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) { // Move Down
            position.y -= moveAmount;
            intendedMovementTexture = downTexture;
            movedThisFrame = true;
            // Update lastAttackDirection based on movement
            if (!isAttacking) this.lastAttackDirection = AttackDirection.DOWN;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) { // Move Left
            position.x -= moveAmount;
            intendedMovementTexture = leftTexture;
            facingRight = false; // Player faces left
            movedThisFrame = true;
            // Update lastAttackDirection based on movement
            if (!isAttacking) this.lastAttackDirection = AttackDirection.LEFT;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) { // Move Right
            position.x += moveAmount;
            intendedMovementTexture = rightTexture;
            facingRight = true; // Player faces right
            movedThisFrame = true;
            // Update lastAttackDirection based on movement
            if (!isAttacking) this.lastAttackDirection = AttackDirection.RIGHT;
        }

        // Clamp position to within arena bounds
        float clampedX = Math.min(Math.max(position.x, minX), maxX - getWidth());
        float clampedY = Math.min(Math.max(position.y, minY), maxY - getHeight());
        position.set(clampedX, clampedY);
        setPosition(clampedX, clampedY); // Synchronize Actor's position
        bounds.setPosition(getX(), getY()); // Update collision bounds

        // Manage the attack state and animation
        if (isAttacking) {
            attackTimer += delta;
            if (attackTimer >= attackDuration) {
                isAttacking = false; // Attack animation duration over
                attackTimer = 0f; // Reset timer for cooldown
                // Revert to the last movement texture or default after attack
                currentActiveTexture = lastMovementTexture;
            } else {
                // If attacking, set the attack animation texture based on determined lastAttackDirection
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
                        // Fallback, if lastAttackDirection is NONE, use facing direction for attack
                        currentActiveTexture = facingRight ? attackRightTexture : attackLeftTexture;
                        break;
                }
            }
        } else {
            // If not attacking, advance the cooldown timer
            if (attackTimer < attackCooldown) {
                attackTimer += delta;
            }

            // Update current texture based on movement only if not attacking
            if (!isAttacking) {
                if (movedThisFrame) {
                    currentActiveTexture = intendedMovementTexture;
                    lastMovementTexture = intendedMovementTexture; // Store for later reversion
                } else {
                    // If not moving, and not attacking, revert to default idle texture
                    currentActiveTexture = defaultTexture;
                    lastMovementTexture = defaultTexture; // Store for later reversion
                }
            }
        }
    }

    /**
     * Initiates the player's attack action.
     * This method sets the internal `isAttacking` flag and resets the `attackTimer`.
     * It relies on `lastAttackDirection` being set externally (e.g., by GameScreen
     * based on mouse or default keyboard direction) for correct animation/hitbox.
     */
    public void attack() {
        // Only allow attack if not currently attacking and cooldown is ready
        if (!isAttacking && attackTimer >= attackCooldown) {
            isAttacking = true;
            attackTimer = 0f; // Start duration timer
            Gdx.app.log("Player", "Gladiator initiates attack animation.");
            // The currentActiveTexture will be set in act(delta) based on lastAttackDirection
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
            // TODO: Implement game over logic here (likely handled by GameRoundManager or GameScreen).
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
    public void draw(Batch batch, float parentAlpha) {
        // Draw the 'currentActiveTexture' at the Actor's position and dimensions.
        batch.draw(currentActiveTexture, getX(), getY(), getWidth(), getHeight());
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
        if (attackUpTexture != null) attackUpTexture.dispose();
        if (attackDownTexture != null) attackDownTexture.dispose();
        if (attackLeftTexture != null) attackLeftTexture.dispose();
        if (attackRightTexture != null) attackRightTexture.dispose();
    }

    // --- Getter Methods ---
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
        return this.damage;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    /**
     * Returns true if the player is currently facing right, false otherwise.
     * This is useful for determining default attack direction when no movement input is given.
     */
    public boolean isFacingRight() { // Make this method public
        return facingRight;
    }

    /**
     * Sets the intended direction for the player's next attack.
     * This method is called externally (e.g., from GameScreen)
     * to ensure the attack animation and hitbox align with player intent.
     * @param direction The AttackDirection for the upcoming attack.
     */
    public void setAttackDirection(AttackDirection direction) { // New public method
        this.lastAttackDirection = direction;
    }


    /**
     * Returns the rectangle representing the player's attack range.
     * This method is used by GameRoundManager to determine if monsters are hit.
     * The dimensions and position of this rectangle should align with the visual attack.
     */
    public Rectangle getAttackRect() {
        float attackRange = 80f; // Base range for the attack's reach

        // Player's current position and dimensions
        float px = getX();
        float py = getY();
        float pWidth = getWidth();
        float pHeight = getHeight();

        // Calculate the attack rectangle based on the stored lastAttackDirection
        switch (lastAttackDirection) {
            case UP:
                // Attack range extends upwards from player's top edge
                return new Rectangle(px, py + pHeight, pWidth, attackRange);
            case DOWN:
                // Attack range extends downwards from player's bottom edge
                return new Rectangle(px, py - attackRange, pWidth, attackRange);
            case LEFT:
                // Attack range extends left from player's left edge
                return new Rectangle(px - attackRange, py, attackRange, pHeight);
            case RIGHT:
                // Attack range extends right from player's right edge
                return new Rectangle(px + pWidth, py, attackRange, pHeight);
            default:
                // Fallback: If no specific attack direction is set (e.g., from movement),
                // default to a forward attack based on facing direction.
                if (facingRight) {
                    return new Rectangle(px + pWidth, py, attackRange, pHeight);
                } else {
                    return new Rectangle(px - attackRange, py, attackRange, pHeight);
                }
        }
    }


    public Rectangle getBoundingRect() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    // --- Setter Methods ---
    public void setDamage(float damage) {
        this.damage = damage;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setMaxHp(float maxHp) {
        this.maxHp = maxHp;
    }

    public void setHp(float hp) {
        this.hp = hp;
    }
}
