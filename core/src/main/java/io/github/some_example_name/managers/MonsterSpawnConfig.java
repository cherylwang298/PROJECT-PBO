package io.github.some_example_name.managers;


import io.github.some_example_name.entities.Monsters; // Import the base Monsters class


/**
 * Represents a configuration for spawning a specific type and amount of monsters.
 */
public class MonsterSpawnConfig {
    private final Class<? extends Monsters> monsterType;
    private final int count;


    /**
     * Constructor for MonsterSpawnConfig.
     * @param monsterType The Class object of the monster type to spawn (e.g., Slime.class).
     * @param count The number of monsters of this type to spawn.
     */
    public MonsterSpawnConfig(Class<? extends Monsters> monsterType, int count) {
        this.monsterType = monsterType;
        this.count = count;
    }


    public Class<? extends Monsters> getMonsterType() {
        return monsterType;
    }


    public int getCount() {
        return count;
    }
}
