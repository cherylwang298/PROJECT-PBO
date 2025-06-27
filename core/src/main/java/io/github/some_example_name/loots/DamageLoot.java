package io.github.some_example_name.loots;

public class DamageLoot extends Loot {
    public DamageLoot(float x, float y, int bonusDamage) {
        super(x, y, "damage", bonusDamage); // akan load damage.png
    }
}
