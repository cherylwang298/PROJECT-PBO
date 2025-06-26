package io.github.some_example_name.loots;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import io.github.some_example_name.entities.Player;

public class Loot {
    private float x, y;
    private Texture texture;
    private String type; // contoh: "gold", "potion"
    private int value;   // contoh: jumlah gold atau jumlah heal

    private float width = 32, height = 32;

    public Loot(float x, float y, String type, int value) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.value = value;
        this.texture = new Texture(Gdx.files.internal(type + ".png")); // contoh: gold.png, potion.png
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height);
    }

    public boolean isCollectedBy(Player player) {
        return player.getBoundingRect().overlaps(getBoundingRect());
    }

    public Rectangle getBoundingRect() {
        return new Rectangle(x, y, width, height);
    }

    public String getType() { return type; }
    public int getValue() { return value; }

    public void dispose() {
        texture.dispose();
    }
}
