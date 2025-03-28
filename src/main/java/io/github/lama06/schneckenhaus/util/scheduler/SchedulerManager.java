package io.github.lama06.schneckenhaus.util.scheduler;

import org.bukkit.plugin.Plugin;

/**
 * Provides the appropriate scheduler adapter for the current server platform.
 */
public class SchedulerManager {
    private static SchedulerAdapter instance;
    
    /**
     * Initialize the scheduler system.
     * 
     * @param plugin The plugin instance
     * @return The initialized scheduler adapter
     */
    public static SchedulerAdapter initialize(Plugin plugin) {
        if (instance != null) {
            return instance;
        }
        
        // Check if we're running on Folia
        boolean isFolia = false;
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            isFolia = true;
        } catch (ClassNotFoundException e) {
            // Not running on Folia
        }
        
        if (isFolia) {
            plugin.getLogger().info("Detected Folia - using regionized scheduler");
            instance = new FoliaSchedulerAdapter();
        } else {
            plugin.getLogger().info("Using standard Bukkit scheduler");
            instance = new BukkitSchedulerAdapter();
        }

        return instance;
    }
    
    /**
     * Get the scheduler adapter.
     * 
     * @return The scheduler adapter
     * @throws IllegalStateException if the scheduler has not been initialized
     */
    public static SchedulerAdapter getAdapter() {
        if (instance == null) {
            throw new IllegalStateException("Scheduler has not been initialized");
        }
        return instance;
    }
}
