package io.github.lama06.schneckenhaus.util.scheduler;

import org.bukkit.Server;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.logging.Level;

public class FoliaSchedulerBridge {

    public Object getGlobalRegionScheduler(Server server) {
        try {
            Method getGlobalRegionSchedulerMethod = server.getClass().getMethod("getGlobalRegionScheduler");
            return getGlobalRegionSchedulerMethod.invoke(server);
        } catch (Exception e) {
            System.err.println("Error getting GlobalRegionScheduler: " + e.getMessage());
            return null;
        }
    }

    public Object createTaskConsumer(Runnable task) throws Exception {
        // Create a proxy for the Consumer interface that will call our task
        return Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class<?>[] { Class.forName("java.util.function.Consumer") },
                (proxy, method, args) -> {
                    if (method.getName().equals("accept")) {
                        task.run();
                        return null;
                    } else if (method.getName().equals("equals")) {
                        return proxy == args[0];
                    } else if (method.getName().equals("hashCode")) {
                        return System.identityHashCode(proxy);
                    } else if (method.getName().equals("toString")) {
                        return "TaskConsumer{" + task + "}";
                    }
                    return null;
                });
    }

    public Object scheduleDelayedTask(Plugin plugin, Object scheduler, Runnable task, long delay) {
        try {
            Object consumer = createTaskConsumer(task);
            
            Method runDelayedMethod = scheduler.getClass().getMethod("runDelayed",
                    Plugin.class, 
                    Class.forName("java.util.function.Consumer"), 
                    long.class);
            
            return runDelayedMethod.invoke(scheduler, plugin, consumer, delay);
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Error running Folia delayed task", e);
            return null;
        }
    }

    public Object scheduleRepeatingTask(Plugin plugin, Object scheduler, Runnable task, long delay, long period) {
        try {
            Object consumer = createTaskConsumer(task);
            
            Method runAtFixedRateMethod = scheduler.getClass().getMethod("runAtFixedRate",
                    Plugin.class, 
                    Class.forName("java.util.function.Consumer"), 
                    long.class, 
                    long.class);
            
            return runAtFixedRateMethod.invoke(scheduler, plugin, consumer, delay, period);
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Error running Folia repeating task", e);
            return null;
        }
    }

    public boolean cancelTask(Object task) {
        if (task == null) {
            return false;
        }
        
        try {
            // Call the cancel method using reflection
            Method cancelMethod = task.getClass().getMethod("cancel");
            cancelMethod.invoke(task);
            return true;
        } catch (Exception e) {
            System.err.println("Error canceling Folia task: " + e.getMessage());
            return false;
        }
    }
}
