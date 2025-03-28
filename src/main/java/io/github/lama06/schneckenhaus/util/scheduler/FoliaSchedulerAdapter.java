package io.github.lama06.schneckenhaus.util.scheduler;

import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class FoliaSchedulerAdapter implements SchedulerAdapter {
    private final AtomicInteger nextTaskId = new AtomicInteger(1);
    private final Map<Integer, Object> tasks = new HashMap<>();
    private final FoliaSchedulerBridge bridge = new FoliaSchedulerBridge();
    
    @Override
    public int runTaskLater(Plugin plugin, Runnable task, long delay) {
        // Must use at least 1 tick for initial delay in Folia
        if (delay <= 0) {
            delay = 1;
        }
        
        Object scheduler = bridge.getGlobalRegionScheduler(plugin.getServer());
        if (scheduler == null) {
            plugin.getLogger().warning("Failed to get GlobalRegionScheduler");
            return -1;
        }
        
        Object scheduledTask = bridge.scheduleDelayedTask(plugin, scheduler, task, delay);
        if (scheduledTask == null) {
            return -1;
        }
        
        int taskId = nextTaskId.getAndIncrement();
        tasks.put(taskId, scheduledTask);
        return taskId;
    }

    @Override
    public int runTaskTimer(Plugin plugin, Runnable task, long delay, long period) {
        if (delay <= 0) {
            delay = 1;
        }
        
        Object scheduler = bridge.getGlobalRegionScheduler(plugin.getServer());
        if (scheduler == null) {
            plugin.getLogger().warning("Failed to get GlobalRegionScheduler");
            return -1;
        }
        
        Object scheduledTask = bridge.scheduleRepeatingTask(plugin, scheduler, task, delay, period);
        if (scheduledTask == null) {
            return -1;
        }
        
        int taskId = nextTaskId.getAndIncrement();
        tasks.put(taskId, scheduledTask);
        return taskId;
    }

    @Override
    public void cancelTask(int taskId) {
        Object scheduledTask = tasks.remove(taskId);
        if (scheduledTask != null) {
            bridge.cancelTask(scheduledTask);
        }
    }

    @Override
    public boolean isFolia() {
        return true;
    }
}
