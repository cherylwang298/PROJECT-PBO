package io.github.some_example_name.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;



import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Monsters {
    protected float x, y;
    protected float width, height;
    protected int health;
    protected float speed;
    protected int damageTocity;
    protected int damageToplayer;
    protected float attackRadius = 0f;
    protected boolean isAggressive;
    protected boolean alive = true;

    //cheryl edit: bikin healthbar
    protected int maxHealth = 100;
    public static Texture pixelTexture;

    static {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 1, 1, 1); // putih
        pixmap.fill();
        pixelTexture = new Texture(pixmap);
        pixmap.dispose();
    }
//------------------------------------------------------------

    public Monsters(float x, float y) {
        this.x = x;
        this.y = y;
        this.maxHealth = health; //coba buat healthbar
    }

    // Getters
    public float getX() { return x; }
    public float getY() { return y; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }
    public int getDamageTocity() { return damageTocity; }

    public int getDamageToplayer() {
        return damageToplayer;
    }

    public float getAttackRadius() { return attackRadius; }
    public boolean isAlive() { return alive && health > 0; }

    public boolean hasReachedCity() {
        float cityBoundaryY = 760; // adjust as needed
        return y <= cityBoundaryY;
    }

    public boolean isPlayerInRange(Player player) {
        float dx = player.getX() - x;
        float dy = player.getY() - y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance <= attackRadius;
    }

    public void kill() {
        alive = false;
        health = 0;
    }

    // Abstract methods for all monster types
    public abstract void update(float deltaTime, Player player);
    public abstract void render(SpriteBatch batch);
    public abstract void onHit(Player player);
    public abstract boolean shouldAttackPlayer(Player player);
    public abstract void dispose();
    public abstract void setSize(float width, float height);

    //cheryl
    //biar bisa di kenakin sama player (ini masih on click, nanti revisi biar cuman bisa ke damage pas posisi deket)
    public void takeDamage(int amount) {
        this.health -= amount;
        if (this.health <= 0) {
            this.kill(); // atau setAlive(false)
        }
    }
    public boolean contains(float x, float y) {
        return x >= this.x && x <= this.x + this.width &&
            y >= this.y && y <= this.y + this.height;
    }


    //function buat render health barnya
    public void renderHealthBar(SpriteBatch batch) {
        if (pixelTexture == null || health <= 0) return;

        float barWidth = width;
        float barHeight = 5f;
        float hpPercent = (float) health / maxHealth;

        float xBar = x;
        float yBar = y + height + 4;

        // Merah (background)
        batch.setColor(1, 0, 0, 1);
        batch.draw(pixelTexture, xBar, yBar, barWidth, barHeight);

        // Hijau (sisa HP)
        batch.setColor(0, 1, 0, 1);
        batch.draw(pixelTexture, xBar, yBar, barWidth * hpPercent, barHeight);

        // Reset warna ke default
        batch.setColor(1, 1, 1, 1);
    }




}
