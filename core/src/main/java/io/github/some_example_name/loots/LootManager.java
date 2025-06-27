package io.github.some_example_name.loots;

import com.badlogic.gdx.Gdx; // Needed for Gdx.audio
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.some_example_name.entities.Player;
import com.badlogic.gdx.audio.Sound; // Needed for Sound

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class LootManager {

    private final List<Loot> activeLoots;
    private Sound lootCollectSound; // The single Sound object for all loot pickups

    public LootManager() {
        activeLoots = new LinkedList<>();

        // Load the loot collection sound ONCE when LootManager is created
        String lootSoundPath = "Loot (audio).MP3"; // <--- VERIFY THIS EXACT PATH/FILENAME
        try {
            this.lootCollectSound = Gdx.audio.newSound(Gdx.files.internal(lootSoundPath));
            Gdx.app.log("LootManager", "Loot pickup sound loaded successfully for manager from: " + lootSoundPath);
        } catch (Exception e) {
            Gdx.app.error("LootManager", "FAILED to load loot pickup sound for manager from '" + lootSoundPath + "': " + e.getMessage());
            this.lootCollectSound = null; // Set to null if loading fails
        }
    }

    // Spawn loot baru di posisi x,y dengan efek loot tertentu
    public void spawnLoot(float x, float y, LootEffect effect) {
        // Pass the single, pre-loaded sound object to the new Loot instance
        Loot loot = new Loot(x, y, effect, lootCollectSound);
        activeLoots.add(loot);
    }

    // Update loot: cek apakah player collect loot
    public void update(float deltaTime, Player player) {
        Iterator<Loot> iterator = activeLoots.iterator();
        while (iterator.hasNext()) {
            Loot loot = iterator.next();

            if (loot.checkCollision(player)) {
                loot.applyEffectToPlayer(player);

                // Dispose of the Loot object's *texture* and other individual resources.
                // The shared sound will NOT be disposed here.
                loot.dispose();

                // Remove loot from the active list after it's collected and disposed
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

    // Dispose all resources when LootManager is no longer needed
    // LootManager should dispose its own resources. If it extends nothing, remove @Override.
    public void dispose() {
        // Dispose all individual Loot instances (their textures)
        for (Loot loot : activeLoots) {
            loot.dispose();
        }
        activeLoots.clear();

        // IMPORTANT: Dispose the shared loot collect sound ONCE here!
        if (lootCollectSound != null) {
            lootCollectSound.dispose();
            Gdx.app.log("LootManager", "Shared loot collect sound disposed.");
        }
    }
}
