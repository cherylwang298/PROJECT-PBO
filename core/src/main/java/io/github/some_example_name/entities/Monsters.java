package io.github.some_example_name.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Monsters {
    protected float x, y;
    protected int health;
    protected float speed;
    protected int damage;
    protected boolean isAggressive;

    public Monsters(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public abstract void update(float deltaTime);
    public abstract void render(SpriteBatch batch);
    public abstract void onHit(Player player);
    public abstract boolean shouldAttackPlayer(Player player);
}
