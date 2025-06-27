package io.github.some_example_name.loots;

import io.github.some_example_name.entities.Player;

public class HpLootEffect implements LootEffect{
    private int bonusHp;

    public HpLootEffect(int bonusHp){
        this.bonusHp = bonusHp;
    }

    @Override
    public void apply(Player player){
        if (player.getHp() + bonusHp > player.getMaxHp()){
            player.setHp(player.getMaxHp());
        }
        else {
            player.setHp(player.getHp() + bonusHp);
        }
 }

    @Override
    public String getEffectName(){
        return "HpLoot";
    }

}

