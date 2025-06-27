package io.github.some_example_name.loots;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import io.github.some_example_name.entities.Player;
import com.badlogic.gdx.audio.Sound; // Still needed for the parameter type

public class Loot {
    protected float x, y;
    protected Texture texture;
    protected float width = 32, height = 32;
    protected LootEffect effect;
    private boolean collected = false;

    // NO LONGER OWNED BY LOOT INSTANCE: private Sound collectSound;
    private final Sound sharedCollectSound; // This sound is now passed in and shared

    public Loot(float x, float y, LootEffect effect, Sound sharedCollectSound) { // Added Sound parameter
        this.x = x;
        this.y = y;
        this.effect = effect;
        this.sharedCollectSound = sharedCollectSound; // Assign the shared sound

        String textureFileName = effect.getEffectName() + ".png";
        try {
            texture = new Texture(Gdx.files.internal(textureFileName));
        } catch (Exception e) {
            Gdx.app.error("Loot", "Failed to load texture for " + textureFileName + ": " + e.getMessage());
            texture = null;
        }

        // NO LONGER LOADING SOUND HERE. It's loaded by LootManager.
    }

    public void render(SpriteBatch batch) {
        if (!collected && texture != null) {
            batch.draw(texture, x, y, width, height);
        }
    }

    public boolean checkCollision(Player player) {
        return !collected && player.getBoundingRect().overlaps(getBoundingRect());
    }

    /**
     * Applies the effect of this loot item to the player and plays the collection sound.
     */
    public void applyEffectToPlayer(Player player) {
        if (!collected) {
            effect.apply(player);
            this.collected = true;

            // Play the shared sound effect!
            if (sharedCollectSound != null) {
                sharedCollectSound.play(0.8f); // Play with 80% volume. Adjust as needed.
                Gdx.app.log("Loot", "Loot collected sound played (sharedCollectSound was NOT null).");
            } else {
                Gdx.app.error("Loot", "Attempted to play loot sound, but sharedCollectSound is null. Check LootManager loading.");
            }
            Gdx.app.log("Loot", "Loot of type " + effect.getEffectName() + " collected (effect applied).");
        }
    }

    public boolean isCollected() {
        return collected;
    }

    public Rectangle getBoundingRect() {
        return new Rectangle(x, y, width, height);
    }

    public LootEffect getEffect() {
        return effect;
    }

    public void dispose() {
        if (texture != null) {
            texture.dispose();
            texture = null;
            Gdx.app.log("Loot", "Loot texture disposed.");
        }
        // NO LONGER DISPOSING COLLECT SOUND HERE. It's disposed by LootManager.
    }
}
