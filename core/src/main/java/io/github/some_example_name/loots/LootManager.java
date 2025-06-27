package io.github.some_example_name.loots;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import io.github.some_example_name.entities.Player;
import io.github.some_example_name.loots.Loot;
import io.github.some_example_name.loots.LootEffect;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class LootManager {

    private final List<Loot> activeLoots;

    public LootManager() {
        activeLoots = new LinkedList<>();
    }

    // Spawn loot baru di posisi x,y dengan efek loot tertentu
    public void spawnLoot(float x, float y, LootEffect effect) {
        Loot loot = new Loot(x, y, effect);
        activeLoots.add(loot);
    }

    // Update loot: cek apakah player collect loot
    public void update(float deltaTime, Player player) {
        Iterator<Loot> iterator = activeLoots.iterator();
        while (iterator.hasNext()) {
            Loot loot = iterator.next();

            // Jika player sudah overlap dengan loot (collect)
            if (loot.isCollectedBy(player)) {
                // Terapkan efek loot ke player
                loot.getEffect().apply(player);
                // Hapus loot dari list setelah diambil
                iterator.remove();
            }
        }
    }

    // Render semua loot aktif
    public void render(SpriteBatch batch) {
        for (Loot loot : activeLoots) {
            loot.render(batch);
        }
    }

    // Dispose semua resource loot
    public void dispose() {
        for (Loot loot : activeLoots) {
            loot.dispose();
        }
        activeLoots.clear();
    }
}
