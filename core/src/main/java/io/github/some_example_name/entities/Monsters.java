package io.github.some_example_name.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Monsters {
    protected float x, y;
    protected float width, height;
    protected int health;
    protected float speed;
    protected int damage;
    protected float attackRadius = 0f;
    protected boolean isAggressive;
    protected boolean alive = true;

    public Monsters(float x, float y) {
        this.x = x;
        this.y = y;
    }

    // Getters
    public float getX() { return x; }
    public float getY() { return y; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }
    public int getDamage() { return damage; }
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
}
