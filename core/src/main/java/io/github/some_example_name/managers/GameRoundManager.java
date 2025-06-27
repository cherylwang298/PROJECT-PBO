package io.github.some_example_name.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import io.github.some_example_name.Main;
import io.github.some_example_name.entities.*;
import io.github.some_example_name.loots.LootManager;
import io.github.some_example_name.screens.GameOverScreen;
import io.github.some_example_name.screens.VictoryScreen;

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

    //test loot
    private final LootManager lootManager;

    //pengecheckan round 5: cw
    private int totalMonsterThisRound = 0;
    private int monstersKilledByPlayer = 0;

    public GameRoundManager(Main game, Player player) {
        this.game = game;
        this.player = player;
        this.allRounds = new ArrayList<>();
        this.activeMonsters = new Array<>();
        this.currentRoundIndex = 0;
        this.roundActive = false;
        this.lootManager = new LootManager(); //test loot

//        roundStartTexture = new Texture(Gdx.files.internal("round1.png"));//test rouond 1
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

    //cheryl: edit + monsters ke setiap rounds
    private void initializeRounds() {
        List<MonsterSpawnConfig> round1 = new ArrayList<>();
        round1.add(new MonsterSpawnConfig(Slime.class, 5));
        allRounds.add(new RoundConfig(1, round1));

       List<MonsterSpawnConfig> round2 = new ArrayList<>();
        round2.add(new MonsterSpawnConfig(Slime.class, 2));
        round2.add(new MonsterSpawnConfig(Goblin.class, 2));
//        round2.add(new MonsterSpawnConfig(Zombie.class, 3));
        allRounds.add(new RoundConfig(2, round2));

        List<MonsterSpawnConfig> round3 = new ArrayList<>();
        round3.add(new MonsterSpawnConfig(Goblin.class, 2));
        round3.add(new MonsterSpawnConfig(Giant.class, 1));
        allRounds.add(new RoundConfig(3, round3));

        List<MonsterSpawnConfig> round4 = new ArrayList<>();
        round4.add(new MonsterSpawnConfig(Zombie.class, 3));
        round4.add(new MonsterSpawnConfig(Giant.class, 2));
        allRounds.add(new RoundConfig(4, round4));

        List<MonsterSpawnConfig> round5 = new ArrayList<>();
        round5.add(new MonsterSpawnConfig(Buffalo.class, 1));
        round5.add(new MonsterSpawnConfig(Giant.class, 2));
        allRounds.add(new RoundConfig(5, round5));

        Gdx.app.log("GameRoundManager", "Rounds initialized: " + allRounds.size());
    }

//    public void startRound(int roundIndex) {
//        if (roundStarted) return;
//        if (roundIndex < 0 || roundIndex >= allRounds.size()) return;
//
//        this.currentRoundIndex = roundIndex;
//        activeMonsters.clear();
//        roundActive = true;
//        roundStarted = true;
//
//        showRoundStart = true;
//        roundStartTimer = 0f;
//    }

    public void startRound(int roundIndex) {
        if (roundStarted) return;
        if (roundIndex < 0 || roundIndex >= allRounds.size()) return;

        monstersKilledByPlayer = 0;
        totalMonsterThisRound = 0;
        this.currentRoundIndex = roundIndex;
        activeMonsters.clear();
        roundActive = true;
        roundStarted = true;

        // SET GAMBAR BERDASARKAN RONDE
        int roundNum = allRounds.get(roundIndex).getRoundNumber();
        String textureFile = "Round" + roundNum + ".png";

        // Dispose texture lama kalau ada
        if (roundStartTexture != null) roundStartTexture.dispose();

        try {
            roundStartTexture = new Texture(Gdx.files.internal(textureFile));
            Gdx.app.log("GameRoundManager", "Loaded texture: " + textureFile);
        } catch (Exception e) {
            Gdx.app.error("GameRoundManager", "Failed to load texture for round: " + roundNum, e);
            roundStartTexture = null; // supaya tidak crash saat render
        }

        showRoundStart = true;
        roundStartTimer = 0f;
    }


    private void spawnAllMonstersNow(MonsterSpawnConfig config) {
        Class<? extends Monsters> monsterClass = config.getMonsterType();
        int count = config.getCount();
        totalMonsterThisRound += count; //coba: cheryl
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
                monster = new Zombie(spawnX, spawnY, finalExitX, finalExitY, lootManager);
            } else if (monsterClass == Buffalo.class) {
                monster = new Buffalo(spawnX, spawnY, finalExitX, finalExitY);
                Gdx.app.log("GameRoundManager", "Created Buffalo!");
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

                //checl klw player hp <= 0 juga game over: edit cher
                if (player.getHp() <= 0) {
                    Gdx.app.log("GameRoundManager", "Game Over! Player mati.");
                    gameOver();
                    return;
                }


                if (monster.hasReachedCity() || monster.getY() <= paddingBottom) {
                    if (cityHealthListener != null) {
                        cityHealthListener.onMonsterReachedCity(monster.getDamageTocity());
                    }
                    cityHearts -= monster.getDamageTocity();
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

//        if (roundActive && activeMonsters.isEmpty()) {
//            if (cityHearts <= 0) {
//                gameOver();
//            } else {
//                endRound();
//                startNextRound();
//            }
//        }

        lootManager.update(deltaTime, player); //test loot

        //check menang/ga di round 5
        if (roundActive && activeMonsters.isEmpty()) {
            boolean isFinalRound = getCurrentRoundNumber() == 5;
            boolean cityAlive = cityHearts >= 1;
            boolean playerAlive = player.getHp() > 0;

            if (isFinalRound) {
                if (monstersKilledByPlayer == totalMonsterThisRound && cityAlive && playerAlive) {
                    victory();
                    return;
                }
                else {
                    gameOver();
                    return;
                }
            }

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
        roundStarted = false; //coba
        Gdx.app.log("GameRoundManager", "Round " + getCurrentRoundNumber() + " complete!");
    }

    private void gameOver() {
        roundActive = false;
        Gdx.app.log("GameRoundManager", "Game Over! City hearts habis.");
        game.setScreen(new GameOverScreen(game));
    }

    //kalau menang: victory scree ny dteigger
    private void victory() {
        roundActive = false;
        Gdx.app.log("GameRoundManager", "Victory! Player & city survived final round.");
        game.setScreen(new VictoryScreen(game));
    }


    public void render(SpriteBatch batch) {
        if (showRoundStart && roundStartTexture != null) {
            float centerX = Gdx.graphics.getWidth() / 2f - roundStartTexture.getWidth() / 2f;
            float centerY = Gdx.graphics.getHeight() / 2f - roundStartTexture.getHeight() / 2f;
            batch.draw(roundStartTexture, centerX, centerY);
        }

        for (Monsters monster : activeMonsters) {
            monster.render(batch);
            monster.renderHealthBar(batch);
        }

        lootManager.render(batch); //test loot
    }

    public void startNextRound() {
        if (currentRoundIndex + 1 < allRounds.size()) {
            roundStarted = false;
            startRound(currentRoundIndex + 1);
        } else {
            Gdx.app.log("GameRoundManager", "All rounds completed. You win!");
        }
    }

//    public void handleClick(float x, float y) {
//        for (Monsters monster : activeMonsters) {
//            if (monster.isAlive() && monster.contains(x, y)) {
//                int damagePlayer = (int) player.getDamage();
//                monster.takeDamage(damagePlayer);
//                Gdx.app.log("GameRoundManager", "Monster clicked and took " + damagePlayer + " damage.");
//                break; // hanya satu monster yang terkena klik
//            }
//        }
//    }

    public void handleClick(float x, float y) {
        Rectangle attackBox = player.getAttackRect();

        for (Monsters monster : activeMonsters) {
            if (monster.isAlive() && monster.contains(x, y)) {
                // Tambahan: hanya jika monster berada di dalam area serang depan
                Rectangle monsterRect = new Rectangle(monster.getX(), monster.getY(), monster.getWidth(), monster.getHeight());

                if (attackBox.overlaps(monsterRect)) {
                    int damagePlayer = (int) player.getDamage();
                    monster.takeDamage(damagePlayer);
                    Gdx.app.log("GameRoundManager", "Monster clicked and took " + damagePlayer + " damage.");

                   if (!monster.isAlive()){
                       monstersKilledByPlayer++;
                       Gdx.app.log("GameRoundManager", "Monster Killed by Player. Total: " + monstersKilledByPlayer);
                   }

                    break; // satu monster saja
                } else {
                    Gdx.app.log("GameRoundManager", "Monster diklik tapi tidak di depan player.");
                }
            }
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
