package io.github.some_example_name.loots;

public class HpLoot extends Loot {
    public HpLoot(float x, float y, int healAmount) {
        super(x, y, "hp", healAmount); // akan load hp.png
    }
}
