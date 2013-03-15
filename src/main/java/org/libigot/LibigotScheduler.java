package org.libigot;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class LibigotScheduler {

    protected final Map<Integer, LibigotTask> tasks = new HashMap<Integer, LibigotTask>();
    protected final Map<Integer, LibigotTask> asyncTasks = Collections.synchronizedMap(new HashMap<Integer, LibigotTask>());

    protected LibigotScheduler() {}

    void addTask(LibigotTask task) {
        task.id = getFreeId();
        task.endTick = Libigot.getServer().getCurrentTick() + task.delay;
        if(!task.async) {
            tasks.put(task.id, task);
        } else {
            asyncTasks.put(task.id, task);
        }
    }

    void removeTask(int id, boolean async) {
        if(!async) {
            this.tasks.remove(id);
        } else {
            this.asyncTasks.remove(id);
        }
    }

    private int getFreeId() {
        int id = 1;
        while(this.tasks.containsKey(id) || this.asyncTasks.containsKey(id)) {
            id++;
        }
        return id;
    }

    protected long getEndTick(LibigotTask task) {
        return task.endTick;
    }

    protected long getDelay(LibigotTask task) {
        return task.delay;
    }

    protected void setEndTick(LibigotTask task, long tick) {
        task.endTick = tick;
    }

    protected void setFinished(LibigotTask task) {
        task.finished = true;
        task.id = -1;
    }
}
