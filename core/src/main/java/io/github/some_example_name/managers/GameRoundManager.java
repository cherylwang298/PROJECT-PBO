
package io.github.some_example_name.managers;

import com.badlogic.gdx.Gdx;
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

    private Array<Monsters> activeMonsters;
    private boolean roundActive;

    private int currentWaveIndex;
    private int monstersSpawnedInCurrentWave;
    private float spawnTimer;
    private final float SPAWN_INTERVAL = 0.5f;

    private MonsterSpawnConfig currentSpawnConfig;

    private CityHealthListener cityHealthListener;

    // Arena padding bawah untuk cek monster keluar arena
    private final float paddingBottom = Gdx.graphics.getHeight() * 0.35f;

    public GameRoundManager(Main game, Player player) {
        this.game = game;
        this.player = player;
        this.allRounds = new ArrayList<>();
        this.activeMonsters = new Array<>();
        this.currentRoundIndex = 0;
        this.roundActive = false;

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
        if (roundIndex < 0 || roundIndex >= allRounds.size()) {
            Gdx.app.error("GameRoundManager", "Invalid round index: " + roundIndex);
            return;
        }

        this.currentRoundIndex = roundIndex;
        activeMonsters.clear();
        roundActive = true;
        currentWaveIndex = 0;
        monstersSpawnedInCurrentWave = 0;
        spawnTimer = 0f;

        Gdx.app.log("GameRoundManager", "Starting Round " + getCurrentRoundNumber());

        if (!allRounds.get(currentRoundIndex).getSpawnWaves().isEmpty()) {
            currentSpawnConfig = allRounds.get(currentRoundIndex).getSpawnWaves().get(currentWaveIndex);
        } else {
            endRound();
        }
    }

    public void update(float deltaTime) {
        Iterator<Monsters> iterator = activeMonsters.iterator();
        while (iterator.hasNext()) {
            Monsters monster = iterator.next();
            if (monster.isAlive()) {
                monster.update(deltaTime, player);
//tambah logic buat ngurangin hati di gamescreen
                if (monster.hasReachedCity()) {
                    Gdx.app.log("GameRoundManager", "Monster reached city, damage = " + monster.getDamage());
                    // Informasikan ke listener kalau monster sudah sampai kota
                    if (cityHealthListener != null) {
                        cityHealthListener.onMonsterReachedCity(monster.getDamage());
                    }
                    monster.kill();
                }

                // Cek apakah monster keluar arena (bawah)
                if (monster.getY() <= paddingBottom) {
                    // Monster keluar arena, langsung hilang & kurangi hearts
                    monster.kill();
                    if (cityHealthListener != null) {
                        cityHealthListener.onMonsterReachedCity(monster.getDamage());
                    }
                    Gdx.app.log("GameRoundManager", "Monster keluar arena, hearts dikurangi.");
                }

//                sampe sini

            } else {
                monster.dispose();
                iterator.remove();
                Gdx.app.log("GameRoundManager", "Monster defeated and removed.");
            }
        }

        // Spawn logic
        if (roundActive) {
            if (currentSpawnConfig != null) {
                if (monstersSpawnedInCurrentWave < currentSpawnConfig.getCount()) {
                    spawnTimer += deltaTime;
                    if (spawnTimer >= SPAWN_INTERVAL) {
                        spawnMonster(currentSpawnConfig.getMonsterType());
                        monstersSpawnedInCurrentWave++;
                        spawnTimer = 0f;
                    }
                } else {
                    currentWaveIndex++;
                    if (currentWaveIndex < allRounds.get(currentRoundIndex).getSpawnWaves().size()) {
                        currentSpawnConfig = allRounds.get(currentRoundIndex).getSpawnWaves().get(currentWaveIndex);
                        monstersSpawnedInCurrentWave = 0;
                        spawnTimer = 0f;
                    } else {
                        currentSpawnConfig = null;
                    }
                }
            }

            if (currentSpawnConfig == null && activeMonsters.isEmpty()) {
                endRound();
            }
        }
    }

    private void spawnMonster(Class<? extends Monsters> monsterClass) {
        try {
            float arenaMarginX = Gdx.graphics.getWidth() * 0.20f;
            float arenaMarginTop = Gdx.graphics.getHeight() * 0.40f;
            float arenaMarginBottom = Gdx.graphics.getHeight() * 0.35f;

            float arenaMinX = arenaMarginX;
            float arenaMaxX = Gdx.graphics.getWidth() - arenaMarginX;
            float arenaMinY = arenaMarginBottom;
            float arenaMaxY = Gdx.graphics.getHeight() - arenaMarginTop;

            float spawnX = arenaMinX + (float)(Math.random() * (arenaMaxX - arenaMinX));
            float spawnY = arenaMinY + (float)(Math.random() * (arenaMaxY - arenaMinY));

            Monsters monster = monsterClass.getConstructor(float.class, float.class).newInstance(spawnX, spawnY);
            activeMonsters.add(monster);

            Gdx.app.log("GameRoundManager", "Spawned " + monsterClass.getSimpleName() + " at (" + spawnX + ", " + spawnY + ")");
        } catch (Exception e) {
            Gdx.app.error("GameRoundManager", "Failed to spawn monster: " + e.getMessage());
        }
    }

    private void endRound() {
        roundActive = false;
        Gdx.app.log("GameRoundManager", "Round " + getCurrentRoundNumber() + " complete!");
    }

    public void render(SpriteBatch batch) {
        for (Monsters monster : activeMonsters) {
            monster.render(batch);
        }
    }

    public void startNextRound() {
        if (currentRoundIndex + 1 < allRounds.size()) {
            startRound(currentRoundIndex + 1);
        } else {
            Gdx.app.log("GameRoundManager", "All rounds completed. You win!");
        }
    }

    public void dispose() {
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
