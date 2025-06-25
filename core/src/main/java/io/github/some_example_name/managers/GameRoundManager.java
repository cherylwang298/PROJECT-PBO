package io.github.some_example_name.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import io.github.some_example_name.Main;
import io.github.some_example_name.entities.Monsters;
import io.github.some_example_name.entities.Player;
import io.github.some_example_name.entities.Slime;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameRoundManager {

    private final Main game;
    private final Player player;

    private final List<RoundConfig> allRounds;
    private int currentRoundIndex;
    private final Array<Monsters> activeMonsters;

    private boolean roundActive;
    private boolean roundStarted = false;

    private CityHealthListener cityHealthListener;
    private final float paddingBottom = Gdx.graphics.getHeight() * 0.4f; //cheryl edit

//    cheryl
    private Texture roundStartTexture;
    private boolean showRoundStart = false;
    private float roundStartTimer = 0f;
    private final float ROUND_START_DURATION = 2f; // 2 detik

    public GameRoundManager(Main game, Player player) {
        this.game = game;
        this.player = player;
        this.allRounds = new ArrayList<>();
        this.activeMonsters = new Array<>();
        this.currentRoundIndex = 0;
        this.roundActive = false;

        roundStartTexture = new Texture(Gdx.files.internal("round1.png")); // pastikan file berada di assets/

        initializeRounds();
    }

    public void setCityHealthListener(CityHealthListener listener) {
        this.cityHealthListener = listener;
    }

    private void initializeRounds() {
        List<MonsterSpawnConfig> round1 = new ArrayList<>();
        round1.add(new MonsterSpawnConfig(Slime.class, 3));
        allRounds.add(new RoundConfig(1, round1));

        List<MonsterSpawnConfig> round2 = new ArrayList<>();
        round2.add(new MonsterSpawnConfig(Slime.class, 5));
        allRounds.add(new RoundConfig(2, round2));

        Gdx.app.log("GameRoundManager", "Rounds initialized: " + allRounds.size());
    }

    public void startRound(int roundIndex) {
        if (roundStarted) return;

        if (roundIndex < 0 || roundIndex >= allRounds.size()) {
            Gdx.app.error("GameRoundManager", "Invalid round index: " + roundIndex);
            return;
        }

        this.currentRoundIndex = roundIndex;
        activeMonsters.clear();
        roundActive = true;
        roundStarted = true;

        showRoundStart = true;
        roundStartTimer = 0f;

//        RoundConfig round = allRounds.get(currentRoundIndex);
//        for (MonsterSpawnConfig config : round.getSpawnWaves()) {
//            spawnAllMonstersNow(config);
//        }
    }

    private void spawnAllMonstersNow(MonsterSpawnConfig config) {
        Class<? extends Monsters> monsterClass = config.getMonsterType();
        int count = config.getCount();

        int[] spawnCountPerDoor = new int[3]; // Track jumlah slime dari tiap pintu

        for (int i = 0; i < count; i++) {
            int randomIndex = (int) (Math.random() * 3); // 0–2 (pintu)
            int offsetY = spawnCountPerDoor[randomIndex] * 20; // geser posisi spawn kalau dobel
            spawnCountPerDoor[randomIndex]++;
            spawnMonster(monsterClass, randomIndex, offsetY);
        }
    }

    private void spawnMonster(Class<? extends Monsters> monsterClass, int index, float spawnOffsetY) {
        try {
            float screenW = Gdx.graphics.getWidth();
            float screenH = Gdx.graphics.getHeight();

            float[][] spawnPoints = new float[][] {
                { screenW * 0.27f, screenH * 0.74f }, // kiri
                { screenW * 0.49f, screenH * 0.71f }, // tengah
                { screenW * 0.72f, screenH * 0.74f }  // kanan
            };

            float baseExitX = screenW * 0.50f;
            float baseExitY = screenH * 0.08f;

            float offsetX = (float) (Math.random() * 100 - 50); // -50 to +50
            float offsetY = (float) (Math.random() * 40 - 20);  // -20 to +20

            float finalExitX = baseExitX + offsetX;
            float finalExitY = baseExitY + offsetY;

            int safeIndex = index % spawnPoints.length;
            float spawnX = spawnPoints[safeIndex][0];
            float spawnY = spawnPoints[safeIndex][1] + spawnOffsetY;

            Monsters monster;

            if (monsterClass == Slime.class) {
                monster = new Slime(spawnX, spawnY, finalExitX, finalExitY);
            } else {
                monster = monsterClass.getConstructor(float.class, float.class)
                    .newInstance(spawnX, spawnY);
            }

            activeMonsters.add(monster);
            Gdx.app.log("GameRoundManager", "Spawned " + monsterClass.getSimpleName() +
                " at (" + (int)spawnX + ", " + (int)spawnY + ") targeting (" +
                (int)finalExitX + ", " + (int)finalExitY + ")");
        } catch (Exception e) {
            Gdx.app.error("GameRoundManager", "Failed to spawn monster", e);
        }
    }

//    public void update(float deltaTime) {
//
//
//        Iterator<Monsters> iterator = activeMonsters.iterator();
//        while (iterator.hasNext()) {
//            Monsters monster = iterator.next();
//            if (monster.isAlive()) {
//                monster.update(deltaTime, player);
//
//                if (monster.hasReachedCity() || monster.getY() <= paddingBottom) {
//                    if (cityHealthListener != null) {
//                        cityHealthListener.onMonsterReachedCity(monster.getDamage());
//                    }
//                    monster.kill();
//                    Gdx.app.log("GameRoundManager", "Monster reached city or exited arena.");
//                }
//
//            } else {
//                monster.dispose();
//                iterator.remove();
//                Gdx.app.log("GameRoundManager", "Monster defeated and removed.");
//            }
//        }
//
//        if (roundActive && activeMonsters.isEmpty()) {
//            endRound();
//        }
//    }

    public void update(float deltaTime) {
//cheryl
        // Tampilkan round start image sebelum spawn monster
        if (showRoundStart) {
            roundStartTimer += deltaTime;
            if (roundStartTimer >= ROUND_START_DURATION) {
                showRoundStart = false;

                // Setelah delay selesai, baru spawn monster
                RoundConfig round = allRounds.get(currentRoundIndex);
                for (MonsterSpawnConfig config : round.getSpawnWaves()) {
                    spawnAllMonstersNow(config);
                }
            }

            return; // Jangan update monster dulu saat masih tampil "Round"
        }

        // Update monster seperti biasa
        Iterator<Monsters> iterator = activeMonsters.iterator();
        while (iterator.hasNext()) {
            Monsters monster = iterator.next();
            if (monster.isAlive()) {
                monster.update(deltaTime, player);

                if (monster.hasReachedCity() || monster.getY() <= paddingBottom) {
                    if (cityHealthListener != null) {
                        cityHealthListener.onMonsterReachedCity(monster.getDamage());
                    }
                    monster.kill();
                    Gdx.app.log("GameRoundManager", "Monster reached city or exited arena.");
                }

            } else {
                monster.dispose();
                iterator.remove();
                Gdx.app.log("GameRoundManager", "Monster defeated and removed.");
            }
        }

        if (roundActive && activeMonsters.isEmpty()) {
            endRound();
        }
    }


    private void endRound() {
        roundActive = false;
        Gdx.app.log("GameRoundManager", "Round " + getCurrentRoundNumber() + " complete!");
    }

    public void render(SpriteBatch batch) {
//        cheryl - temp
        if (showRoundStart && roundStartTexture != null) {
            float centerX = Gdx.graphics.getWidth() / 2f - roundStartTexture.getWidth() / 2f;
            float centerY = Gdx.graphics.getHeight() / 2f - roundStartTexture.getHeight() / 2f;
            batch.draw(roundStartTexture, centerX, centerY);
        }

        for (Monsters monster : activeMonsters) {
            monster.render(batch);
        }
    }

    public void startNextRound() {
        if (currentRoundIndex + 1 < allRounds.size()) {
            roundStarted = false;
            startRound(currentRoundIndex + 1);
        } else {
            Gdx.app.log("GameRoundManager", "All rounds completed. You win!");
        }
    }

    public void dispose() {
//        temp cheryl
        if (roundStartTexture != null) {
            roundStartTexture.dispose();
        }
        for (Monsters monster : activeMonsters) {
            monster.dispose();
        }
        activeMonsters.clear();
    }

    public int getCurrentRoundNumber() {
        if (currentRoundIndex < allRounds.size()) {
            return allRounds.get(currentRoundIndex).getRoundNumber();
        }
        return -1;
    }

    public int getActiveMonsterCount() {
        return activeMonsters.size;
    }

    public boolean isRoundActive() {
        return roundActive;
    }

    public interface CityHealthListener {
        void onMonsterReachedCity(int damage);
    }
}
