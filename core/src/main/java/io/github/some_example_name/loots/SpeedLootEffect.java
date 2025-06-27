package io.github.some_example_name.loots;

import io.github.some_example_name.entities.Player;

public class SpeedLootEffect implements LootEffect{
    private int bonusSpeed;

    public SpeedLootEffect(int bonusSpeed){
        this.bonusSpeed = bonusSpeed;
    }

    @Override
    public void apply(Player player){
        player.setSpeed(player.getSpeed() + bonusSpeed);
    }

    @Override
    public String getEffectName(){
        return "SpeedLoot";
    }
}
