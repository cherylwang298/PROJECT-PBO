//package io.github.some_example_name.entities;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.math.Vector2;
//
//public class Goblin extends Monsters {
//
//    private Texture goblinFront, goblinBack, goblinLeft, goblinRight;
//    private Texture currentTexture;
//
//    private float textureWidth, textureHeight;
//    private float exitX, exitY;
//
//    private boolean escaped = false;
//    private boolean pendingRemove = false;
//    private float escapeTimer = 0f;
//
//    private boolean aggro = false;  // special behavior: only attacks if attacked
//
//    public Goblin(float x, float y) {
//        super(x, y);
//
//        this.health = 50;
//        this.speed = 70.0f;
//        this.Damage = 5; //aku ganti dari damage -> Damage (cheryl)
//        this.isAggressive = false;  // by default goblin is passive
//
//        try {
//            goblinFront = new Texture(Gdx.files.internal("goblinFront.png"));
//            goblinBack = new Texture(Gdx.files.internal("goblinBack.png"));
//            goblinLeft = new Texture(Gdx.files.internal("goblinLeft.png"));
//            goblinRight = new Texture(Gdx.files.internal("goblinRight.png"));
//            currentTexture = goblinFront;
//
//            textureWidth = goblinFront.getWidth();
//            textureHeight = goblinFront.getHeight();
//        } catch (Exception e) {
//            Gdx.app.error("Goblin", "Failed to load goblin textures: " + e.getMessage());
//            goblinFront = goblinBack = goblinLeft = goblinRight = new Texture("placeholder.png");
//            currentTexture = goblinFront;
//            textureWidth = textureHeight = 32;
//        }
//
//        this.setSize(64, 64);
//        assignRandomExit();
//    }
//
//    private void assignRandomExit() {
//        int exitIndex = (int)(Math.random() * 3);
//
//        float screenWidth = Gdx.graphics.getWidth();
//        float screenHeight = Gdx.graphics.getHeight();
//
//        switch (exitIndex) {
//            case 0:
//                exitX = screenWidth / 2f;
//                exitY = 0;
//                break;
//            case 1:
//                exitX = screenWidth * 0.22f;
//                exitY = screenHeight * 0.86f;
//                break;
//            case 2:
//                exitX = screenWidth * 0.77f;
//                exitY = screenHeight * 0.86f;
//                break;
//        }
//    }
//
//    @Override
//    public void update(float deltaTime, Player player) {
//        float oldX = x, oldY = y;
//
//        if (!aggro) {
//            // Move toward exit if not aggro
//            Vector2 direction = new Vector2(exitX - (x + width / 2), exitY - (y + height / 2));
//            if (direction.len() > 2f) {
//                direction.nor();
//                x += direction.x * speed * deltaTime;
//                y += direction.y * speed * deltaTime;
//
//                float dx = x - oldX;
//                float dy = y - oldY;
//
//                if (Math.abs(dx) > Math.abs(dy)) {
//                    currentTexture = (dx > 0) ? goblinRight : goblinLeft;
//                } else {
//                    currentTexture = (dy > 0) ? goblinBack : goblinFront;
//                }
//            }
//
//            if (!escaped && isReachedExit()) {
//                escaped = true;
//                Gdx.app.log("Goblin", "Escaped through the door!");
//                pendingRemove = true;
//            }
//        } else {
//            // If aggro, chase the player
//            Vector2 toPlayer = new Vector2(player.getX() - x, player.getY() - y);
//            if (toPlayer.len() > 2f) {
//                toPlayer.nor();
//                x += toPlayer.x * speed * deltaTime;
//                y += toPlayer.y * speed * deltaTime;
//
//                if (Math.abs(toPlayer.x) > Math.abs(toPlayer.y)) {
//                    currentTexture = (toPlayer.x > 0) ? goblinRight : goblinLeft;
//                } else {
//                    currentTexture = (toPlayer.y > 0) ? goblinBack : goblinFront;
//                }
//            }
//
//            // Tambahkan logic damage ke player kalau perlu
//            //if (distanceToPlayer(player) < 30f) {
//            //player.takeDamage(damage);
//            //}
//        }
//
//        if (pendingRemove) {
//            escapeTimer += deltaTime;
//            if (escapeTimer >= 0.1f) {
//                this.health = 0;
//            }
//        }
//    }
//
//    private boolean isReachedExit() {
//        float distance = Vector2.dst(x + width / 2, y + height / 2, exitX, exitY);
//        return distance < 20f;
//    }
//
//    @Override
//    public void onHit(Player player) {
//        health -= 10;
//        Gdx.app.log("Goblin", "Hit! Health: " + health);
//        aggro = true; // mulai agresif jika diserang
//    }
//
//    @Override
//    public boolean shouldAttackPlayer(Player player) {
//        return aggro;  // hanya menyerang kalau sudah diserang
//    }
//
//    @Override
//    public void render(SpriteBatch batch) {
//        batch.draw(currentTexture, x, y, width, height);
//    }
//
//    @Override
//    public void dispose() {
//        goblinFront.dispose();
//        goblinBack.dispose();
//        goblinLeft.dispose();
//        goblinRight.dispose();
//    }
//
//    @Override
//    public float getWidth() {
//        return width;
//    }
//
//    @Override
//    public float getHeight() {
//        return height;
//    }
//
//    @Override
//    public void setSize(float width, float height) {
//        this.width = width;
//        this.height = height;
//    }
//}


package io.github.some_example_name.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Goblin extends Monsters {

    private Texture goblinFront, goblinBack, goblinLeft, goblinRight;
    private Texture currentTexture;

    private float textureWidth, textureHeight;
    private float exitX, exitY;

    private boolean escaped = false;
    private boolean pendingRemove = false;
    private float escapeTimer = 0f;

    private boolean aggro = false;  // special behavior: only attacks if attacked

    public Goblin(float x, float y) {
        super(x, y);

        this.health = 50;
        this.speed = 70.0f;
        this.Damage = 5; //aku ganti dari damage -> Damage (cheryl)
        this.isAggressive = false;  // by default goblin is passive

        try {
            goblinFront = new Texture(Gdx.files.internal("goblinFront.png"));
            goblinBack = new Texture(Gdx.files.internal("goblinBack.png"));
            goblinLeft = new Texture(Gdx.files.internal("goblinLeft.png"));
            goblinRight = new Texture(Gdx.files.internal("goblinRight.png"));
            currentTexture = goblinFront;

            textureWidth = goblinFront.getWidth();
            textureHeight = goblinFront.getHeight();
        } catch (Exception e) {
            Gdx.app.error("Goblin", "Failed to load goblin textures: " + e.getMessage());
            goblinFront = goblinBack = goblinLeft = goblinRight = new Texture("placeholder.png");
            currentTexture = goblinFront;
            textureWidth = textureHeight = 32;
        }

        this.setSize(64, 64);
        assignRandomExit();
    }

    private void assignRandomExit() {
        int exitIndex = (int)(Math.random() * 3);

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        switch (exitIndex) {
            case 0:
                exitX = screenWidth / 2f;
                exitY = 0;
                break;
            case 1:
                exitX = screenWidth * 0.22f;
                exitY = screenHeight * 0.86f;
                break;
            case 2:
                exitX = screenWidth * 0.77f;
                exitY = screenHeight * 0.86f;
                break;
        }
    }

    @Override
    public void update(float deltaTime, Player player) {
        float oldX = x, oldY = y;

        if (!aggro) {
            // Move toward exit if not aggro
            Vector2 direction = new Vector2(exitX - (x + width / 2), exitY - (y + height / 2));
            if (direction.len() > 2f) {
                direction.nor();
                x += direction.x * speed * deltaTime;
                y += direction.y * speed * deltaTime;

                float dx = x - oldX;
                float dy = y - oldY;

                if (Math.abs(dx) > Math.abs(dy)) {
                    currentTexture = (dx > 0) ? goblinRight : goblinLeft;
                } else {
                    currentTexture = (dy > 0) ? goblinBack : goblinFront;
                }
            }

            if (!escaped && isReachedExit()) {
                escaped = true;
                Gdx.app.log("Goblin", "Escaped through the door!");
                pendingRemove = true;
            }
        } else {
            // If aggro, chase the player
            Vector2 toPlayer = new Vector2(player.getX() - x, player.getY() - y);
            if (toPlayer.len() > 2f) {
                toPlayer.nor();
                x += toPlayer.x * speed * deltaTime;
                y += toPlayer.y * speed * deltaTime;

                if (Math.abs(toPlayer.x) > Math.abs(toPlayer.y)) {
                    currentTexture = (toPlayer.x > 0) ? goblinRight : goblinLeft;
                } else {
                    currentTexture = (toPlayer.y > 0) ? goblinBack : goblinFront;
                }
            }

            // Tambahkan logic damage ke player kalau perlu
            //if (distanceToPlayer(player) < 30f) {
            //player.takeDamage(damage);
            //}
        }

        if (pendingRemove) {
            escapeTimer += deltaTime;
            if (escapeTimer >= 0.1f) {
                this.health = 0;
            }
        }
    }

    private boolean isReachedExit() {
        float distance = Vector2.dst(x + width / 2, y + height / 2, exitX, exitY);
        return distance < 20f;
    }

    @Override
    public void onHit(Player player) {
        health -= 10;
        Gdx.app.log("Goblin", "Hit! Health: " + health);
        aggro = true; // mulai agresif jika diserang
    }

    @Override
    public boolean shouldAttackPlayer(Player player) {
        return aggro;  // hanya menyerang kalau sudah diserang
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(currentTexture, x, y, width, height);
    }

    @Override
    public void dispose() {
        goblinFront.dispose();
        goblinBack.dispose();
        goblinLeft.dispose();
        goblinRight.dispose();
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }
}
