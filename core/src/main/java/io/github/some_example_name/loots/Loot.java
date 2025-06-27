package io.github.some_example_name.loots;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import io.github.some_example_name.entities.Player;

public class Loot {
    protected float x, y;
    protected Texture texture;
    protected float width = 32, height = 32;
    protected LootEffect effect;

    public Loot(float x, float y, LootEffect effect) {
        this.x = x;
        this.y = y;
        this.effect = effect;

        // Load texture berdasarkan nama efek (misal speed, hp, damage)
        String textureName = effect.getEffectName(); // kamu harus buat method getEffectName di LootEffect
        try {
            texture = new Texture(Gdx.files.internal(textureName + ".png"));
        } catch (Exception e) {
            Gdx.app.error("Loot", "Failed to load texture: " + textureName + ".png");
            texture = null;
        }
    }

    public void render(SpriteBatch batch) {
        if (texture != null) {
            batch.draw(texture, x, y, width, height);
        }
    }

    public boolean isCollectedBy(Player player) {
        return player.getBoundingRect().overlaps(getBoundingRect());
    }

    public Rectangle getBoundingRect() {
        return new Rectangle(x, y, width, height);
    }

    public LootEffect getEffect() {
        return effect;
    }

    public void dispose() {
        if (texture != null) texture.dispose();
    }
}
