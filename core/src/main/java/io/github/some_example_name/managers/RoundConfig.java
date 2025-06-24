package io.github.some_example_name.managers;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Defines a single round in the game, including the types and counts of monsters to spawn.
 */
public class RoundConfig {
    private final int roundNumber;
    private final List<MonsterSpawnConfig> spawnWaves; // List of monster waves for this round


    /**
     * Constructor for RoundConfig.
     * @param roundNumber The number of this round.
     * @param spawnWaves A list of MonsterSpawnConfig objects, each representing a wave of monsters.
     */
    public RoundConfig(int roundNumber, List<MonsterSpawnConfig> spawnWaves) {
        this.roundNumber = roundNumber;
        this.spawnWaves = Collections.unmodifiableList(new ArrayList<>(spawnWaves)); // Make it unmodifiable
    }


    public int getRoundNumber() {
        return roundNumber;
    }


    public List<MonsterSpawnConfig> getSpawnWaves() {
        return spawnWaves;
    }
}
