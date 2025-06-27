package io.github.some_example_name.managers;

import io.github.some_example_name.entities.*;
import java.util.ArrayList;
import java.util.List;

public class RoundManager {
    private int currentRound = 0;
    private Round current;

    public void startNextRound() {
        currentRound++;

        List<String> monsterTypes = new ArrayList<>();

        if (currentRound == 1) {
            monsterTypes = List.of("slime", "slime", "slime", "slime", "slime");
        } else if (currentRound == 2) {
            monsterTypes = List.of("slime", "goblin", "goblin", "slime");
        } else if (currentRound == 3) {
            monsterTypes = List.of("goblin", "giant", "giant");
        } else if (currentRound == 4) {
            monsterTypes = List.of("zombie", "giant", "zombie", "zombie", "giant");
        } else if (currentRound == 5) {
            monsterTypes = List.of("buffalo", "giant", "giant");
        }

        List<Monsters> monsters = new ArrayList<>();
        for (String type : monsterTypes) {
            monsters.add(createMonster(type));
        }

        current = new Round(currentRound, monsters);
    }


    private Monsters createMonster(String type) {
//        nanti uncomment, sesuaikan nama monster ya ges
//        if (type.equalsIgnoreCase("goblin")) return new Goblin();
//        if (type.equalsIgnoreCase("zombie")) return new Zombie();
//        if (type.equalsIgnoreCase("giant")) return new Giant();
//        if (type.equalsIgnoreCase("troll")) return new Troll();
//        if (type.equalsIgnoreCase("buffalo")) return new Buffalo();
        throw new IllegalArgumentException("Unknown monster type: " + type);
    }

    public List<Monsters> getCurrentMonsters() {
        return current.getMonsters();
    }

    public boolean isRoundComplete() {
        return current.getMonsters().isEmpty();
    }
}
