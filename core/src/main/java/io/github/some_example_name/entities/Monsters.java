//package io.github.some_example_name.entities;
//
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//
//public abstract class Monsters {
//    protected float x, y;
//    protected int health;
//    protected float speed;
//    protected int damage;
//    protected boolean isAggressive;
//
//    public Monsters(float x, float y) {
//        this.x = x;
//        this.y = y;
//    }
//
//    public abstract void update(float deltaTime);
//    public abstract void render(SpriteBatch batch);
//    public abstract void onHit(Player player);
//    public abstract boolean shouldAttackPlayer(Player player);
//}


package io.github.some_example_name.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Monsters {
    protected float x, y;
    protected float width, height;  // <-- Tambahan ini!
    protected int health;
    protected float speed;
    protected int damage;
    protected boolean isAggressive;

    public Monsters(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public abstract void update(float deltaTime, Player player);
    public abstract void render(SpriteBatch batch);
    public abstract void onHit(Player player);
    public abstract boolean shouldAttackPlayer(Player player);
    public abstract void dispose();

    public float getWidth() { return width; }
    public float getHeight() { return height; }
    public float getX() { return x; }
    public float getY() { return y; }
    public boolean isAlive() { return health > 0; }

    public abstract void setSize(float width, float height);
}
