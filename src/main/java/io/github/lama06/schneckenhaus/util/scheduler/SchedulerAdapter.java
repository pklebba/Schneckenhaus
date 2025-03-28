package io.github.lama06.schneckenhaus.util.scheduler;

import org.bukkit.plugin.Plugin;

/**
 * Adapter interface for scheduling tasks that works with both Bukkit and Folia.
 */
public interface SchedulerAdapter {
    /**
     * Runs a task once after the specified delay in ticks.
     * 
     * @param plugin The plugin instance
     * @param task The task to run
     * @param delay The delay in ticks
     * @return A task ID that can be used to cancel the task
     */
    int runTaskLater(Plugin plugin, Runnable task, long delay);
    
    /**
     * Runs a task repeatedly with the specified delay and period in ticks.
     * 
     * @param plugin The plugin instance
     * @param task The task to run
     * @param delay The initial delay in ticks
     * @param period The period in ticks
     * @return A task ID that can be used to cancel the task
     */
    int runTaskTimer(Plugin plugin, Runnable task, long delay, long period);
    
    /**
     * Cancels a task with the given ID.
     * 
     * @param taskId The ID of the task to cancel
     */
    void cancelTask(int taskId);
    
    /**
     * Determines if this scheduler is running on Folia.
     * 
     * @return true if running on Folia, false otherwise
     */
    boolean isFolia();
}

