package io.github.some_example_name.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import io.github.some_example_name.Main;
import io.github.some_example_name.entities.*;
import io.github.some_example_name.screens.GameOverScreen;

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
    private final float paddingBottom = Gdx.graphics.getHeight() * 0.4f;

    private Texture roundStartTexture;
    private boolean showRoundStart = false;
    private float roundStartTimer = 0f;
    private final float ROUND_START_DURATION = 2f;

    private int cityHearts = 5; // jumlah nyawa kota

    public GameRoundManager(Main game, Player player) {
        this.game = game;
        this.player = player;
        this.allRounds = new ArrayList<>();
        this.activeMonsters = new Array<>();
        this.currentRoundIndex = 0;
        this.roundActive = false;

        roundStartTexture = new Texture(Gdx.files.internal("round1.png"));
        initializeRounds();
    }

    public void setCityHealthListener(CityHealthListener listener) {
        this.cityHealthListener = listener;
    }

    public void setCityHearts(int hearts) {
        this.cityHearts = hearts;
    }

    public int getCityHearts() {
        return cityHearts;
    }

    private void initializeRounds() {
        List<MonsterSpawnConfig> round1 = new ArrayList<>();
        round1.add(new MonsterSpawnConfig(Slime.class, 3));
        allRounds.add(new RoundConfig(1, round1));

        List<MonsterSpawnConfig> round2 = new ArrayList<>();
        round2.add(new MonsterSpawnConfig(Slime.class, 1));
        round2.add(new MonsterSpawnConfig(Goblin.class, 2));
        allRounds.add(new RoundConfig(2, round2));

        List<MonsterSpawnConfig> round3 = new ArrayList<>();
        round3.add(new MonsterSpawnConfig(Goblin.class, 1));
        round3.add(new MonsterSpawnConfig(Giant.class, 1));
        allRounds.add(new RoundConfig(3, round3));

        List<MonsterSpawnConfig> round4 = new ArrayList<>();
        round4.add(new MonsterSpawnConfig(Zombie.class, 1));
        round4.add(new MonsterSpawnConfig(Giant.class, 1));
        allRounds.add(new RoundConfig(4, round4));

        List<MonsterSpawnConfig> round5 = new ArrayList<>();
        round5.add(new MonsterSpawnConfig(Buffalo.class, 1));
        allRounds.add(new RoundConfig(5, round5));

        Gdx.app.log("GameRoundManager", "Rounds initialized: " + allRounds.size());
    }

    public void startRound(int roundIndex) {
        if (roundStarted) return;
        if (roundIndex < 0 || roundIndex >= allRounds.size()) return;

        this.currentRoundIndex = roundIndex;
        activeMonsters.clear();
        roundActive = true;
        roundStarted = true;

        showRoundStart = true;
        roundStartTimer = 0f;
    }

    private void spawnAllMonstersNow(MonsterSpawnConfig config) {
        Class<? extends Monsters> monsterClass = config.getMonsterType();
        int count = config.getCount();
        int[] spawnCountPerDoor = new int[3];

        for (int i = 0; i < count; i++) {
            int randomIndex = (int) (Math.random() * 3);
            int offsetY = spawnCountPerDoor[randomIndex] * 20;
            spawnCountPerDoor[randomIndex]++;
            spawnMonster(monsterClass, randomIndex, offsetY);
        }
    }

    private void spawnMonster(Class<? extends Monsters> monsterClass, int index, float spawnOffsetY) {
        try {
            float screenW = Gdx.graphics.getWidth();
            float screenH = Gdx.graphics.getHeight();

            float[][] spawnPoints = {
                { screenW * 0.27f, screenH * 0.74f },
                { screenW * 0.49f, screenH * 0.71f },
                { screenW * 0.72f, screenH * 0.74f }
            };

            float finalExitX = screenW * 0.50f;
            float finalExitY = screenH * 0.08f;

            int safeIndex = index % spawnPoints.length;
            float spawnX = spawnPoints[safeIndex][0];
            float spawnY = spawnPoints[safeIndex][1] + spawnOffsetY;

            Monsters monster;
            if (monsterClass == Slime.class) {
                monster = new Slime(spawnX, spawnY, finalExitX, finalExitY);
            } else if (monsterClass == Goblin.class) {
                monster = new Goblin(spawnX, spawnY, finalExitX, finalExitY);
            } else if (monsterClass == Giant.class) {
                monster = new Giant(spawnX, spawnY, finalExitX, finalExitY);
            } else if (monsterClass == Zombie.class) {
                monster = new Zombie(spawnX, spawnY, finalExitX, finalExitY);
            } else if (monsterClass == Buffalo.class) {
                monster = new Buffalo(spawnX, spawnY, finalExitX, finalExitY);
            } else {
                Gdx.app.error("GameRoundManager", "Unsupported monster type");
                return;
            }

            activeMonsters.add(monster);
            Gdx.app.log("GameRoundManager", "Spawned " + monsterClass.getSimpleName());
        } catch (Exception e) {
            Gdx.app.error("GameRoundManager", "Failed to spawn monster", e);
        }
    }

    public void update(float deltaTime) {
        if (showRoundStart) {
            roundStartTimer += deltaTime;
            if (roundStartTimer >= ROUND_START_DURATION) {
                showRoundStart = false;
                RoundConfig round = allRounds.get(currentRoundIndex);
                for (MonsterSpawnConfig config : round.getSpawnWaves()) {
                    spawnAllMonstersNow(config);
                }
            }
            return;
        }

        Iterator<Monsters> iterator = activeMonsters.iterator();
        while (iterator.hasNext()) {
            Monsters monster = iterator.next();
            if (monster.isAlive()) {
                monster.update(deltaTime, player);
                if (monster.hasReachedCity() || monster.getY() <= paddingBottom) {
                    if (cityHealthListener != null) {
                        cityHealthListener.onMonsterReachedCity(monster.getDamage());
                    }
                    cityHearts -= monster.getDamage();
                    Gdx.app.log("GameRoundManager", "City hearts: " + cityHearts);
                    monster.kill();

                    if (cityHearts <= 0) {
                        gameOver();
                        return;
                    }
                }
            } else {
                monster.dispose();
                iterator.remove();
            }
        }

        if (roundActive && activeMonsters.isEmpty()) {
            if (cityHearts <= 0) {
                gameOver();
            } else {
                endRound();
                startNextRound();
            }
        }
    }

    private void endRound() {
        roundActive = false;
        Gdx.app.log("GameRoundManager", "Round " + getCurrentRoundNumber() + " complete!");
    }

    private void gameOver() {
        roundActive = false;
        Gdx.app.log("GameRoundManager", "Game Over! City hearts habis.");
        game.setScreen(new GameOverScreen(game));
    }

    public void render(SpriteBatch batch) {
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
        if (roundStartTexture != null) roundStartTexture.dispose();
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
