package io.github.some_example_name.loots;

import io.github.some_example_name.entities.Player;

public class DamageLootEffect implements LootEffect{
    private int bonusDamage;

    public DamageLootEffect(int bonusDamage){
        this.bonusDamage = bonusDamage;
    }

    @Override
    public void apply(Player player){
        player.setDamage(player.getDamage() + bonusDamage);
    }

}
