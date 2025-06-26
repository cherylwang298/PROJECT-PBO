////package io.github.some_example_name.entities;
////
////import com.badlogic.gdx.Gdx;
////import com.badlogic.gdx.graphics.Texture;
////import com.badlogic.gdx.graphics.g2d.SpriteBatch;
////import com.badlogic.gdx.math.Vector2;
////
////public class Slime extends Monsters {
////
////    private Texture slimeFront, slimeBack, slimeLeft, slimeRight;
////    private Texture currentTexture;
////
////    private float textureWidth, textureHeight;
////    private float exitX, exitY;
////    private float waypointX, waypointY;
////
////    private boolean escaped = false;
////    private boolean pendingRemove = false;
////    private float escapeTimer = 0f;
////
////    private boolean reachedWaypoint = false;
////
//////    private int Damage;
////
////    public Slime(float x, float y) {
////        super(x, y);
////
////        this.health = 30;
////        this.speed = 60.0f;
////        this.Damage = 1;
////        this.isAggressive = false;
////
////
////        try {
////            slimeFront = new Texture(Gdx.files.internal("slimeFront.png"));
////            slimeBack = new Texture(Gdx.files.internal("slimeBack.png"));
////            slimeLeft = new Texture(Gdx.files.internal("slimeLeft.png"));
////            slimeRight = new Texture(Gdx.files.internal("slimeRight.png"));
////            currentTexture = slimeFront;
////
////            textureWidth = slimeFront.getWidth();
////            textureHeight = slimeFront.getHeight();
////        } catch (Exception e) {
////            Gdx.app.error("Slime", "Failed to load slime textures: " + e.getMessage());
////            slimeFront = slimeBack = slimeLeft = slimeRight = new Texture("placeholder.png");
////            currentTexture = slimeFront;
////            textureWidth = textureHeight = 32;
////        }
////
////        this.setSize(64, 64);
////
////        assignRandomExitWithWaypoint();
////    }
////
////    private void assignRandomExitWithWaypoint() {
////        int exitIndex = (int)(Math.random() * 3);
////
////        float screenWidth = Gdx.graphics.getWidth();
////        float screenHeight = Gdx.graphics.getHeight();
////
////        switch (exitIndex) {
////            case 0: // Pintu depan (bawah tengah)
////                exitX = screenWidth / 2f;
////                exitY = 0;
////                waypointX = exitX;
////                waypointY = screenHeight * 0.25f; // buffer aman
////                break;
////            case 1: // Pintu kiri atas
////                exitX = screenWidth * 0.24f;
////                exitY = screenHeight * 0.86f;
////                waypointX = exitX;
////                waypointY = screenHeight * 0.65f;
////                break;
////            case 2: // Pintu kanan atas
////                exitX = screenWidth * 0.76f;
////                exitY = screenHeight * 0.86f;
////                waypointX = exitX;
////                waypointY = screenHeight * 0.65f;
////                break;
////        }
////    }
////
////    @Override
////    public void update(float deltaTime, Player player) {
////        float oldX = x, oldY = y;
////
////        Vector2 target;
////
////        if (!reachedWaypoint) {
////            target = new Vector2(waypointX, waypointY);
////            if (isReached(target, 10f)) {
////                reachedWaypoint = true;
////            }
////        } else {
////            target = new Vector2(exitX, exitY);
////        }
////
////        // Movement logic:
////        Vector2 direction = new Vector2(target.x - (x + width / 2), target.y - (y + height / 2));
////        float distance = direction.len();
////
////        if (distance > 2f) {
////            direction.nor();
////            float moveStep = speed * deltaTime;
////            if (moveStep > distance) moveStep = distance;
////
////            x += direction.x * moveStep;
////            y += direction.y * moveStep;
////
////            float dx = x - oldX;
////            float dy = y - oldY;
////
////            if (Math.abs(dx) > Math.abs(dy)) {
////                currentTexture = (dx > 0) ? slimeRight : slimeLeft;
////            } else {
////                currentTexture = (dy > 0) ? slimeBack : slimeFront;
////            }
////        }
////
////        if (reachedWaypoint && isReachedExit()) {
////            escaped = true;
////            Gdx.app.log("Slime", "Escaped through the door!");
////            pendingRemove = true;
////        }
////
////        if (pendingRemove) {
////            escapeTimer += deltaTime;
////            if (escapeTimer >= 0.1f) {
////                this.health = 0;
////            }
////        }
////    }
////
////    private boolean isReached(Vector2 target, float threshold) {
////        return Vector2.dst(x + width / 2, y + height / 2, target.x, target.y) < threshold;
////    }
////
////    private boolean isReachedExit() {
////        return isReached(new Vector2(exitX, exitY), 20f);
////    }
////
////    @Override
////    public void onHit(Player player) {
////        health -= 10;
////        Gdx.app.log("Slime", "Hit! Health: " + health);
////    }
////
////    @Override
////    public boolean shouldAttackPlayer(Player player) {
////        return false;
////    }
////
////    @Override
////    public void render(SpriteBatch batch) {
////        batch.draw(currentTexture, x, y, width, height);
////    }
////
////    @Override
////    public void dispose() {
////        slimeFront.dispose();
////        slimeBack.dispose();
////        slimeLeft.dispose();
////        slimeRight.dispose();
////    }
////
////    @Override
////    public float getWidth() { return width; }
////    @Override
////    public float getHeight() { return height; }
////    @Override
////    public void setSize(float width, float height) { this.width = width; this.height = height; }
////}
//
//
//package io.github.some_example_name.entities;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.math.Vector2;
//
//public class Slime extends Monsters {
//
//    private Texture slimeFront, slimeBack, slimeLeft, slimeRight;
//    private Texture currentTexture;
//
//    private float textureWidth, textureHeight;
//    private float exitX, exitY;
//    private float waypointX, waypointY;
//
//    private boolean escaped = false;
//    private boolean pendingRemove = false;
//    private float escapeTimer = 0f;
//
//    private boolean reachedWaypoint = false;
//
////    private int Damage;
//
//    public Slime(float x, float y) {
//        super(x, y);
//
//        this.health = 30;
//        this.speed = 60.0f;
//        this.Damage = 1;
//        this.isAggressive = false;
//
//
//        try {
//            slimeFront = new Texture(Gdx.files.internal("slimeFront.png"));
//            slimeBack = new Texture(Gdx.files.internal("slimeBack.png"));
//            slimeLeft = new Texture(Gdx.files.internal("slimeLeft.png"));
//            slimeRight = new Texture(Gdx.files.internal("slimeRight.png"));
//            currentTexture = slimeFront;
//
//            textureWidth = slimeFront.getWidth();
//            textureHeight = slimeFront.getHeight();
//        } catch (Exception e) {
//            Gdx.app.error("Slime", "Failed to load slime textures: " + e.getMessage());
//            slimeFront = slimeBack = slimeLeft = slimeRight = new Texture("placeholder.png");
//            currentTexture = slimeFront;
//            textureWidth = textureHeight = 32;
//        }
//
//        this.setSize(64, 64);
//
//        assignRandomExitWithWaypoint();
//    }
//
//    public Slime(float x, float y, float exitX, float exitY) {
//        this(x, y); // panggil constructor utama
//        this.exitX = exitX;
//        this.exitY = exitY;
//    }
//
//
//    private void assignRandomExitWithWaypoint() {
//        int exitIndex = (int)(Math.random() * 3);
//
//        float screenWidth = Gdx.graphics.getWidth();
//        float screenHeight = Gdx.graphics.getHeight();
//
//        switch (exitIndex) {
//            case 0: // Pintu depan (bawah tengah)
//                exitX = screenWidth / 2f;
//                exitY = 0;
//                waypointX = exitX;
//                waypointY = screenHeight * 0.25f; // buffer aman
//                break;
//            case 1: // Pintu kiri atas
//                exitX = screenWidth * 0.24f;
//                exitY = screenHeight * 0.86f;
//                waypointX = exitX;
//                waypointY = screenHeight * 0.65f;
//                break;
//            case 2: // Pintu kanan atas
//                exitX = screenWidth * 0.76f;
//                exitY = screenHeight * 0.86f;
//                waypointX = exitX;
//                waypointY = screenHeight * 0.65f;
//                break;
//        }
//    }
//
//    @Override
//    public void update(float deltaTime, Player player) {
//        float slimeCenterX = x + width / 2;
//        float slimeCenterY = y + height / 2;
//
//        // Jarak aman dari tembok arena
//        float margin = 40f;
//
//        Vector2 move = new Vector2();
//
//        // Hindari tembok kiri/kanan
//        if (slimeCenterX < margin) {
//            move.x = 1; // dorong ke kanan
//        } else if (slimeCenterX > Gdx.graphics.getWidth() - margin) {
//            move.x = -1; // dorong ke kiri
//        }
//
//        // Hindari tembok atas/bawah
//        if (slimeCenterY > Gdx.graphics.getHeight() - margin) {
//            move.y = -1; // dorong ke bawah
//        } else if (slimeCenterY < margin) {
//            move.y = 1; // dorong ke atas
//        }
//
//        // Kalau tidak dekat tembok, tetap menuju target keluar
//        if (move.isZero()) {
//            move.set(exitX - slimeCenterX, exitY - slimeCenterY).nor();
//        } else {
//            move.nor(); // normalisasi agar tidak terlalu cepat
//        }
//
//        // Apply movement
//        x += move.x * speed * deltaTime;
//        y += move.y * speed * deltaTime;
//
//        // Tentukan arah animasi
//        if (Math.abs(move.x) > Math.abs(move.y)) {
//            currentTexture = (move.x > 0) ? slimeRight : slimeLeft;
//        } else {
//            currentTexture = (move.y > 0) ? slimeBack : slimeFront;
//        }
//    }
//
//
//    private boolean isReached(Vector2 target, float threshold) {
//        return Vector2.dst(x + width / 2, y + height / 2, target.x, target.y) < threshold;
//    }
//
//    private boolean isReachedExit() {
//        return isReached(new Vector2(exitX, exitY), 20f);
//    }
//
//    @Override
//    public void onHit(Player player) {
//        health -= 10;
//        Gdx.app.log("Slime", "Hit! Health: " + health);
//    }
//
//    @Override
//    public boolean shouldAttackPlayer(Player player) {
//        return false;
//    }
//
//    @Override
//    public void render(SpriteBatch batch) {
//        batch.draw(currentTexture, x, y, width, height);
//    }
//
//    @Override
//    public void dispose() {
//        slimeFront.dispose();
//        slimeBack.dispose();
//        slimeLeft.dispose();
//        slimeRight.dispose();
//    }
//
//    @Override
//    public float getWidth() { return width; }
//    @Override
//    public float getHeight() { return height; }
//    @Override
//    public void setSize(float width, float height) { this.width = width; this.height = height; }
//}


package io.github.some_example_name.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Slime extends Monsters {

    private Texture slimeFront, slimeBack, slimeLeft, slimeRight;
    private Texture currentTexture;

    private float textureWidth, textureHeight;
    private float exitX, exitY;
    private float waypointX, waypointY;

    private boolean useWaypoint = true;
    private boolean reachedWaypoint = false;

    private enum MovementStyle { BELOK_DULU, TURUN_DULU, LANGSUNG }
    private MovementStyle movementStyle;

    public Slime(float x, float y) {
        super(x, y);

        this.health = 30;
        this.maxHealth=30;  //buat maxhealth healthbar
        this.speed = 60.0f;
        this.damageTocity = 1;
        this.isAggressive = false;

        try {
            slimeFront = new Texture(Gdx.files.internal("slimeFront.png"));
            slimeBack = new Texture(Gdx.files.internal("slimeBack.png"));
            slimeLeft = new Texture(Gdx.files.internal("slimeLeft.png"));
            slimeRight = new Texture(Gdx.files.internal("slimeRight.png"));
            currentTexture = slimeFront;

            textureWidth = slimeFront.getWidth();
            textureHeight = slimeFront.getHeight();
        } catch (Exception e) {
            Gdx.app.error("Slime", "Failed to load slime textures: " + e.getMessage());
            slimeFront = slimeBack = slimeLeft = slimeRight = new Texture("placeholder.png");
            currentTexture = slimeFront;
            textureWidth = textureHeight = 32;
        }

        this.setSize(64, 64);
        assignRandomExitWithWaypoint();
    }

    public Slime(float x, float y, float exitX, float exitY) {
        this(x, y);
        this.exitX = exitX;
        this.exitY = exitY;
    }

    private void assignRandomExitWithWaypoint() {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        exitX = screenWidth * 0.50f;
        exitY = screenHeight * 0.08f;

        int style = (int)(Math.random() * 3); // 0, 1, 2
        useWaypoint = true;
        reachedWaypoint = false;

        if (style == 0) {
            // Belok dulu ke kiri/kanan lalu turun
            float offsetX = (float)(Math.random() * 150 - 75); // ±75px
            waypointX = x + offsetX;
            waypointY = y;
            movementStyle = MovementStyle.BELOK_DULU;

        } else if (style == 1) {
            // Turun dulu, baru belok
            waypointX = x;
            waypointY = y - (float)(Math.random() * 80 + 40); // 40–120px
            movementStyle = MovementStyle.TURUN_DULU;

        } else {
            // Langsung ke pintu keluar
            useWaypoint = false;
            reachedWaypoint = true;
            movementStyle = MovementStyle.LANGSUNG;
        }

        Gdx.app.log("Slime", "Spawned with movement style: " + movementStyle.name());
    }

    @Override
    public void update(float deltaTime, Player player) {
        float slimeCenterX = x + width / 2;
        float slimeCenterY = y + height / 2;

        Vector2 target = new Vector2(
            (!useWaypoint || reachedWaypoint) ? exitX : waypointX,
            (!useWaypoint || reachedWaypoint) ? exitY : waypointY
        );

        Vector2 move = new Vector2(target.x - slimeCenterX, target.y - slimeCenterY);

        if (move.len() < 5f) {
            if (useWaypoint && !reachedWaypoint) {
                reachedWaypoint = true;
            } else {
                move.setZero();
            }
        } else {
            move.nor();
        }

        x += move.x * speed * deltaTime;
        y += move.y * speed * deltaTime;

        // Arah animasi berdasarkan gerakan
        if (Math.abs(move.x) > Math.abs(move.y)) {
            currentTexture = (move.x > 0) ? slimeRight : slimeLeft;
        } else if (Math.abs(move.y) > 0.01f) {
            currentTexture = (move.y > 0) ? slimeBack : slimeFront;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(currentTexture, x, y, width, height);
    }

    @Override
    public void onHit(Player player) {
        health -= 10;
        Gdx.app.log("Slime", "Hit! Health: " + health);
    }

    @Override
    public boolean shouldAttackPlayer(Player player) {
        return false;
    }

    @Override
    public void dispose() {
        slimeFront.dispose();
        slimeBack.dispose();
        slimeLeft.dispose();
        slimeRight.dispose();
    }

    @Override
    public float getWidth() { return width; }

    @Override
    public float getHeight() { return height; }

    @Override
    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean hasReachedCity() {
        return y <= 50; // sesuaikan threshold pintu keluar
    }

    @Override
    public void kill() {
        this.health = 0;
    }
}
