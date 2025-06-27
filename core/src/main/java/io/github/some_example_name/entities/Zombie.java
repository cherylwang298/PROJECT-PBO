    package io.github.some_example_name.entities;

    import com.badlogic.gdx.Gdx;
    import com.badlogic.gdx.graphics.Texture;
    import com.badlogic.gdx.graphics.g2d.SpriteBatch;
    import com.badlogic.gdx.math.Vector2;
    import io.github.some_example_name.loots.HpLootEffect;
    import io.github.some_example_name.loots.LootEffect;
    import io.github.some_example_name.loots.LootManager;

    import java.util.Random;

    public class Zombie extends Monsters {

        private Texture front1, front2;
        private Texture left1;
        private Texture right1;
        private Texture currentTexture;

        private float animationTimer = 0f;
        private final float FRAME_DURATION = 0.25f;
        private boolean toggleFrame = false;

        private float exitX, exitY;
        private float waypointX, waypointY;
        private boolean useWaypoint = true;
        private boolean reachedWaypoint = false;

    //    private final float attackCooldown = 1.5f;
    //    private float attackTimer = 0f;

        private final Random random = new Random();

        //testing loot: cheryl
        private LootEffect lootEffect;
        private LootManager lootManager;

        public Zombie(float x, float y) {
            super(x, y);
            this.attackRadius = 80f; // lebih masuk akal dari 999
            this.attackDelay = 1.5f;
            this.health = 60;
            this.maxHealth = 60;  //buat maxhealth healthbar
            this.speed = 50.0f;
            this.damageToplayer = 15; //toplayer: yang diterima player kalau kenak attack
            this.damageTocity = 1;
            this.isAggressive = true;
            this.lootEffect = new HpLootEffect(10);//test
            this.lootManager = lootManager;

            try {
                front1 = new Texture(Gdx.files.internal("zombieFront1.png"));
                front2 = new Texture(Gdx.files.internal("zombieFront2.png"));
                left1 = new Texture(Gdx.files.internal("zombieLeft1.png"));
                right1 = new Texture(Gdx.files.internal("zombieRight1.png"));
                currentTexture = front1;
            } catch (Exception e) {
                Gdx.app.error("Zombie", "Failed to load zombie textures.");
                front1 = front2 = left1 = right1 = new Texture("placeholder.png");
                currentTexture = front1;
            }

            setSize(64, 64);
            assignRandomExitWithWaypoint();
        }

        public Zombie(float x, float y, float exitX, float exitY, LootManager lootManager) {
            this(x, y);
            this.exitX = exitX;
            this.exitY = exitY;

            //test loot
            this.lootManager = lootManager;
            this.lootEffect = new HpLootEffect(20);
        }

        private void assignRandomExitWithWaypoint() {
            float screenWidth = Gdx.graphics.getWidth();
            float screenHeight = Gdx.graphics.getHeight();

            exitX = screenWidth * 0.50f;
            exitY = screenHeight * 0.08f;

            int style = (int)(Math.random() * 3);
            useWaypoint = true;
            reachedWaypoint = false;

            if (style == 0) {
                float offsetX = (float)(Math.random() * 150 - 75);
                waypointX = x + offsetX;
                waypointY = y;
            } else if (style == 1) {
                waypointX = x;
                waypointY = y - (float)(Math.random() * 80 + 40);
            } else {
                useWaypoint = false;
                reachedWaypoint = true;
            }
        }


        @Override
        public void update(float deltaTime, Player player) {
            animationTimer += deltaTime;
            if (animationTimer >= FRAME_DURATION) {
                toggleFrame = !toggleFrame;
                animationTimer = 0f;
            }

            updateAttackCooldown(deltaTime);

            if (isPlayerInRange(player)) {
                attackPlayer(player); // berhenti + serang player
                return;
            }

            // Zombie selalu agresif → kejar player
            moveTowardPlayer(player, deltaTime);

            // Animasi arah
            float dx = player.getX() - x;
            float dy = player.getY() - y;

            if (Math.abs(dx) > Math.abs(dy)) {
                currentTexture = (dx > 0) ? right1 : left1;
            } else {
                currentTexture = toggleFrame ? front2 : front1;
            }

            if (hasReachedCity()) {
                kill();
            }
        }


        @Override
        public void onHit(Player player) {
            health -= 10;
            Gdx.app.log("Zombie", "Hit! Health: " + health);

            if (health <= 0) {
                int roll = random.nextInt(3);
                switch (roll) {
                    case 0:
                        player.takeDamage(-15);
                        Gdx.app.log("Loot", "Player gained +15 HP!");
                        break;
                    case 1:
                        Gdx.app.log("Loot", "Player gained speed bonus! (placeholder)");
                        break;
                    case 2:
                        Gdx.app.log("Loot", "Player gained +5 damage! (placeholder)");
                        break;
                }
                kill();
            }
        }

        @Override
        public boolean shouldAttackPlayer(Player player) {
            return true;
        }

        @Override
        public void render(SpriteBatch batch) {
            batch.draw(currentTexture, x, y, width, height);
        }

        @Override
        public void dispose() {
            front1.dispose(); front2.dispose();
            left1.dispose();
            right1.dispose();
        }

        @Override
        public void setSize(float width, float height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public boolean hasReachedCity() {
            return y <= 50;
        }

        @Override
        public void kill() {
            this.health = 0;

            //test loot
            if (lootManager != null && lootEffect != null){
                lootManager.spawnLoot(x,y,lootEffect);
            }
        }
    }
