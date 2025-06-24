package io.github.some_example_name.managers;

import io.github.some_example_name.entities.Monsters;
import java.util.ArrayList;
import java.util.List;

public class Round {

    private int roundNumber;
    private List<Monsters> monsters;

    public Round(int roundNumber, List<Monsters> monsters) {
        this.roundNumber = roundNumber;
        this.monsters = monsters;
    }

    public List<Monsters> getMonsters() {
        return monsters;
    }

    public int getRoundNumber() {
        return roundNumber;
    }
}

